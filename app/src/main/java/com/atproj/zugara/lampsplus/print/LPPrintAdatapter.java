package com.atproj.zugara.lampsplus.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.atproj.zugara.lampsplus.utils.ImageUtils;
import com.atproj.zugara.lampsplus.utils.ScalingUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by andre on 05-Feb-19.
 */

public class LPPrintAdatapter extends PrintDocumentAdapter {


    private Context context;
    private PrintedPdfDocument pdfDocument;
    private List<Bitmap> images;

    public LPPrintAdatapter(Context context, List<String> urls) {
        this.context = context;
        this.images = new ArrayList<>();
        for (String url : urls) {
            Uri uri = uriFromFile(context, new File(url));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                images.add(bitmap);
            } catch (IOException e) {
                ;
            }
        }
    }

    private Uri uriFromFile(Context context, File file) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file) :
                Uri.fromFile(file);
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        // Create a new PdfDocument with the requested page attributes
        pdfDocument = new PrintedPdfDocument(context, newAttributes);

        // Respond to cancellation request
        if (cancellationSignal.isCanceled() ) {
            callback.onLayoutCancelled();
            return;
        }

        // Compute the expected number of printed pages
        int pages = images.size();//computePageCount(newAttributes);

        if (pages > 0) {
            // Return print information to print framework
            PrintDocumentInfo info = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(pages)
                    .build();
            // Content layout reflow is complete
            callback.onLayoutFinished(info, true);
        } else {
            // Otherwise report an error to the print framework
            callback.onLayoutFailed("Page count calculation failed.");
        }
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        for (int i = 0; i < images.size(); i++) {
            PdfDocument.Page page = pdfDocument.startPage(i);

            // check for cancellation
            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
                pdfDocument.close();
                pdfDocument = null;
                return;
            }

            // Draw page content for printing
            drawPage(page, images.get(i));

            // Rendering is complete, so page can be finalized.
            pdfDocument.finishPage(page);
        }

        // Write PDF document to file
        try {
            pdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            pdfDocument.close();
            pdfDocument = null;
        }

        int[] arrPages = normalizeRanges(pages);
        PageRange[] pageRanges = convertIntegerArrayToPageRanges(arrPages);
        // Signal the print framework the document is complete
        callback.onWriteFinished(pageRanges);
    }

    /**
     * Gets an array of page ranges and returns an array of integers with all ranges expanded.
     */
    private int[] normalizeRanges(PageRange[] ranges) {
        // Expand ranges into a list of individual numbers.
        ArrayList<Integer> pages = new ArrayList<Integer>();
        for (PageRange range : ranges) {
            for (int i = range.getStart(); i <= range.getEnd(); i++) {
                pages.add(i);
            }
        }

        // Convert the list into array.
        int[] ret = new int[pages.size()];
        Iterator<Integer> iterator = pages.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private PageRange[] convertIntegerArrayToPageRanges(int[] pagesArray) {
        PageRange[] pageRanges;
        if (pagesArray != null) {
            pageRanges = new PageRange[pagesArray.length];
            for (int i = 0; i < pageRanges.length; i++) {
                int page = pagesArray[i];
                pageRanges[i] = new PageRange(page, page);
            }
        } else {
            // null corresponds to all pages in Chromium printing logic.
            pageRanges = new PageRange[] { PageRange.ALL_PAGES };
        }
        return pageRanges;
    }


    private void drawPage(PdfDocument.Page page, Bitmap bitmap) {
        Canvas canvas = page.getCanvas();
        Bitmap scaledBitmap = ImageUtils.compressBitmap(bitmap, page.getCanvas().getWidth() , page.getCanvas().getHeight(), ScalingUtils.ScalingLogic.FIT);
        canvas.drawBitmap(scaledBitmap, 0, 0, null);
    }
}

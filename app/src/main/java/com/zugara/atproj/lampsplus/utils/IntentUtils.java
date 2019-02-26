package com.zugara.atproj.lampsplus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.print.PrintHelper;
import android.util.Log;

import com.zugara.atproj.lampsplus.print.LPPrintAdatapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andre on 22-Dec-18.
 */

public class IntentUtils {

    public static void openStorageIntent(Activity activity, String type, int requestCode) {
        Intent storageIntent = new Intent();
        storageIntent.setType("image/*");
        storageIntent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(storageIntent, requestCode);
    }

    public static void openImage(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(uriFromFile(context, file), "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public static void createEmail(Context context, String subject, String message, List<String> pathAttachments) {
        ArrayList<Uri> attachments = new ArrayList<>();
        for (int i = 0; i < pathAttachments.size(); i++) {
            Uri attachment = uriFromFile(context, new File(pathAttachments.get(i)));
            attachments.add(attachment);
        }
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.putExtra(Intent.EXTRA_STREAM, attachments);
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
        context.startActivity(emailIntent);
    }

    /*public static void printImages(Activity activity, List<String> sources) {
        PrintHelper photoPrinter = new PrintHelper(activity);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FILL);

        for (String url : sources) {
            Uri uri = uriFromFile(activity, new File(url));
            try {
                photoPrinter.printBitmap("design", uri);
            } catch (FileNotFoundException e) {
                ;
            }
        }
    }*/

    public static void printImages(Activity activity, List<String> sources) {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);

        // Set job name, which will be displayed in the print queue
        String jobName = "Design";

        PrintAttributes printAttributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE, 72, 72))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document
        printManager.print(jobName, new LPPrintAdatapter(activity, sources), printAttributes);
    }



    private static Uri uriFromFile(Context context, File file) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file) :
                Uri.fromFile(file);
    }
}

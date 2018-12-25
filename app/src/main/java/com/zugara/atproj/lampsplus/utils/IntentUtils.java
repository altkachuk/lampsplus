package com.zugara.atproj.lampsplus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by andre on 22-Dec-18.
 */

public class IntentUtils {

    public static void openImage(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(uriFromFile(context, file), "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public static void createEmail(Context context, String subject, String message, String pathAttachment) {
        Uri attachment = uriFromFile(context, new File(pathAttachment));

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.putExtra(Intent.EXTRA_STREAM, attachment);
        context.startActivity(emailIntent);
    }

    private static Uri uriFromFile(Context context, File file) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file) :
                Uri.fromFile(file);
    }
}

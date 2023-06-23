package org.odk.collect.android.utilities;

import android.text.Html;

public class TextUtils {
    public static String formatHtmlText(String body) {
        String plainString = Html.fromHtml(body).toString();
        return cropText(plainString);
    }

    public static String cropText(String body) {
        body = body.trim().replaceAll("\\s+", " ");
        String plainString = body.substring(0, Math.min(120, body.length()));
        return plainString + "...";
    }
}

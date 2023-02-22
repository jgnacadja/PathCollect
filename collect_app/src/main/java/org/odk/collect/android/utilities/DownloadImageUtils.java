package org.odk.collect.android.utilities;

import android.graphics.drawable.Drawable;
import org.odk.collect.android.R;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadImageUtils {
    public static Drawable loadImageFromUrl(String url) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return Drawable.createFromStream(input, "src name");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
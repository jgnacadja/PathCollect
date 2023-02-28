package org.odk.collect.android.tasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import org.odk.collect.android.views.ForumViewHolder;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadImageTask extends AsyncTask<String, Void, Drawable> {

    private ForumViewHolder holder;

    public DownloadImageTask() {

    }

    public DownloadImageTask(ForumViewHolder holder) {
        this.holder = holder;
    }

    @Override
    protected Drawable doInBackground(String... urls) {
        String url = urls[0];
        if (url != null && !url.isEmpty()) {
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
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        holder.setIconDrawable(drawable);
    }

}
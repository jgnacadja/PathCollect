package org.odk.collect.android.tasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import com.google.gson.Gson;
import org.odk.collect.android.adapters.ArticleListAdapter;
import org.odk.collect.android.adapters.model.ArticleImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import timber.log.Timber;


public class DownloadArticleImageTask extends AsyncTask<String, Void, Drawable> {

    private ArticleListAdapter.ViewHolder holder;

    public DownloadArticleImageTask() {

    }

    public DownloadArticleImageTask(ArticleListAdapter.ViewHolder holder) {
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

                int status = connection.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return this.getImageDrawable(sb.toString());
                    default:
                        Timber.tag("DownloadArticleImageTask").i("Request failure");
                        return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private Drawable getImageDrawable(String jsonObject){
        ArticleImage articleImage = new Gson().fromJson(jsonObject, ArticleImage.class);

        try {
            URL urlConnection = new URL(articleImage.getMediaDetails().getSizes().getThumbnail().getSourceUrl());
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

    @Override
    protected void onPostExecute(Drawable drawable) {
        holder.setIconDrawable(drawable);
    }

}
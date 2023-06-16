package org.odk.collect.android.tasks;


import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class SendRequestToWP extends AsyncTask<String, Void, ResponseBody> {

    private Exception exception;

    @SuppressLint("TimberExceptionLogging")
    protected ResponseBody doInBackground(String... urls) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urls[0])
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body();
        } catch (IOException e) {
            Timber.tag("SendRequestToWP").d(e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(ResponseBody response) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
package org.odk.collect.android.tasks;


import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class SendRequestToWP extends AsyncTask<String, Void, ResponseBody> {

    private Exception exception;

    @SuppressLint("TimberExceptionLogging")
    protected ResponseBody doInBackground(String... urls) {
        OkHttpClient client = getHttpClient();
        Request request = new Request.Builder()
                .url(urls[0])
                .build();

        if (client != null) {
            try (Response response = client.newCall(request).execute()) {
                return response.body();
            } catch (IOException e) {
                Timber.tag("SendRequestToWP").d(e.getMessage());
                return null;
            }
        } else {
            Timber.tag("SendRequestToWP").d("Client is null");
            return null;
        }
    }

    protected void onPostExecute(ResponseBody response) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }

    private OkHttpClient getHttpClient() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
            newBuilder.sslSocketFactory(sc.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            newBuilder.hostnameVerifier((hostname, session) -> true);
            return newBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
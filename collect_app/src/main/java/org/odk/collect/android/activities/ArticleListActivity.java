package org.odk.collect.android.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.odk.collect.android.R;
import org.odk.collect.android.adapters.ArticleListAdapter;
import org.odk.collect.android.adapters.model.Article;
import org.odk.collect.android.utilities.ExternalWebPageHelper;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class ArticleListActivity extends AppCompatActivity implements
        ArticleListAdapter.ArticleItemClickListener {
    private List<Article> articles;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String tag = "ArticleListActivity";
        setContentView(R.layout.article_list);
        Timber.tag(tag).d("Initialisation de setContentView");

        initToolbar();
        ReadArticlesTask task = new ReadArticlesTask();
        task.execute("");

        recyclerView = findViewById(R.id.articlerecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ArticleListAdapter(articles, this, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.collect_app_name));
        setSupportActionBar(toolbar);
    }

    private void loadArticles() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://dssc-cms.000webhostapp.com/wp-json/wp/v2/posts?status=publish")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Traitement en cas d'erreur
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                try {
                    JSONArray jsonArray = new JSONArray(responseBody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Article article = new Gson().fromJson(jsonObject.toString(), Article.class);
                        articles.add(article);
                    }

                    runOnUiThread(() -> recyclerView.getAdapter().notifyDataSetChanged());
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    @Override
    public void onClick(int position) {
        if (MultiClickGuard.allowClick(getClass().getName())) {
            Article article = articles.get(position);
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(ExternalWebPageHelper.OPEN_URL, article.getLink());
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class ReadArticlesTask extends AsyncTask<String, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(String... urls) {
            String url = urls[0];
            List<Article> result = new ArrayList<>();
            if (url != null && !url.isEmpty()) {
                JSONArray jsonArray = this.getArticles(url);
                for (int i = 0; i < (jsonArray != null ? jsonArray.length() : 0); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Article article = this.parseArticle(jsonObject);
                        result.add(article);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                return result;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Article> result) {
            if (result == null) {
                articles = new ArrayList<>();
            } else {
                articles = result;
            }
        }

        private JSONArray getArticles(String url){
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-length", "0");
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setConnectTimeout(60);
                connection.setReadTimeout(60);
                connection.connect();
                int status = connection.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();

                        return new JSONArray(sb.toString());
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private Article parseArticle(JSONObject json){
            int id;
            try {
                id = json.getInt("id");
            } catch (JSONException e) {
                id = 0;
                e.printStackTrace();
            }
            String date;
            try {
                date = json.getString("date");
            } catch (JSONException e) {
                date = "";
                e.printStackTrace();
            }
            String link;
            try {
                link = json.getString("link");
            } catch (JSONException e) {
                link = "";
                e.printStackTrace();
            }
            String title;
            try {
                title = json.getJSONObject("title").getString("rendered");
            } catch (JSONException e) {
                title = "";
                e.printStackTrace();
            }
            String  content;
            try {
                  content = json.getJSONObject("content").getString("rendered");
            } catch (JSONException e) {
                content = "";
                e.printStackTrace();
            }
            String contentPreview;
            try {
                 contentPreview = json.getJSONObject("excerpt").getString("rendered");
            } catch (JSONException e) {
                contentPreview = "";
                e.printStackTrace();
            }
            String author;
            try {
                String imageBaseUrl = "https://dssc-cms.000webhostapp.com/wp-json/wp/v2/users/";
                author = imageBaseUrl+json.getInt("author");
            } catch (JSONException e) {
                author = "";
                e.printStackTrace();
            }
            String image;
            try {
                String imageBaseUrl = "https://dssc-cms.000webhostapp.com/wp-json/wp/v2/media/";
                image = imageBaseUrl+json.getInt("featured_media");
            } catch (JSONException e) {
                image = "";
                e.printStackTrace();
            }
            List<String> categories = null;
            return new Article(id, date, link, title, content, contentPreview, author, image, categories);
        }
    }
}

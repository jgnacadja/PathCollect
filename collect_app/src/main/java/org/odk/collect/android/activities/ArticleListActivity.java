package org.odk.collect.android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.ArticleListAdapter;
import org.odk.collect.android.adapters.model.Article;
import org.odk.collect.android.dao.WpApiService;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.android.utilities.ExternalWebPageHelper;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class ArticleListActivity extends CollectAbstractActivity implements
        ArticleListAdapter.ArticleItemClickListener {
    private List<Article> articles;
    private RecyclerView recyclerView;
    private ArticleListAdapter adapter;

    private static final String TAG = "ArticleListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list);
        DaggerUtils.getComponent(this).inject(this);

        initToolbar(getString(R.string.screen_articles_list), false, null);
        articles = new ArrayList<>();

        // initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.articleRecyclerView);
        adapter = new ArticleListAdapter(articles, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create API service instance
        WpApiService apiService = retrofit.create(WpApiService.class);

        // call API to get data
        Call<List<Article>> call = apiService.getPosts("publish");
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    articles.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Timber.tag(TAG).e("Response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Timber.tag(TAG).e(t);
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
}
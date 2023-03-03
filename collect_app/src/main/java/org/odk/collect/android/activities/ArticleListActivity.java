package org.odk.collect.android.activities;

import android.content.Intent;
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

import java.io.IOException;
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

        articles = new ArrayList<>();
        Timber.tag(tag).d("Creation d'un article");
        articles.add(new Article(
                54,
                "2023-03-01T15:13:11",
                "https://dssc-cms.000webhostapp.com/2023/03/planification-familiale-contraception",
                "Planification familiale/Contraception",
                "\n<h2>Principaux faits</h2>\n\n\n\n<ul>\n<li>En 2019, sur 1,9 milliard de femmes en âge de procréer (15-49 ans) dans le monde, elles sont 1,1 milliard à avoir besoin de planification familiale ; parmi celles-ci, 842 millions utilisent des méthodes de contraception, et 270 millions n’ont pas accès à la contraception dont elles ont besoin.[1,2]</li>\n\n\n\n<li>La proportion de femmes en âge de procréer (15 à 49 ans) utilisant des méthodes modernes de planification familiale – l’indicateur 3.7.1 des objectifs de développement durable – était de 75,7 % à l’échelle mondiale en 2019 ; toutefois, moins de la moitié des besoins en planification familiale étaient satisfaits en Afrique centrale et en Afrique de l’Ouest.[1]</li>\n\n\n\n<li>Une seule méthode contraceptive, les préservatifs, permet à la fois d’éviter une grossesse et la transmission des infections sexuellement transmissibles, dont le VIH.</li>\n\n\n\n<li>La contraception renforce les droits des populations à choisir le nombre d’enfants qu’elles souhaitent avoir et à déterminer l’espacement des naissances.</li>\n</ul>\n\n\n\n<p><strong>Bref aperçu</strong></p>\n\n\n\n<p>La garantie d’un accès de toutes les populations à leurs méthodes de contraception préférées permet de renforcer plusieurs droits humains tels que le droit à la vie et à la liberté, la liberté d’opinion et d’expression et le droit au travail et à l’éducation, tout en apportant d’autres avantages importants en matière de santé, et dans d’autres domaines. L’utilisation de la contraception protège les femmes, en particulier les adolescentes, des risques que peuvent représenter les grossesses pour leur santé et lorsque les naissances sont espacées de moins de deux ans, le taux de mortalité chez le nourrisson est supérieur de 45&nbsp;% au taux de mortalité lorsque les naissances sont espacées de 2 à 3 ans, et supérieur de 60% au taux de mortalité lorsqu’elles le sont de quatre ans ou plus.[3] La&nbsp;contraception offre tout un éventail d’avantages potentiels dans d’autres domaines que la santé, qui vont des possibilités élargies d’éducation et d’autonomisation des femmes, à la croissance durable de la population et au développement économique des pays.</p>\n\n\n\n<p>La prévalence de méthodes modernes de contraception chez les femmes mariées en âge de procréer a progressé dans le monde entre&nbsp;2000 et 2019 de 2,1 points de pourcentage, passant de 55,0&nbsp;% (95&nbsp;%&nbsp;IC&nbsp;: 53,7&nbsp;%-56,3&nbsp;%) à 57,1&nbsp;% (95&nbsp;% IC&nbsp;: 54,6&nbsp;%–59,5&nbsp;%).[1] La&nbsp;lenteur de cette augmentation s’explique, entre autres,&nbsp;par : le choix limité des méthodes&nbsp;; l’accès limité aux services, en particulier pour les jeunes, les populations les plus pauvres et les personnes non mariées&nbsp;; la crainte ou l’expérience d’effets secondaires&nbsp;; les&nbsp;barrières culturelles ou religieuses&nbsp;; la médiocre qualité des services disponibles&nbsp;; les opinions biaisées des utilisateurs et des prestataires contre certaines méthodes&nbsp;; et les obstacles liés au genre dans l’accès aux services.</p>\n",
                "<p>Principaux faits Bref aperçu La garantie d’un accès de toutes les populations à leurs méthodes de contraception préférées permet de renforcer plusieurs droits humains tels que le droit à la vie et à la liberté, la liberté d’opinion et d’expression et le droit au travail et à l’éducation, tout en apportant d’autres avantages importants en</p>\n<div><a class=\"btn-filled btn\" href=\"https://dssc-cms.000webhostapp.com/2023/03/planification-familiale-contraception\" title=\"Planification familiale/Contraception\">Lire la suite</a></div>\n",
                "RAM",
                "\"https://dssc-cms.000webhostapp.com/wp-json/wp/v2/media/55\"",
                null));
        Timber.tag(tag).d("Initialisation recyclerView");
        recyclerView = findViewById(R.id.articlerecyclerView);
        Timber.tag(tag).d("Initialisation de LayoutManager");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        Timber.tag(tag).d("Initialisation de setLayoutManger");

        recyclerView.setLayoutManager(layoutManager);
        Timber.tag(tag).d("Initialisation de setAdapter");

        recyclerView.setAdapter(new ArticleListAdapter(articles, this, this));
        Timber.tag(tag).d("Initialisation de setItemAnimator");
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        loadArticles();
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
}

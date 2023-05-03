package org.odk.collect.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.odk.collect.android.R;

public class SliderArticleActivity extends CollectAbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_article_reading);

        // Récupération du bouton
        ImageButton nextButton = findViewById(R.id.next_article);

        // Définition de l'action du bouton
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancement de la nouvelle activité
                Intent intent = new Intent(SliderArticleActivity.this, SliderForumActivity.class);
                startActivity(intent);
            }
        });

        Button skipButton = findViewById(R.id.skip_article);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderArticleActivity.this, LandingPageActivity.class);
                startActivity(intent);
            }
        });
    }
}

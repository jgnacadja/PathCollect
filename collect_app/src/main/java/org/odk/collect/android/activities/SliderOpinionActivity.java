package org.odk.collect.android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.odk.collect.android.R;

public class SliderOpinionActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        boolean hasAlreadyStarted = sharedPreferences.getBoolean("hasAlreadyStarted", false);

        if (!hasAlreadyStarted) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setContentView(R.layout.slider_opinion);

            // Récupération du bouton
            ImageButton nextButton = findViewById(R.id.next_opinion);

            // Définition de l'action du bouton
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lancement de la nouvelle activité
                    Intent intent = new Intent(SliderOpinionActivity.this, SliderArticleActivity.class);
                    startActivity(intent);
                }
            });

            Button skipButton = findViewById(R.id.skip_opinion);
            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SliderOpinionActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                }
            });

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasAlreadyStarted", true);
            editor.apply();
        } else {
            Intent intent = new Intent(SliderOpinionActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

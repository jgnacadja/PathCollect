package org.odk.collect.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.odk.collect.android.R;

public class SliderHospitalActivity extends CollectAbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_hospital_center);

        // Récupération du bouton
        ImageButton nextButton = findViewById(R.id.next_hospital);

        // Définition de l'action du bouton
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancement de la nouvelle activité
                Intent intent = new Intent(SliderHospitalActivity.this, LandingPageActivity.class);
                startActivity(intent);
            }
        });

        Button skipButton = findViewById(R.id.skip_hospital);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderHospitalActivity.this, LandingPageActivity.class);
                startActivity(intent);
            }
        });
    }
}
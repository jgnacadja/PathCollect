package org.odk.collect.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.odk.collect.android.R;

public class SliderHospitalActivity extends AppCompatActivity {
//    ViewPager viewPager;
//    Button btnNext;
//    int[] layouts;
//    AdapterActivity adapterActivity;
//
//    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
//    protected void onCreate(Bundle savedInstanceState) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        setContentView(R.layout.activity_slider);
//
//        viewPager = findViewById(R.id.pager);
//
//        layouts = new int[] {
//                R.layout.slide_opinion_poll,
//                R.layout.slider_article_reading,
//                R.layout.slider_discussion_forum,
//                R.layout.slider_hospital_center
//        };
//        adapterActivity = new AdapterActivity(this,layouts);
//        viewPager.setAdapter(adapterActivity);
//
//        btnNext = findViewById(R.id.next_enq);
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentPosition = viewPager.getCurrentItem();
//                if (currentPosition == 1) {
//                    startActivity(new Intent(getApplicationContext(), DeleteSavedFormActivity.class));
//                } else if (currentPosition < layouts.length - 1) {
//                    viewPager.setCurrentItem(currentPosition + 1);
//                } else {
//                    startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
//                }
//            }
//        });
//        viewPager.addOnPageChangeListener(viewPagerChangeListener);
//    }
//    ViewPager.OnPageChangeListener viewPagerChangeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int i, float v, int i1) {
//
//        }
//
//        @Override
//        public void onPageSelected(int i) {
//            if(i == 1){
//                btnNext.setText(getString(R.string.continu));
//            } else {
//                btnNext.setText(getString(R.string.next));
//            }
//        }
//
//
//        @Override
//        public void onPageScrollStateChanged(int i) {
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_hospital_center);

        // Récupération du bouton
        ImageButton nextButton = findViewById(R.id.next_hospital);

        // Définition de l'action du bouton
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancement de la nouvelle activité
                Intent intent = new Intent(SliderHospitalActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        Button skipButton = findViewById(R.id.skip_hospital);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderHospitalActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}

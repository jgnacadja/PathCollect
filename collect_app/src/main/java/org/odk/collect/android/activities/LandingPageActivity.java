package org.odk.collect.android.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import org.odk.collect.android.R;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.android.utilities.ApplicationConstants;
import org.odk.collect.android.utilities.ThemeUtils;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;

public class LandingPageActivity extends CollectAbstractActivity{
    //button
    private Button sondageButton;
    private Button messageButton;
    private Button articleButton;
    private Button centreHospitalButton;
    private Button cycleButton;
    private Button notificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ThemeUtils(this).setDarkModeForCurrentProject();

        super.onCreate(savedInstanceState);
        DaggerUtils.getComponent(this).inject(this);
        setContentView(R.layout.landing_page);

        initToolbar();

        // songade button
        sondageButton = findViewById(R.id.sondage);
        sondageButton.setText(getString(R.string.btn_sondage));
        sondageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                i.putExtra(ApplicationConstants.BundleKeys.FORM_MODE,
                        ApplicationConstants.FormModes.EDIT_SAVED);
                startActivity(i);
            }
        });

        // message button
        messageButton = findViewById(R.id.message);
        messageButton.setText(getString(R.string.btn_messsage));
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        MainMenuActivity.class);
                startActivity(i);
            }
        });

        //article Button
        articleButton = findViewById(R.id.article);
        articleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        // centre Hospital Button
        centreHospitalButton = findViewById(R.id.centre_Hospital);
        centreHospitalButton.setText(getString(R.string.btn_centreHospital));
        centreHospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // cycle Button
        cycleButton = findViewById(R.id.cycle);
        cycleButton.setText(getString(R.string.btn_cycle));
        cycleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPeriodical();
            }
        });

        notificationButton = findViewById(R.id.notification);
        notificationButton.setText(getString(R.string.btn_notification));
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        NotificationActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.about_menu_icon).setVisible(true).setEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!MultiClickGuard.allowClick(getClass().getName())) {
            return true;
        }

        if (item.getItemId() == R.id.about_menu_icon) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void openPeriodical() {
        Intent i;
        String packageName = getString(R.string.periodical_app_package_name);
        PackageManager manager = getApplicationContext().getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage(packageName);
            if (i == null){
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            //if not found in device then will come here
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName)));
            } catch (ActivityNotFoundException ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+packageName)));
            }
        }
    }
}
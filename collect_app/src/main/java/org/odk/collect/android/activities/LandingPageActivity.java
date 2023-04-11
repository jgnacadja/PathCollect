package org.odk.collect.android.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
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
                confirmOpenPeriodical();
            }
        });

        notificationButton = findViewById(R.id.notification);
        notificationButton.setText(getString(R.string.btn_notification));
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void confirmOpenPeriodical(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LandingPageActivity.this);
        builder.setTitle("AVERTISSEMENTS");
        builder.setMessage("Pour accéder à la fonctionnalité de gestion de cycle menstruel, vous serez redirigée vers l'application mobile Periodical. Si vous n’avez pas encore l’application Periodical installée, vous serez redirigée vers Google Play Store pour l’installer.\n\nCette application utilise la méthode Knaus-Ogino, une méthode de contraception naturelle, pour déterminer les jours fertiles, mais elle présente un taux d'échec supérieur à 14%. L'abstinence sexuelle est la seule méthode de contraception efficace à 100% pour éviter complètement une grossesse.\n\nBien qu'elle donne une indication sur les jours non fertiles, il n’est pas recommandé d’utiliser cette application pour éviter une grossesse. Les utilisatrices doivent s'adresser aux formations sanitaires habilitées si elles ont besoin de conseils sur l'utilisation de cette application avec des méthodes contraceptives modernes.\n\nLes informations fournies par cette application ne doivent pas être considérées comme des conseils médicaux.\n\nL’objectif principal de cette application est d'aider les femmes à suivre leur cycle et santé menstruel.\n\nAfin d'éviter tout risque de préjudice ou d'erreur de diagnostic, il est fortement recommandé aux utilisatrices de ne pas tenter de s'auto-diagnostiquer ou de s'auto-traiter. En cas de symptômes ou d'irrégularités identifiés, elles doivent consulter des formations sanitaires habilitées.");
        builder.setIcon(R.drawable.notes);
        builder.setPositiveButton("J’ai lu et bien", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action à effectuer lorsque l'utilisateur clique sur le bouton "J'accepte continuer"
                // Par exemple, vous pouvez lancer l'application Periodical ici
                dialog.dismiss();
                openPeriodical();
            }
        });
        builder.setNegativeButton("Annuler", null);
        AlertDialog dialog = builder.create();
        dialog.show();
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
package org.odk.collect.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.ProductListAdapter;
import org.odk.collect.android.adapters.model.Hospital;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;

import java.io.Serializable;
import java.util.List;

public class HospitalActivity extends CollectAbstractActivity {

    private static final String TAG = "HospitalActivity";
    private List<Hospital.Prestation> prestations;
    private RecyclerView recyclerView;
    private ProductListAdapter adapter;
    private TextView name;
    private TextView type;
    private TextView level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_layout);
        DaggerUtils.getComponent(this).inject(this);

        String screenTitle = getIntent().getStringExtra("screenTitle");
        initToolbar(getString(R.string.screen_health_center, screenTitle), false, null);

        Serializable s = getIntent().getSerializableExtra("hospital");
        Hospital hospital = (Hospital) s;

        name = findViewById(R.id.hospitalDetailName);
        name.setText(hospital.getName());
        type = findViewById(R.id.hospitalDetailType);
        type.setText(hospital.getType());
        level = findViewById(R.id.hospitalDetailLevel);
        level.setText(hospital.getLevel());

        prestations = hospital.getPrestations();

        // initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.productRecyclerView);
        adapter = new ProductListAdapter(prestations, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

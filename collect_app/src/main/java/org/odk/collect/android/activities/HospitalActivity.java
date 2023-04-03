package org.odk.collect.android.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.odk.collect.android.R;
import org.odk.collect.android.adapters.ProductListAdapter;
import org.odk.collect.android.adapters.model.Hospital;
import org.odk.collect.android.injection.DaggerUtils;
import java.io.Serializable;
import java.util.List;

public class HospitalActivity extends CollectAbstractActivity {

    private static final String TAG = "HospitalActivity";
    private List<Hospital.Prestation> prestations;
    private RecyclerView recyclerView;
    private ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_layout);
        DaggerUtils.getComponent(this).inject(this);

        String screenTitle = getIntent().getStringExtra("screenTitle");
        initToolbar(getString(R.string.screen_health_center, screenTitle), false, null);

        Serializable s = getIntent().getSerializableExtra("hospital");
        Hospital hospital = (Hospital) s;

        TextView name = findViewById(R.id.hospitalDetailName);
        name.setText(hospital.getName());
        TextView type = findViewById(R.id.hospitalDetailType);
        type.setText(hospital.getType());
        TextView level = findViewById(R.id.hospitalDetailLevel);
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

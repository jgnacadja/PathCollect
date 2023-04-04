package org.odk.collect.android.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.HospitalListAdapter;
import org.odk.collect.android.adapters.ProductListAdapter;
import org.odk.collect.android.adapters.model.Hospital;
import org.odk.collect.android.dao.ApiGatewayService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class HospitalActivity extends AppCompatActivity {

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

        initToolbar();

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

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.collect_app_name));
        setSupportActionBar(toolbar);
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
package org.odk.collect.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.HospitalListAdapter;
import org.odk.collect.android.adapters.model.Hospital;
import org.odk.collect.android.dao.ApiGatewayService;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;

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

public class HospitalListActivity extends CollectAbstractActivity implements
        HospitalListAdapter.HospitalItemClickListener{

    private static final String TAG = "HospitalListActivity";
    private List<Hospital> hospitals;
    private RecyclerView recyclerView;
    private HospitalListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_list_layout);

        initToolbar(getString(R.string.screen_health_center_list), false, null);
        hospitals = new ArrayList<>();

        // initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.hospitalRecyclerView);
        adapter = new HospitalListAdapter(hospitals, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_gateway_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create API service instance
        ApiGatewayService apiService = retrofit.create(ApiGatewayService.class);

        // create JSON request body
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{}");

        // call API to get data
        Call<List<Hospital>> call = apiService.searchHospitals(requestBody);
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                if (response.isSuccessful()) {
                    hospitals.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Timber.tag(TAG).e("Response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                Timber.tag(TAG).e(t);
            }
        });
    }

    @Override
    public void onClick(int position) {
        if (MultiClickGuard.allowClick(getClass().getName())) {
            Hospital hospital = hospitals.get(position);
            Intent intent = new Intent(this, HospitalActivity.class);
            intent.putExtra("screenTitle", hospital.getLevel() + " " +hospital.getName());
            intent.putExtra("hospital", hospital);
            startActivity(intent);
        }
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

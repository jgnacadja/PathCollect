package org.odk.collect.android.activities;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.odk.collect.android.R;
import org.odk.collect.android.adapters.HospitalListAdapter;
import org.odk.collect.android.adapters.model.Hospital;
import org.odk.collect.android.utilities.ExternalWebPageHelper;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;
import java.util.ArrayList;
import java.util.List;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import timber.log.Timber;

public class HospitalListActivity extends AppCompatActivity implements
        HospitalListAdapter.HospitalItemClickListener{
    private List<Hospital> hospitals;
    private RecyclerView recyclerView;
    private HospitalListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_layout);

        initToolbar();
        hospitals = new ArrayList<>();
        Hospital hospital = new Hospital();
        String name = "zogbo";
        hospital.setName(name.toUpperCase());
        hospital.setLevel(Hospital.HospitalType.PRIVATE);
        hospital.setType(Hospital.CareLevel.CABINET_PRIVEE);
        hospitals.add(hospital);

        // initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.hospitalRecyclerView);
        adapter = new HospitalListAdapter(hospitals, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // create Retrofit instance
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(getString(R.string.api_url))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

//        // create API service instance
//        WpApiService apiService = retrofit.create(WpApiService.class);

//        // call API to get data
//        Call<List<Hospital>> call = apiService.getPosts("publish");
//        call.enqueue(new Callback<List<Hospital>>() {
//            @Override
//            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
//                if (response.isSuccessful()) {
//                    Hospital.addAll(response.body());
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Timber.tag("HospitalListActivity").e("Response not successful");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Hospital>> call, Throwable t) {
//                Timber.tag("HospitalListActivity").e(t);
//            }
//        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.collect_app_name));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(int position) {
//        if (MultiClickGuard.allowClick(getClass().getName())) {
//            Hospital hospital = hospitals.get(position);
//            Intent intent = new Intent(this, WebViewActivity.class);
//            intent.putExtra(ExternalWebPageHelper.OPEN_URL, hospitals.getLink());
//            startActivity(intent);
//        }
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

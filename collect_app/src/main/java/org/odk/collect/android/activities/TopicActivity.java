package org.odk.collect.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.odk.collect.android.R;
import org.odk.collect.android.adapters.TopicListAdapter;
import org.odk.collect.android.adapters.model.Topic;
import org.odk.collect.android.dao.TopicDao;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.androidshared.system.IntentLauncher;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;


public class TopicActivity extends CollectAbstractActivity implements
        TopicListAdapter.TopicItemClickListener {

    @Inject
    IntentLauncher intentLauncher;
    private TopicListAdapter adapter;
    private List<Topic> topics;
    private static final String TAG = "TopicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_layout);
        DaggerUtils.getComponent(this).inject(this);

        initToolbar();
        ProgressBar progressBar = findViewById(R.id.topicProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        TextView tv = findViewById(R.id.topicFetchError);

        // Get a reference to the "topics" node in Firebase
        TopicDao dao = new TopicDao();
        topics = new ArrayList<Topic>();

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.topicRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicListAdapter(topics, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Get topics
        dao.getAll(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topics = new ArrayList<Topic>();

                for (DataSnapshot topicSnapshot : snapshot.getChildren()) {
                    Topic topic = topicSnapshot.getValue(Topic.class);
                    if (topic != null) {
                        topics.add(topic);
                    }
                }
                adapter.setTopics(topics);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                Timber.tag(TAG).e(error.toException(), "Error reading topics");
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.collect_app_name));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(int position) {
        if (MultiClickGuard.allowClick(getClass().getName())) {
            Topic topic = topics.get(position);
            Intent intent = new Intent(this, DiscussionListActivity.class);
            intent.putExtra("topicId", topic.getId());
            startActivity(intent);
        }
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

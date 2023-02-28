package org.odk.collect.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.DiscussionListAdapter;
import org.odk.collect.android.adapters.model.Discussion;
import org.odk.collect.android.dao.DiscussionDao;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class DiscussionListActivity extends CollectAbstractActivity implements
        DiscussionListAdapter.DiscussionItemClickListener {

    private static final String TAG = "DiscussionListActivity";
    private DiscussionListAdapter adapter;
    private List<Discussion> discussions;
    private String topicId;
    private DiscussionDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_list_layout);
        DaggerUtils.getComponent(this).inject(this);

        initToolbar();
        ProgressBar progressBar = findViewById(R.id.discussionListProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        TextView tv = findViewById(R.id.discussionListFetchError);

        // Get a reference to the "discussions" node in Firebase
        dao = new DiscussionDao();

        discussions = new ArrayList<Discussion>();
        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.discussionListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiscussionListAdapter(discussions, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Get discussions
        topicId = getIntent().getStringExtra("topicId");
        dao.getDiscussionsByTopicId(topicId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                discussions = new ArrayList<Discussion>();

                for (DataSnapshot discussionSnapshot : snapshot.getChildren()) {
                    Discussion discussion = discussionSnapshot.getValue(Discussion.class);
                    if (discussion != null) {
                        discussions.add(discussion);
                    }
                }
                adapter.setDiscussions(discussions);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                Timber.tag(TAG).e(error.toException(), "Error reading discussions");
            }
        });

        ImageButton addDiscussionButton = findViewById(R.id.add_discussion);
        addDiscussionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToCreateDiscussion();
            }
        });
    }

    public void redirectToCreateDiscussion() {
        Intent intent = new Intent(this, AddDiscussionActivity.class);
        intent.putExtra("topicId", topicId);
        startActivity(intent);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.collect_app_name));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(int position) {
        if (MultiClickGuard.allowClick(getClass().getName())) {
            Discussion discussion = discussions.get(position);
            // Update the views count in the Discussion object
            discussion.incrementViews();

            // Update the views count in the database
            dao.updateDiscussionViewsCount(discussion);

            Intent intent = new Intent(this, DiscussionActivity.class);
            intent.putExtra("discussionId", discussion.getId());
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

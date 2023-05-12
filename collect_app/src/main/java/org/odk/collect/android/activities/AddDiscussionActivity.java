package org.odk.collect.android.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Discussion;
import org.odk.collect.android.dao.DiscussionDao;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;


public class AddDiscussionActivity extends CollectAbstractActivity {

    private TextInputEditText questionLabelTV;
    private MaterialTextView submitButton;
    private TextView errorMessage;
    private ProgressBar pgsBar;
    private String topicId;
    private DiscussionDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discussion_layout);
        DaggerUtils.getComponent(this).inject(this);

        initToolbar(getString(R.string.screen_add_question), false, null);
        topicId = getIntent().getStringExtra("topicId");
        // Get a reference to the "discussions" node in Firebase
        dao = new DiscussionDao();

        errorMessage = findViewById(R.id.discussion_creation_error);
        errorMessage.setVisibility(View.GONE);
        pgsBar = findViewById(R.id.discussionpBar);
        questionLabelTV = findViewById(R.id.question_label);
        submitButton = findViewById(R.id.submit_discussion);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDiscussion();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void createDiscussion() {
        pgsBar.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
        errorMessage.setText("");
        String title = questionLabelTV.getText().toString();

        if (TextUtils.isEmpty(title)) {
            pgsBar.setVisibility(View.GONE);
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Veuillez entrer le titre de la question");
        } else {
            Discussion discussion = new Discussion(
                    getString(R.string.anon_user),
                    title,
                    topicId
            );

            dao.addDiscussion(discussion);
            Intent intent = new Intent(this, DiscussionListActivity.class);
            intent.putExtra("topicId", topicId);
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
}

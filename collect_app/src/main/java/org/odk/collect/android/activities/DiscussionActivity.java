package org.odk.collect.android.activities;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.CommentListAdapter;
import org.odk.collect.android.adapters.model.Comment;
import org.odk.collect.android.adapters.model.Discussion;
import org.odk.collect.android.dao.CommentDao;
import org.odk.collect.android.dao.DiscussionDao;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.android.utilities.TimeAgo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class DiscussionActivity extends CollectAbstractActivity {

    private static final String TAG = "DiscussionActivity";
    private CommentListAdapter adapter;
    private Discussion discussion;
    private List<Comment> comments;
    private DiscussionDao discussionDao;
    private CommentDao commentDao;
    private TextView titleTextView;
    private ImageView authorIconImageView;
    private TextView authorNameTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;
    private ImageButton discussionLikeBtn;
    private TextView errorMessage;
    private EditText commentText;
    private ImageButton commentBtn;
    private ProgressBar pgsBar;
    private boolean isDiscussionRead;
    private boolean isCommentsRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_layout);
        DaggerUtils.getComponent(this).inject(this);

        initToolbar();
        initComponent();
        ProgressBar progressBar = findViewById(R.id.discussionProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        TextView tv = findViewById(R.id.discussionFetchError);
        isDiscussionRead = false;
        isCommentsRead = false;

        // Get a reference to the "discussions" node in Firebase
        discussionDao = new DiscussionDao();

        // Get a reference to the "comments" node in Firebase
        commentDao = new CommentDao();

        comments = new ArrayList<Comment>();

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.commentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentListAdapter(comments, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Get discussion
        String discussionId = getIntent().getStringExtra("discussionId");
        DatabaseReference discussionRef = FirebaseDatabase.getInstance().getReference().child("discussions").child(discussionId);

        // Attach a listener to the discussion reference to get updates
        discussionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the Discussion object from the database
                discussion = snapshot.getValue(Discussion.class);

                // Init display with discussion data
                titleTextView.setText(discussion.getTitle());
                new DownloadImageFile().execute(discussion.getIcon());
                descriptionTextView.setText(discussion.getDescription());
                dateTextView.setText(TimeAgo.formatTimestamp(discussion.getTimestamp()));
                authorNameTextView.setText(discussion.getAuthor());

                // Init listeners
                discussionLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Update the likes count in the Discussion object
                        discussion.incrementLikes();

                        // Update the likes count in the database
                        discussionDao.updateDiscussionLikesCount(discussion);
                    }
                });

                commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createComment();
                    }
                });
                isDiscussionRead = true;
                progressBar.setVisibility(getVisibility(isDiscussionRead, isCommentsRead));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                isDiscussionRead = false;
                Timber.tag(TAG).e(error.toException(), "Error reading the discussion");
            }
        });

        commentDao.getCommentsByDiscussionId(discussionId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments = new ArrayList<Comment>();

                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    if (discussion != null) {
                        comments.add(comment);
                    }
                }
                adapter.setComments(comments);
                isCommentsRead = true;
                progressBar.setVisibility(getVisibility(isDiscussionRead, isCommentsRead));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                isCommentsRead = false;
                Timber.tag(TAG).e(error.toException(), "Error reading comments");
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.collect_app_name));
        setSupportActionBar(toolbar);
    }

    private int getVisibility(boolean isDiscussionRead, boolean isCommentsRead) {
        if (isCommentsRead && isDiscussionRead) {
            return View.GONE;
        } else {
            return View.VISIBLE;
        }
    }

    private void initComponent() {
        errorMessage = findViewById(R.id.comment_creation_error);
        errorMessage.setVisibility(View.GONE);
        pgsBar = findViewById(R.id.pBar);
        titleTextView = findViewById(R.id.discussion_title);
        authorIconImageView = findViewById(R.id.author_icon);
        descriptionTextView = findViewById(R.id.discussion_description);
        dateTextView = findViewById(R.id.discussion_publication_date);
        authorNameTextView = findViewById(R.id.author_name);
        discussionLikeBtn = findViewById(R.id.discussion_like_icon);
        commentText = findViewById(R.id.message_input_text);
        commentBtn = findViewById(R.id.send_button);
    }

    private void createComment() {
        pgsBar.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
        errorMessage.setText("");
        String content = commentText.getText().toString();

        if (TextUtils.isEmpty(content)) {
            pgsBar.setVisibility(View.GONE);
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Veuillez saisir un commentaire !");
        } else {
            Comment comment = new Comment();
            comment.setDiscussionId(discussion.getId());
            comment.setText(content);
            comment.setLikes(0);
            comment.setAuthor(getString(R.string.anon_user));
            comment.setIcon(null);

            commentDao.addComment(comment);
            commentText.setText("");
        }

        pgsBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class DownloadImageFile extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... urls) {
            String url = urls[0];
            if (url != null && !url.isEmpty()) {
                try {
                    URL urlConnection = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) urlConnection
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    return Drawable.createFromStream(input, "src name");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (drawable != null) {
                authorIconImageView.setImageDrawable(drawable);
            } else {
                int iconId = R.drawable.ic_anonymous_user;
                authorIconImageView.setImageResource(iconId);
                authorIconImageView.setTag(iconId);
            }
        }

    }
}

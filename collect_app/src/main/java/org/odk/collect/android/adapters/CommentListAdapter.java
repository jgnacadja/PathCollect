package org.odk.collect.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Comment;
import org.odk.collect.android.dao.CommentDao;
import org.odk.collect.android.tasks.DownloadImageTask;
import org.odk.collect.android.utilities.TimeAgo;
import org.odk.collect.android.views.ForumViewHolder;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private final Context context;
    private final String installID;
    private List<Comment> comments;

    public CommentListAdapter(List<Comment> comments, Context context, String installID) {
        this.context = context;
        this.comments = comments;
        this.installID = installID;
    }

    @NonNull
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.comment_layout, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.ViewHolder holder, int position) {
        // Get a reference to the "discussions" node in Firebase
        CommentDao dao = new CommentDao();

        Comment comment = comments.get(position);

        // Check if the user has already liked this comment
        boolean alreadyLiked;
        try {
            alreadyLiked = comment.getLikedUsers().contains(installID);
        } catch (NullPointerException e) {
            alreadyLiked = false;
        }

        DownloadImageTask task = new DownloadImageTask(holder);
        task.execute(comment.getIcon());
        holder.author.setText(comment.getAuthor());
        holder.content.setText(comment.getText());
        holder.date.setText(TimeAgo.formatTimestamp(comment.getTimestamp()));
        holder.likes.setText(String.valueOf(comment.getLikes()));

        if (alreadyLiked) {
            int iconId = R.drawable.thumb_up_filled;
            holder.likeBtn.setImageResource(iconId);
            holder.likeBtn.setTag(iconId);
        }

        boolean finalAlreadyLiked = alreadyLiked;
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalAlreadyLiked) {
                    // Update the likes count in the Comment object
                    comment.incrementLikes();
                    holder.likes.setText(String.valueOf(comment.getLikes()));

                    // Update the likes count in the database
                    dao.updateCommentLikesCount(comment, installID, true);
                    int iconId = R.drawable.thumb_up_filled;
                    holder.likeBtn.setImageResource(iconId);
                    holder.likeBtn.setTag(iconId);
                } else {
                    // Update the likes count in the Comment object
                    comment.decrementLikes();
                    holder.likes.setText(String.valueOf(comment.getLikes()));

                    // Update the likes count in the database
                    dao.updateCommentLikesCount(comment, installID, false);
                    int iconId = R.drawable.thumbs_up;
                    holder.likeBtn.setImageResource(iconId);
                    holder.likeBtn.setTag(iconId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }


    public interface CommentItemClickListener {
        void onClick(int position);
    }

    class ViewHolder extends ForumViewHolder {
        private final TextView author;
        private final TextView date;
        private final TextView content;
        private final TextView likes;
        private final ImageButton likeBtn;

        ViewHolder(View view) {
            super(view, R.id.comment_author_image);
            author = view.findViewById(R.id.comment_author_name);
            content = view.findViewById(R.id.comment_content);
            likes = view.findViewById(R.id.comment_like_count);
            date = view.findViewById(R.id.comment_publication_date);
            likeBtn = view.findViewById(R.id.comment_like_icon);
        }
    }
}

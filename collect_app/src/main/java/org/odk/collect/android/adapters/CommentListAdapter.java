/*
 * Copyright (C) 2018 Shobhit Agarwal
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
import org.odk.collect.android.utilities.DownloadImageTask;
import org.odk.collect.android.utilities.TimeAgo;
import org.odk.collect.android.views.ForumViewHolder;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private final Context context;
    private List<Comment> comments;

    public CommentListAdapter(List<Comment> comments, Context context) {
        this.context = context;
        this.comments = comments;
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
        DownloadImageTask task = new DownloadImageTask(holder);
        task.execute(comment.getIcon());
        holder.author.setText(comment.getAuthor());
        holder.content.setText(comment.getText());
        holder.date.setText(TimeAgo.formatTimestamp(comment.getTimestamp()));
        holder.likes.setText(String.valueOf(comment.getLikes()));

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the likes count in the Discussion object
                comment.incrementLikes();
                holder.likes.setText(String.valueOf(comment.getLikes()));

                // Update the likes count in the database
                dao.updateCommentLikesCount(comment);
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

    class ViewHolder extends ForumViewHolder implements View.OnClickListener {
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
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //
        }
    }
}

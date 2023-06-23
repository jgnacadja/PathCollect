package org.odk.collect.android.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Post;
import org.odk.collect.android.tasks.DownloadPostImageTask;
import org.odk.collect.android.utilities.TextUtils;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private final Context context;
    private List<Post> posts;
    private final PostItemClickListener listener;

    public PostListAdapter(List<Post> posts, Context context, PostItemClickListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.post_list_item, parent, false);
        return new ViewHolder(itemLayoutView, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = posts.get(position);
        if(post.getMedia() != null){
            String image = post.getMedia();
            DownloadPostImageTask task = new DownloadPostImageTask(holder);
            task.execute(image);
        } else {
            int iconId = R.drawable.ic_outline_website_24;
            holder.imageView.setImageResource(iconId);
            holder.imageView.setTag(iconId);
        }
        holder.title.setText(post.getTitle());
        holder.preview.setText(TextUtils.formatHtmlText(post.getSummary()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface PostItemClickListener {
        void onClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final PostItemClickListener listener;
        private final ImageView imageView;
        private final TextView title;
        private final TextView preview;

        ViewHolder(View view, PostItemClickListener listener) {
            super(view);
            this.listener = listener;
            imageView = view.findViewById(R.id.postImageView);
            title = view.findViewById(R.id.postTitle);
            preview = view.findViewById(R.id.postPreview);
            view.setOnClickListener(this);
        }

        public void setIconDrawable(Drawable drawable){
            if(drawable != null){
                this.imageView.setImageDrawable(drawable);
            } else {
                int iconId = R.drawable.ic_outline_website_24;
                this.imageView.setImageResource(iconId);
                this.imageView.setTag(iconId);
            }
        }
        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }
}

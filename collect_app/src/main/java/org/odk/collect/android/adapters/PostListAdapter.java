package org.odk.collect.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private final Context context;
    private List<Post> articles;
    private final ArticleItemClickListener listener;

    public PostListAdapter(List<Post> articles, Context context, ArticleItemClickListener listener) {
        this.context = context;
        this.articles = articles;
        this.listener = listener;
    }

    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.article_list_item, parent, false);
        return new ViewHolder(itemLayoutView, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post article = articles.get(position);
        if(article.get_links().getWpFeaturedMedia() != null){
            String image = article.get_links().getWpFeaturedMedia().get(0).getHref();
            DownloadPostImageTask task = new DownloadPostImageTask(holder);
            task.execute(image);
        } else {
            int iconId = R.drawable.ic_outline_website_24;
            holder.imageView.setImageResource(iconId);
            holder.imageView.setTag(iconId);
        }
        holder.title.setText(article.getTitle().getRendered());
        String plainString = Html.fromHtml(article.getExcerpt().getRendered()).toString();
        holder.preview.setText(plainString.substring(0, Math.min(120, plainString.length())) + "...");
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public interface ArticleItemClickListener {
        void onClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ArticleItemClickListener listener;
        private final ImageView imageView;
        private final TextView title;
        private final TextView preview;

        ViewHolder(View view, ArticleItemClickListener listener) {
            super(view);
            this.listener = listener;
            imageView = view.findViewById(R.id.articleImageView);
            title = view.findViewById(R.id.articleTitle);
            preview = view.findViewById(R.id.articlePreview);
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

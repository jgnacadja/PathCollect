package org.odk.collect.android.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Article;
import org.odk.collect.android.tasks.DownloadArticleImageTask;

import java.util.List;

import timber.log.Timber;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    private final Context context;
    private List<Article> articles;
    private final ArticleItemClickListener listener;

    public ArticleListAdapter(List<Article> articles, Context context, ArticleItemClickListener listener) {
        this.context = context;
        this.articles = articles;
        this.listener = listener;
    }

    @Override
    public ArticleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.article_list_item, parent, false);
        return new ViewHolder(itemLayoutView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = articles.get(position);
        if(article.get_links().getWp_featuredmedia() != null){
            String image = article.get_links().getWp_featuredmedia().get(0).getHref();
            DownloadArticleImageTask task = new DownloadArticleImageTask(holder);
            task.execute(image);
        } else {
            int iconId = R.drawable.ic_outline_website_24;
            holder.imageView.setImageResource(iconId);
            holder.imageView.setTag(iconId);
        }
        holder.title.setText(article.getTitle().getRendered());
        String plainString = Html.fromHtml(article.getExcerpt().getRendered()).toString();
        holder.preview.setText(plainString);
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

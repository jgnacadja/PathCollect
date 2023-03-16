package org.odk.collect.android.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Article;
import org.odk.collect.android.tasks.DownloadArticleImageTask;

import java.util.List;

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
//        String image = article.get_links().getWp_featuredmedia().get(0).getHref();
//        DownloadArticleImageTask task = new DownloadArticleImageTask(holder);
//        task.execute(image);
        int iconId = R.drawable.bubbles;
        holder.imageView.setImageResource(iconId);
        holder.imageView.setTag(iconId);
        holder.title.setText(article.getTitle().getRendered());
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

        ViewHolder(View view, ArticleItemClickListener listener) {
            super(view);
            this.listener = listener;
            imageView = view.findViewById(R.id.articleimageView);
            title = view.findViewById(R.id.articletitle);
            view.setOnClickListener(this);
        }

        public void setIconDrawable(Drawable drawable){
            if(drawable != null){
                this.imageView.setImageDrawable(drawable);
            } else {
                int iconId = R.drawable.bubbles;
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

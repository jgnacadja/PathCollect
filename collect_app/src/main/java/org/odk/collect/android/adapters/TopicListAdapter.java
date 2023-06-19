package org.odk.collect.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Topic;
import org.odk.collect.android.tasks.DownloadImageTask;
import org.odk.collect.android.views.ForumViewHolder;

import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private final Context context;
    private final TopicItemClickListener listener;
    private List<Topic> topics;

    public TopicListAdapter(List<Topic> topics, Context context, TopicItemClickListener listener) {
        this.context = context;
        this.topics = topics;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.topic_item_layout, parent, false);
        return new ViewHolder(itemLayoutView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicListAdapter.ViewHolder holder, int position) {
        Topic topic = topics.get(position);
        DownloadImageTask task = new DownloadImageTask(holder);
        task.execute(topic.getIcon());
        holder.title.setText(topic.getTitle());
        holder.discussionCount.setText(this.context.getString(R.string.questions_count, topic.getDiscussionCount()));
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }


    public interface TopicItemClickListener {
        void onClick(int position);
    }

    class ViewHolder extends ForumViewHolder implements View.OnClickListener {
        private final TopicItemClickListener listener;
        private final TextView title;
        private final TextView discussionCount;

        ViewHolder(View view, TopicItemClickListener listener) {
            super(view, R.id.topic_image);
            this.listener = listener;
            title = view.findViewById(R.id.topic_title);
            discussionCount = view.findViewById(R.id.discussions_count);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }

    }
}

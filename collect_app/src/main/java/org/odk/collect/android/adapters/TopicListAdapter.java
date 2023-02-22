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
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Topic;
import org.odk.collect.android.utilities.DownloadImageUtils;

import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private final Context context;
    private final List<Topic> topics;
    private final TopicItemClickListener listener;

    public TopicListAdapter(List<Topic> topics, Context context, TopicItemClickListener listener) {
        this.context = context;
        this.topics = topics;
        this.listener = listener;
    }

    @Override
    public TopicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.topic_item_layout, parent, false);
        return new ViewHolder(itemLayoutView, listener);
    }

    @Override
    public void onBindViewHolder(TopicListAdapter.ViewHolder holder, int position) {
        Topic topic = topics.get(position);
        Drawable drawable = DownloadImageUtils.loadImageFromUrl(topic.getIcon());
        if(drawable != null){
            holder.imageView.setImageDrawable(drawable);
        } else {
            int iconId = R.drawable.form_state_blank_circle;
            holder.imageView.setImageResource(iconId);
            holder.imageView.setTag(iconId);
        }
        holder.title.setText(topic.getTitle());
        holder.discussionCount.setText("325000");
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public interface TopicItemClickListener {
        void onClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TopicItemClickListener listener;
        private final ImageView imageView;
        private final TextView title;
        private final TextView discussionCount;

        ViewHolder(View view, TopicItemClickListener listener) {
            super(view);
            this.listener = listener;
            imageView = view.findViewById(R.id.topic_image);
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

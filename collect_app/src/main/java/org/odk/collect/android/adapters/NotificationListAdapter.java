package org.odk.collect.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Notification;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private final Context context;
    private final List<Notification> items;

    public NotificationListAdapter(List<Notification> items, Context context) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context).inflate(R.layout.notification_item_layout, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = items.get(position);
        holder.notificationTitle.setText(notification.getTitle());
        holder.notificationSubtitle.setText(notification.getBody());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView notificationTitle;
        private final TextView notificationSubtitle;

        ViewHolder(View view) {
            super(view);
            notificationTitle = view.findViewById(R.id.notificationTitle);
            notificationSubtitle = view.findViewById(R.id.notificationSubtitle);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }
}
package org.odk.collect.android.views;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;

public abstract class ForumViewHolder extends RecyclerView.ViewHolder {
    private final ImageView imageView;

    public ForumViewHolder(@NonNull View view, int viewId) {
        super(view);
        this.imageView = view.findViewById(viewId);
    }

    public void setIconDrawable(Drawable drawable) {
        if (drawable != null) {
            this.imageView.setImageDrawable(drawable);
        } else {
            int iconId = R.drawable.ic_anonymous_user;
            this.imageView.setImageResource(iconId);
            this.imageView.setTag(iconId);
        }
    }
}

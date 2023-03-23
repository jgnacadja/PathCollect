package org.odk.collect.android.utilities;

import androidx.work.Data;

import org.odk.collect.android.adapters.model.Notification;

public class NotificationTypeConverter {

    // Convert a Notification object into a Data object
    public static Data notificationToData(Notification notification) {
        return new Data.Builder()
                .putLong("dbId", notification.getDbId())
                .putString("title", notification.getTitle())
                .putString("body", notification.getBody())
                .putLong("timestamp", notification.getTimestamp())
                .build();
    }

    // Convert a Data object into a Notification object
    public static Notification dataToNotification(Data data) {
        return new Notification(
                data.getLong("dbId", 0),
                data.getString("title"),
                data.getString("body"),
                data.getLong("timestamp", 0)
        );
    }
}

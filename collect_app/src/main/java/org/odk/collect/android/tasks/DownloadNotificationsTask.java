package org.odk.collect.android.tasks;

import android.os.AsyncTask;

import org.odk.collect.android.adapters.model.Notification;
import org.odk.collect.android.database.notification.DatabaseNotificationRepository;
import org.odk.collect.android.formmanagement.NotificationDownloader;

public class DownloadNotificationsTask extends
        AsyncTask<Notification, Void, Notification> {

    private final NotificationDownloader notificationDownloader;
    private final DatabaseNotificationRepository repository;

    public DownloadNotificationsTask(NotificationDownloader notificationDownloader, DatabaseNotificationRepository repository) {
        this.notificationDownloader = notificationDownloader;
        this.repository = repository;
    }

    @Override
    protected Notification doInBackground(Notification... values) {
//        this.checkOrCreateDb();
        Notification n = values[0];
        // Save the notification to the local database using a DAO
        return repository.save(n);
    }

//    private void checkOrCreateDb(){
//        if (!repository.doesDatabaseExist()) {
//            repository.createDatabase();
//        }
//    }
}


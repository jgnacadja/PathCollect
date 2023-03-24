package org.odk.collect.android.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.odk.collect.android.adapters.model.Notification;
import org.odk.collect.android.database.notification.DatabaseNotificationRepository;
import org.odk.collect.android.formmanagement.NotificationDownloader;

import timber.log.Timber;

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
        Timber.tag("DownloadNotifTask").i(new Gson().toJson(values));
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


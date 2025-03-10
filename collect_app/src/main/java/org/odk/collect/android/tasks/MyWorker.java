package org.odk.collect.android.tasks;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import org.odk.collect.android.adapters.model.Notification;
import org.odk.collect.android.database.notification.DatabaseNotificationRepository;
import org.odk.collect.android.storage.StoragePathProvider;
import org.odk.collect.android.storage.StorageSubdirectory;

import timber.log.Timber;

public class MyWorker extends Worker {

    private static final String TAG = "MyWorker";

    private DatabaseNotificationRepository repository;

    public MyWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        String dbPath = new StoragePathProvider().getOdkDirPath(StorageSubdirectory.METADATA, null);
        repository = new DatabaseNotificationRepository(getApplicationContext(), dbPath);

    }

    @SuppressLint("WrongThread")
    @NonNull
    @Override
    public Result doWork() {
        Timber.tag(TAG).d("Performing long running task in scheduled job");
        Timber.tag(TAG).d(new Gson().toJson(getInputData()));

        // Get the notification data from the input data
        String title = getInputData().getString("title");
        String body = getInputData().getString("body");

        // Create a new notification object from the data
        Notification notification = new Notification(title, body, System.currentTimeMillis());

//         Save the notification to the local database using a DAO
        Timber.tag(TAG).i("Saving notification to local database using DAO");
        repository.save(notification);

        return Result.success();
    }
}

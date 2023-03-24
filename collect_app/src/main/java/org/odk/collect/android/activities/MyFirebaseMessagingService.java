package org.odk.collect.android.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import org.odk.collect.android.R;
import org.odk.collect.android.database.notification.DatabaseNotificationRepository;
import org.odk.collect.android.storage.StoragePathProvider;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private StoragePathProvider storagePathProvider = new StoragePathProvider();
    private DatabaseNotificationRepository repository;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.tag(TAG).d("From: %s", remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Timber.tag(TAG).d("Message data payload: %s", remoteMessage.getData());
            scheduleJob(remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Timber.tag(TAG).d("Message Notification Body: %s", remoteMessage.getNotification().getBody());
            String notificationBody = remoteMessage.getNotification().getBody();
            if (remoteMessage.getNotification().getBody() != null) {
                sendNotification(notificationBody);
            }
        }

    }
    @Override
    public void onNewToken(String token) {
        Timber.tag(TAG).d("Refreshed token: %s", token);

        sendRegistrationToServer(token);
    }
    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob(Map<String, String> data) {
        Data inputData = new Data.Builder()
                .putAll(new HashMap<>(data))
                .build();
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(inputData)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Timber.tag(TAG).d("Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, OldNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

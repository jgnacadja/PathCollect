package org.odk.collect.android.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.odk.collect.android.R;
import org.odk.collect.android.tasks.MyWorker;
import org.odk.collect.android.activities.NotificationActivity;
import org.odk.collect.android.tasks.SendRequestToWP;

import timber.log.Timber;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

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
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String notificationTitle = remoteMessage.getNotification().getTitle();
            String notificationBody = remoteMessage.getNotification().getBody();

            Timber.tag(TAG).d("Message Notification Title: %s", notificationTitle);
            Timber.tag(TAG).d("Message Notification Body: %s", notificationBody);

            if (notificationTitle != null && notificationBody != null) {
                sendNotification(notificationTitle, notificationBody);
                saveNotification(notificationTitle.trim(), notificationBody.trim());
            }
        }
    }

    @Override
    public void onNewToken(String token) {
        Timber.tag(TAG).d("Refreshed token: %s", token);

        sendRegistrationToServer(token);
    }

    /**
     * Async work for saving received notification using WorkManager.
     */
    private void saveNotification(String title, String body) {
        Data inputData = new Data.Builder()
                .putString("title", title)
                .putString("body", body)
                .build();
        Timber.tag(TAG).d(new Gson().toJson(inputData));
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(inputData)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String apiKey = getString(R.string.fcm_api_key);
        String subscription = getString(R.string.wp_fcm_subscription);
        String url = String.format(
                getString(R.string.wp_fcm_api),
                apiKey,
                androidId,
                token,
                subscription
        );

        new SendRequestToWP().execute(url);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notes)
                        .setContentTitle(getString(R.string.fcm_message) + " : " + messageTitle)
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

package org.odk.collect.android.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Notification;
import org.odk.collect.android.database.notification.DatabaseNotificationRepository;
import org.odk.collect.android.databinding.ActivityMainBinding;
import org.odk.collect.android.storage.StoragePathProvider;
import org.odk.collect.android.storage.StorageSubdirectory;
import org.odk.collect.android.tasks.DownloadNotificationsTask;

import java.util.List;

import timber.log.Timber;

public class OldNotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private static final String POST_NOTIFICATIONS = "POST_NOTIFICATIONS";

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this, "FCM can't post notifications without POST_NOTIFICATIONS permission",
                            Toast.LENGTH_LONG).show();
                }
            });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        StoragePathProvider storagePathProvider = new StoragePathProvider();
        String dbPath = storagePathProvider.getOdkDirPath(StorageSubdirectory.METADATA, null);
        DatabaseNotificationRepository repository = new DatabaseNotificationRepository(this, dbPath);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Timber.tag(TAG).d("Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        binding.subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.tag(TAG).d("Subscribing to weather topic");
                // [START subscribe_topics]
//                FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                String msg = getString(R.string.msg_subscribed);
//                                if (!task.isSuccessful()) {
//                                    msg = getString(R.string.msg_subscribe_failed);
//                                }
//                                Timber.tag(TAG).d(msg);
//                                Toast.makeText(NotificationActivity.this, msg, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                // [END subscribe_topics]
                Notification notif = new Notification("Test notification", "Corps notification", 10054789);
                DownloadNotificationsTask task = new DownloadNotificationsTask(null, repository);
                task.execute(notif);
            }
        });

        binding.logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                // [START log_reg_token]
//                FirebaseMessaging.getInstance().getToken()
//                        .addOnCompleteListener(new OnCompleteListener<String>() {
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void onComplete(@NonNull Task<String> task) {
//                                if (!task.isSuccessful()) {
//                                    Timber.tag(TAG).w(task.getException(), "Fetching FCM registration token failed");
//                                    return;
//                                }
//
//                                // Get new FCM registration token
//                                String token = task.getResult();
//                                String android_id = Settings.Secure.getString(getContentResolver(),
//                                        Settings.Secure.ANDROID_ID);
//
//                                // Log and toast
//                                String msg = "Token = "+getString(R.string.msg_token_fmt, token);
//                                Timber.tag(TAG).d(msg);
//
//                                String msgDId = "Device id = " + android_id;
//                                Timber.tag(TAG).d(msgDId);
//
//                                binding.showInformations.setText(msg+"\n"+msgDId);
//
//                                String api_key = "2894p49788.s6s350o0o770q62q-o208ppq997ss2n4q949-242ps9s1150o51s3599r3s433q";
//                                String subscription = "DSSC";
//                                String url = String.format(
//                                        "https://dssc-cms.000webhostapp.com/wp-json/fcm/pn/subscribe?rest_api_key=%s&device_uuid=%s&device_token=%s&subscription=%s",
//                                        api_key,
//                                        android_id,
//                                        token,
//                                        subscription
//                                );
//
//                                new SubscribeToWP().execute(url);
//                            }
//                        });
                // [END log_reg_token]

                List<Notification> notifications = repository.getAll();
                Timber.tag(TAG).i("%s", notifications.size());
            }
        });

        askNotificationPermission();
    }

    private void askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(POST_NOTIFICATIONS);
            }
        }
    }
}
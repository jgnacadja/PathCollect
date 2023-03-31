package org.odk.collect.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import org.odk.collect.android.tasks.SendRequestToWP;

import timber.log.Timber;

public class UninstallReceiver extends BroadcastReceiver {
    private static final String TAG = "UninstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            onUninstall(context);
        }
        Timber.tag(TAG).d("Uninstall Sucess");

    }
    public void onUninstall(Context context) {
        // Perform any necessary cleanup or data-saving operations here
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String apiKey = context.getString(R.string.fcm_api_key);
        String url = String.format(
                context.getString(R.string.wp_fcm_api_unsubscribe),
                apiKey,
                androidId
        );

        new SendRequestToWP().execute(url);
    }
}

package org.odk.collect.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.NotificationListAdapter;
import org.odk.collect.android.adapters.model.Notification;
import org.odk.collect.android.database.notification.DatabaseNotificationRepository;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.android.storage.StoragePathProvider;
import org.odk.collect.android.storage.StorageSubdirectory;
import org.odk.collect.androidshared.system.IntentLauncher;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;

import java.util.List;

import javax.inject.Inject;

public class NotificationActivity extends CollectAbstractActivity {

    @Inject
    IntentLauncher intentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        DaggerUtils.getComponent(this).inject(this);

        initToolbar(getString(R.string.screen_notification_list), false, null);

        StoragePathProvider storagePathProvider = new StoragePathProvider();
        String dbPath = storagePathProvider.getOdkDirPath(StorageSubdirectory.METADATA, null);
        DatabaseNotificationRepository repository = new DatabaseNotificationRepository(this, dbPath);

        List<Notification> items = repository.getAll();

        RecyclerView recyclerView = findViewById(R.id.notificationRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new NotificationListAdapter(items, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.about_menu_icon).setVisible(true).setEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!MultiClickGuard.allowClick(getClass().getName())) {
            return true;
        }

        if (item.getItemId() == R.id.about_menu_icon) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

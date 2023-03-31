package org.odk.collect.android.activities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
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

        initToolbar();

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

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.show_notifications));
        setSupportActionBar(toolbar);
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

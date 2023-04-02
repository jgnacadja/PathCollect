package org.odk.collect.android.database.notification;

import static android.provider.BaseColumns._ID;
import static org.odk.collect.android.database.DatabaseConstants.NOTIFICATIONS_TABLE_NAME;
import static org.odk.collect.android.database.DatabaseObjectMapper.getNotificationFromCurrentCursorPosition;
import static org.odk.collect.android.database.DatabaseObjectMapper.getValuesFromNotification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import org.jetbrains.annotations.NotNull;
import org.odk.collect.android.adapters.model.Notification;
import org.odk.collect.android.database.DatabaseConnection;
import org.odk.collect.android.database.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DatabaseNotificationRepository {

    private static DatabaseConnection databaseConnection;

    public DatabaseNotificationRepository(Context context, String dbPath) {
        this.databaseConnection = new DatabaseConnection(
                context,
                dbPath,
                DatabaseConstants.NOTIFICATIONS_DATABASE_NAME,
                new NotificationDatabaseMigrator(),
                DatabaseConstants.NOTIFICATIONS_DATABASE_VERSION
        );
    }

    private static List<Notification> getNotificationFromCursor(Cursor cursor) {
        List<Notification> notifications = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                Notification notification = getNotificationFromCurrentCursorPosition(cursor);

                notifications.add(notification);
            }
        }
        return notifications;
    }

    public Notification get(Long id) {
        return queryForOneNotification(_ID + "=?", new String[]{id.toString()});
    }

    public List<Notification> getAll() {
        return queryForNotification(null, null);
    }

    public Notification save(@NotNull Notification notification) {
        final ContentValues values = getValuesFromNotification(notification);
        Long idFromUri = insertNotification(values);
        return get(idFromUri);
    }

    public Cursor rawQuery(Map<String, String> projectionMap, String[] projection, String selection, String[] selectionArgs, String sortOrder, String groupBy) {
        return queryAndReturnCursor(projectionMap, projection, selection, selectionArgs, sortOrder, groupBy);
    }

    private Notification queryForOneNotification(String selection, String[] selectionArgs) {
        List<Notification> notifications = queryForNotification(selection, selectionArgs);
        return !notifications.isEmpty() ? notifications.get(0) : null;
    }

    private static List<Notification> queryForNotification(String selection, String[] selectionArgs) {
        try (Cursor cursor = queryAndReturnCursor(null, null, selection, selectionArgs, null, null)) {
            return getNotificationFromCursor(cursor);
        }
    }

    private static Cursor queryAndReturnCursor(Map<String, String> projectionMap, String[] projection, String selection, String[] selectionArgs, String sortOrder, String groupBy) {
        SQLiteDatabase readableDatabase = databaseConnection.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(NOTIFICATIONS_TABLE_NAME);

        if (projectionMap != null) {
            qb.setProjectionMap(projectionMap);
        }

        return qb.query(readableDatabase, projection, selection, selectionArgs, groupBy, null, sortOrder);
    }

    private Long insertNotification(ContentValues values) {
        SQLiteDatabase writeableDatabase = databaseConnection.getWriteableDatabase();
        return writeableDatabase.insertOrThrow(NOTIFICATIONS_TABLE_NAME, null, values);
    }

    private void updateNotification(Long id, ContentValues values) {
        SQLiteDatabase writeableDatabase = databaseConnection.getWriteableDatabase();
        writeableDatabase.update(NOTIFICATIONS_TABLE_NAME, values, _ID + "=?", new String[]{String.valueOf(id)});
    }
}

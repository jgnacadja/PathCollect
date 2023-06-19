package org.odk.collect.android.database.notification;

import static android.provider.BaseColumns._ID;
import static org.odk.collect.android.database.DatabaseConstants.NOTIFICATIONS_TABLE_NAME;
import static org.odk.collect.android.database.notification.DatabaseNotificationColumns.BODY;
import static org.odk.collect.android.database.notification.DatabaseNotificationColumns.TIMESTAMP;
import static org.odk.collect.android.database.notification.DatabaseNotificationColumns.TITLE;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.odk.collect.android.database.DatabaseMigrator;
import org.odk.collect.android.utilities.SQLiteUtils;

public class NotificationDatabaseMigrator implements DatabaseMigrator {

    private static final String[] COLUMN_NAMES = {_ID, TITLE,
            BODY, TIMESTAMP};

    private static final String TEMP_NOTIFICATION_TABLE_NAME = "notifications_v4";
    private static final String MODEL_VERSION = "modelVersion";

    public void onCreate(SQLiteDatabase db) {
        createNotificationsTableV11(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion) throws SQLException {
        switch (oldVersion) {
            case 1:
                upgradeToVersion2(db);
            case 2:
            case 3:
                upgradeToVersion4(db, oldVersion);
            case 4:
                upgradeToVersion5(db);
            case 5:
                upgradeToVersion6(db);
            case 6:
                upgradeToVersion7(db);
            case 7:
                upgradeToVersion8(db);
            case 8:
                upgradeToVersion9(db);
            case 9:
                upgradeToVersion10(db);
            case 10:
                upgradeToVersion11(db);
        }
    }

    public void onDowngrade(SQLiteDatabase db) throws SQLException {
        SQLiteUtils.dropTable(db, NOTIFICATIONS_TABLE_NAME);
        createNotificationsTableV11(db);
    }

    private void upgradeToVersion2(SQLiteDatabase db) {
        SQLiteUtils.dropTable(db, NOTIFICATIONS_TABLE_NAME);
        onCreate(db);
    }

    private void upgradeToVersion4(SQLiteDatabase db, int oldVersion) {
        // adding BASE64_RSA_PUBLIC_KEY and changing type and name of
        // integer MODEL_VERSION to text VERSION
        SQLiteUtils.dropTable(db, TEMP_NOTIFICATION_TABLE_NAME);
        createNotificationsTableV4(db, TEMP_NOTIFICATION_TABLE_NAME);
        db.execSQL("INSERT INTO "
                + TEMP_NOTIFICATION_TABLE_NAME
                + " ("
                + _ID
                + ", "
                + TITLE
                + ", "
                + BODY
                + ", "
                + TIMESTAMP
                + ") SELECT "
                + _ID
                + ", "
                + TITLE
                + ", "
                + BODY
                + ", "
                + "CASE WHEN "
                + MODEL_VERSION
                + " IS NOT NULL THEN "
                + "CAST("
                + MODEL_VERSION
                + " AS TEXT) ELSE NULL END, "
                + TIMESTAMP + " FROM "
                + NOTIFICATIONS_TABLE_NAME);

        SQLiteUtils.dropTable(db, NOTIFICATIONS_TABLE_NAME);
        createNotificationsTableV4(db, NOTIFICATIONS_TABLE_NAME);
        db.execSQL("INSERT INTO "
                + NOTIFICATIONS_TABLE_NAME
                + " ("
                + _ID
                + ", "
                + TITLE
                + ", "
                + BODY
                + ", "
                + TIMESTAMP + ") SELECT "
                + _ID + ", "
                + TITLE
                + ", "
                + BODY
                + ", "
                + TIMESTAMP
                + ", " // milliseconds
                + NOTIFICATIONS_TABLE_NAME);
        SQLiteUtils.dropTable(db, NOTIFICATIONS_TABLE_NAME);
    }

    private void upgradeToVersion5(SQLiteDatabase db) {
        SQLiteUtils.addColumn(db, NOTIFICATIONS_TABLE_NAME, "lastDetectedFormVersionHash", "text");
        SQLiteUtils.addColumn(db, NOTIFICATIONS_TABLE_NAME, "lastFormVersionHash", "text");
    }

    private void upgradeToVersion6(SQLiteDatabase db) {
        SQLiteUtils.addColumn(db, NOTIFICATIONS_TABLE_NAME, "lastDetectedVersionHash", "text");
    }

    private void upgradeToVersion7(SQLiteDatabase db) {
        String temporaryTable = NOTIFICATIONS_TABLE_NAME + "_ntn";
        SQLiteUtils.renameTable(db, NOTIFICATIONS_TABLE_NAME, temporaryTable);
        createNotificationsTableV7(db);
        SQLiteUtils.copyRows(db, temporaryTable, COLUMN_NAMES, NOTIFICATIONS_TABLE_NAME);
        SQLiteUtils.dropTable(db, temporaryTable);
    }

    private void upgradeToVersion8(SQLiteDatabase db) {
        SQLiteUtils.addColumn(db, NOTIFICATIONS_TABLE_NAME, "lastDetectedFormHash", "text");
    }

    private void upgradeToVersion9(SQLiteDatabase db) {
        String temporaryTable = NOTIFICATIONS_TABLE_NAME + "_ntn";
        SQLiteUtils.renameTable(db, NOTIFICATIONS_TABLE_NAME, temporaryTable);
        createNotificationsTableV9(db);
        SQLiteUtils.copyRows(db, temporaryTable, new String[]{_ID, TITLE, BODY,
                TIMESTAMP}, NOTIFICATIONS_TABLE_NAME);
        SQLiteUtils.dropTable(db, temporaryTable);
    }

    private void upgradeToVersion10(SQLiteDatabase db) {
        String temporaryTable = NOTIFICATIONS_TABLE_NAME + "_ntn";
        SQLiteUtils.renameTable(db, NOTIFICATIONS_TABLE_NAME, temporaryTable);
        createNotificationsTableV10(db);
        SQLiteUtils.copyRows(db, temporaryTable, new String[]{_ID, TITLE, BODY,
                TIMESTAMP}, NOTIFICATIONS_TABLE_NAME);
        SQLiteUtils.dropTable(db, temporaryTable);
    }

    private void upgradeToVersion11(SQLiteDatabase db) {
        String temporaryTable = NOTIFICATIONS_TABLE_NAME + "_ntn";
        SQLiteUtils.renameTable(db, NOTIFICATIONS_TABLE_NAME, temporaryTable);
        createNotificationsTableV11(db);
        SQLiteUtils.copyRows(db, temporaryTable, new String[]{_ID, TITLE, BODY,
                TIMESTAMP}, NOTIFICATIONS_TABLE_NAME);
        SQLiteUtils.dropTable(db, temporaryTable);
    }

    private void createNotificationsTableV4(SQLiteDatabase db, String tableName) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + _ID + " integer primary key, "
                + TITLE + " text not null, "
                + BODY + " text not null, "
                + TIMESTAMP + " integer, "
                + "lastDetectedFormVersionHash" + " text);");
    }

    private void createNotificationsTableV7(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NOTIFICATIONS_TABLE_NAME + " ("
                + _ID + " integer primary key, "
                + TITLE + " text not null, "
                + BODY + " text not null, "
                + TIMESTAMP + " integer);");
    }

    private void createNotificationsTableV9(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NOTIFICATIONS_TABLE_NAME + " ("
                + _ID + " integer primary key, "
                + TITLE + " text not null, "
                + BODY + " text, "
                + TIMESTAMP + " integer, "
                + "deleted" + " boolean default(0));");
    }

    private void createNotificationsTableV10(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NOTIFICATIONS_TABLE_NAME + " ("
                + _ID + " integer primary key, "
                + TITLE + " text not null, "
                + BODY + " text, "
                + TIMESTAMP + " integer);");
    }

    private void createNotificationsTableV11(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NOTIFICATIONS_TABLE_NAME + " ("
                + _ID + " integer primary key, "
                + TITLE + " text not null, "
                + BODY + " text, "
                + TIMESTAMP + " integer);");
    }
}

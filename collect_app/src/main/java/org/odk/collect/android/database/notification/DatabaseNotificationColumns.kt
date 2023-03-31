package org.odk.collect.android.database.notification

import android.provider.BaseColumns

/**
 * Columns for the Notifications table.
 */
object DatabaseNotificationColumns : BaseColumns {
    // These are the only things needed for an insert
    const val DB_ID = "dbId"
    const val TITLE = "title"
    const val BODY = "body" // can be null
    const val TIMESTAMP = "timestamp"

}

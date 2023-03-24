package org.odk.collect.android.adapters.model;

import org.jetbrains.annotations.NotNull;

public class Notification {
    private Long dbId;
    private String title;
    private String body;
    private long timestamp;

    public Notification(Long dbId, String title, String body, long timestamp) {
        this.dbId = dbId;
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public Notification(String title, String body, long timestamp) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public Notification(Notification.Builder builder) {
        dbId = builder.dbId;
        title = builder.title;
        body = builder.body;
        timestamp = builder.timestamp;

    }
    public static class Builder {
        private Long dbId;
        private String title;
        private String body;
        private Long timestamp;

        public Builder() {
        }

        public Builder(@NotNull Notification notification) {
            dbId = notification.dbId;
            title = notification.title;
            body = notification.body;
            timestamp = notification.timestamp;
        }
        public Notification.Builder dbId(Long id) {
            this.dbId = id;
            return this;
        }
        public Notification.Builder title(String title) {
            this.title = title;
            return this;
        }
        public Notification.Builder body(String body) {
            this.body = body;
            return this;
        }
        public Notification.Builder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        public Notification build() {
            return new Notification(this);
        }

    }

    public Long getDbId() {
        return dbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "dbId=" + dbId +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }


}

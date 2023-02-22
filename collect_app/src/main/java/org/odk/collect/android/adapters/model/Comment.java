package org.odk.collect.android.adapters.model;

public class Comment {
    private String id;
    private String text;
    private long timestamp;
    private String discussionId;

    public Comment(){

    }

    public Comment(String id, String text, long timestamp, String discussionId){
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.discussionId = discussionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(String discussionId) {
        this.discussionId = discussionId;
    }
}

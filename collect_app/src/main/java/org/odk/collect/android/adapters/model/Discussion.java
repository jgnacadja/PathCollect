package org.odk.collect.android.adapters.model;

public class Discussion {
    private String id;
    private String title;
    private long timestamp;
    private String topicId;

    public Discussion(){

    }

    public Discussion(String id, String title, long timestamp, String topicId){
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.topicId = topicId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}

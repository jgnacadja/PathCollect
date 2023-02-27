package org.odk.collect.android.adapters.model;

public class Comment {
    private String id;
    private String icon;
    private String author;
    private String text;
    private long timestamp;
    private String discussionId;
    private int likes;

    public Comment(){

    }

    public Comment(String id, String icon, String author, String text, long timestamp, String discussionId, int likes){
        this.id = id;
        this.icon = icon;
        this.author = author;
        this.text = text;
        this.timestamp = timestamp;
        this.discussionId = discussionId;
        this.likes = likes;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void incrementLikes() {
        likes++;
    }

    public void decrementLikes() {
        likes--;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

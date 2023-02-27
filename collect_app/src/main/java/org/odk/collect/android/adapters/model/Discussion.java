package org.odk.collect.android.adapters.model;

public class Discussion {
    private String id;
    private String icon;
    private String author;
    private String title;
    private String description;
    private long timestamp;
    private String topicId;
    private int likes;
    private int views;
    private int commentCount;
    private long lastCommentTimestamp;

    public Discussion() {

    }

    public Discussion(String id, String icon, String author, String title, String description, long timestamp, String topicId, int views, int likes, int commentCount, long lastCommentTimestamp) {
        this.id = id;
        this.icon = icon;
        this.author = author;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.topicId = topicId;
        this.views = views;
        this.likes = likes;
        this.commentCount = commentCount;
        this.lastCommentTimestamp = lastCommentTimestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getLastCommentTimestamp() {
        return lastCommentTimestamp;
    }

    public void setLastCommentTimestamp(long lastCommentTimestamp) {
        this.lastCommentTimestamp = lastCommentTimestamp;
    }

    public void incrementViews() {
        views++;
    }

    public void incrementLikes() {
        likes++;
    }

    public void decrementLikes() {
        likes--;
    }

    public void incrementCommentCount() {
        commentCount++;
    }

    public void decrementCommentCount() {
        commentCount--;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

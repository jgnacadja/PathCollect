package org.odk.collect.android.adapters.model;

import java.util.List;

public class Discussion {
    private String id;
    private String icon;
    private String author;
    private String title;
    private long timestamp;
    private String topicId;
    private int likes;
    private int views;
    private int commentCount;
    private long lastCommentTimestamp;
    private List<String> likedUsers;
    private List<String> viewedUsers;

    public Discussion() {

    }

    public Discussion(String id, String icon, String author, String title, long timestamp, String topicId, int views, int likes, int commentCount, long lastCommentTimestamp, List<String> likedUsers, List<String> viewedUsers) {
        this.id = id;
        this.icon = icon;
        this.author = author;
        this.title = title;
        this.timestamp = timestamp;
        this.topicId = topicId;
        this.views = views;
        this.likes = likes;
        this.commentCount = commentCount;
        this.lastCommentTimestamp = lastCommentTimestamp;
        this.likedUsers = likedUsers;
        this.viewedUsers = viewedUsers;
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

    public List<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<String> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public List<String> getViewedUsers() {
        return viewedUsers;
    }

    public void setViewedUsers(List<String> viewedUsers) {
        this.viewedUsers = viewedUsers;
    }
}

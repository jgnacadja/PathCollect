package org.odk.collect.android.adapters.model;

import java.util.List;

public class Comment {
    private String id;
    private String icon;
    private String author;
    private String text;
    private long timestamp;
    private String discussionId;
    private int likes;
    private List<String> likedUsers;
    private List<String> viewedUsers;

    public Comment() {

    }

    public Comment(String id, String icon, String author, String text, long timestamp, String discussionId, int likes, List<String> likedUsers, List<String> viewedUsers) {
        this.id = id;
        this.icon = icon;
        this.author = author;
        this.text = text;
        this.timestamp = timestamp;
        this.discussionId = discussionId;
        this.likes = likes;
        this.likedUsers = likedUsers;
        this.viewedUsers = viewedUsers;
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

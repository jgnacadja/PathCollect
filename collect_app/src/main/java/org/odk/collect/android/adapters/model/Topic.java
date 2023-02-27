package org.odk.collect.android.adapters.model;

public class Topic {
    private String id;
    private String title;
    private String icon;
    private int discussionCount;

    public Topic(){

    }

    public Topic(String id, String title, String icon, int discussionCount){
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.discussionCount = discussionCount;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getDiscussionCount(){
        return discussionCount;
    }

    public void setDiscussionCount(int discussionCount) {
        this.discussionCount = discussionCount;
    }

    public void incrementDiscussionCount() {
        discussionCount++;
    }

    public void decrementDiscussionCount() {
        discussionCount--;
    }
}

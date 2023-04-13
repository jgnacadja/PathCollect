package org.odk.collect.android.adapters.model;

import androidx.annotation.Keep;

@Keep
public class Post {
    private int id;
    private String date;
    private String link;
    private String title;
    private String summary;
    private String media;

    public Post() {
    }

    public Post(int id, String date, String link, String title, String summary, String media) {
        this.id = id;
        this.date = date;
        this.link = link;
        this.title = title;
        this.summary = summary;
        this.media = media;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}

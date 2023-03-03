package org.odk.collect.android.adapters.model;

import org.odk.collect.android.widgets.TriggerWidget;

import java.util.List;

public class Article {
    private int id;
    private String date;
    private String link;
    private String title;
    private String  content;
    private String  contentPreview;
    private String  author;
    private String  image;
    private List<String>  categories;

    public Article(int id, String date, String link, String title, String content, String contentPreview, String author, String image, List<String> categories) {
        this.id = id;
        this.date = date;
        this.link = link;
        this.title = title;
        this.content = content;
        this.contentPreview = contentPreview;
        this.author = author;
        this.categories = categories;
        this.image = image;
    }

    public Article() {

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentPreview() {
        return contentPreview;
    }

    public void setContentPreview(String contentPreview) {
        this.contentPreview = contentPreview;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

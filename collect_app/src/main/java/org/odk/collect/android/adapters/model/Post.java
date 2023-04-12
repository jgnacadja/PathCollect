package org.odk.collect.android.adapters.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class Post {
    @SerializedName("id")
    private int id;
    @SerializedName("date_gmt")
    private String dateGmt;
    @SerializedName("modified_gmt")
    private String modifiedGmt;
    @SerializedName("link")
    private String link;
    @SerializedName("title")
    private Title title;
    @SerializedName("content")
    private Content content;
    @SerializedName("excerpt")
    private Content excerpt;
    @SerializedName("categories")
    private List<Integer> categories;
    @SerializedName("tags")
    private List<Integer> tags;
    @SerializedName("_links")
    private Links _links;

    public Post(int id, String dateGmt, String modifiedGmt, String link, Title title, Content content, Content excerpt, List<Integer> categories, List<Integer> tags, Links _links) {
        this.id = id;
        this.dateGmt = dateGmt;
        this.modifiedGmt = modifiedGmt;
        this.link = link;
        this.title = title;
        this.content = content;
        this.excerpt = excerpt;
        this.categories = categories;
        this.tags = tags;
        this._links = _links;
    }

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateGmt() {
        return dateGmt;
    }

    public void setDateGmt(String dateGmt) {
        this.dateGmt = dateGmt;
    }

    public String getModifiedGmt() {
        return modifiedGmt;
    }

    public void setModifiedGmt(String modifiedGmt) {
        this.modifiedGmt = modifiedGmt;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(Content excerpt) {
        this.excerpt = excerpt;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }

    @Keep
    public static class Title {
        @SerializedName("rendered")
        private String rendered;

        public Title(String rendered) {
            this.rendered = rendered;
        }

        public Title() {
        }

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }
    }

    @Keep
    public static class Content {
        @SerializedName("rendered")
        private String rendered;
        @SerializedName("protected")
        private boolean _protected;

        public Content(String rendered, boolean _protected) {
            this.rendered = rendered;
            this._protected = _protected;
        }

        public Content() {
        }

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        public boolean is_protected() {
            return _protected;
        }

        public void set_protected(boolean _protected) {
            this._protected = _protected;
        }
    }

    @Keep
    public static class Links {
        @SerializedName("author")
        private List<Feature> author;
        @SerializedName("wp:attachment")
        private List<WpAttachment> wpAttachment;
        @SerializedName("wp:featuredmedia")
        private List<Feature> wpFeaturedMedia;

        @Keep
        public static class Feature {
            @SerializedName("embeddable")
            private boolean embeddable;
            @SerializedName("href")
            private String href;

            public Feature(boolean embeddable, String href) {
                this.embeddable = embeddable;
                this.href = href;
            }

            public Feature() {
            }

            public boolean isEmbeddable() {
                return embeddable;
            }

            public void setEmbeddable(boolean embeddable) {
                this.embeddable = embeddable;
            }

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }

        @Keep
        public static class WpAttachment {
            @SerializedName("href")
            private String href;

            public WpAttachment(String href) {
                this.href = href;
            }

            public WpAttachment() {
            }

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }

        public Links() {
        }

        public Links(List<Feature> author, List<WpAttachment> wpAttachment, List<Feature> wpFeaturedMedia) {
            this.author = author;
            this.wpAttachment = wpAttachment;
            this.wpFeaturedMedia = wpFeaturedMedia;
        }

        public List<Feature> getAuthor() {
            return author;
        }

        public void setAuthor(List<Feature> author) {
            this.author = author;
        }

        public List<WpAttachment> getWpAttachment() {
            return wpAttachment;
        }

        public void setWpAttachment(List<WpAttachment> wpAttachment) {
            this.wpAttachment = wpAttachment;
        }

        public List<Feature> getWpFeaturedMedia() {
            return wpFeaturedMedia;
        }

        public void setWpFeaturedMedia(List<Feature> wpFeaturedMedia) {
            this.wpFeaturedMedia = wpFeaturedMedia;
        }
    }
}

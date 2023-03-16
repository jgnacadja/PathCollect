package org.odk.collect.android.adapters.model;

import java.util.List;

public class Article {
    private int id;
    private String date_gmt;
    private String modified_gmt;
    private String link;
    private Title title;
    private Content content;
    private Content excerpt;
    private List<Integer> categories;
    private List<Integer> tags;
    private Links _links;

    public Article(int id, String date_gmt, String modified_gmt, String link, Title title, Content content, Content excerpt, List<Integer> categories, List<Integer> tags, Links _links) {
        this.id = id;
        this.date_gmt = date_gmt;
        this.modified_gmt = modified_gmt;
        this.link = link;
        this.title = title;
        this.content = content;
        this.excerpt = excerpt;
        this.categories = categories;
        this.tags = tags;
        this._links = _links;
    }

    public Article() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_gmt() {
        return date_gmt;
    }

    public void setDate_gmt(String date_gmt) {
        this.date_gmt = date_gmt;
    }

    public String getModified_gmt() {
        return modified_gmt;
    }

    public void setModified_gmt(String modified_gmt) {
        this.modified_gmt = modified_gmt;
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

    public static class Title {
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

    public static class Content {
        private String rendered;
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

    public static class Links {
        private List<Feature> author;
        private List<WpAttachment> wp_attachment;
        private List<Feature> wp_featuredmedia;
        private List<WpTerm> wp_term;

        public static class Feature {
            private boolean embeddable;
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

        public static class WpAttachment {
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

        public static class WpTerm {
            private String taxonomy;
            private boolean embeddable;
            private String href;

            public WpTerm(String taxonomy, boolean embeddable, String href) {
                this.taxonomy = taxonomy;
                this.embeddable = embeddable;
                this.href = href;
            }

            public WpTerm() {
            }

            public String getTaxonomy() {
                return taxonomy;
            }

            public void setTaxonomy(String taxonomy) {
                this.taxonomy = taxonomy;
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

        public Links(List<Feature> author, List<WpAttachment> wp_attachment, List<Feature> wp_featuredmedia, List<WpTerm> wp_term) {
            this.author = author;
            this.wp_attachment = wp_attachment;
            this.wp_featuredmedia = wp_featuredmedia;
            this.wp_term = wp_term;
        }

        public Links() {
        }

        public List<Feature> getAuthor() {
            return author;
        }

        public void setAuthor(List<Feature> author) {
            this.author = author;
        }

        public List<WpAttachment> getWp_attachment() {
            return wp_attachment;
        }

        public void setWp_attachment(List<WpAttachment> wp_attachment) {
            this.wp_attachment = wp_attachment;
        }

        public List<Feature> getWp_featuredmedia() {
            return wp_featuredmedia;
        }

        public void setWp_featuredmedia(List<Feature> wp_featuredmedia) {
            this.wp_featuredmedia = wp_featuredmedia;
        }

        public List<WpTerm> getWp_term() {
            return wp_term;
        }

        public void setWp_term(List<WpTerm> wp_term) {
            this.wp_term = wp_term;
        }
    }
}

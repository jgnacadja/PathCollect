package org.odk.collect.android.adapters.model;

import com.google.gson.annotations.SerializedName;

public class ArticleImage {

    @SerializedName("id")
    private int id;

    @SerializedName("source_url")
    private String sourceUrl;

    @SerializedName("media_details")
    private MediaDetails mediaDetails;

    public ArticleImage(Integer id, MediaDetails mediaDetails, String sourceUrl) {
        this.id = id;
        this.mediaDetails = mediaDetails;
        this.sourceUrl = sourceUrl;
    }

    public ArticleImage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MediaDetails getMediaDetails() {
        return mediaDetails;
    }

    public void setMediaDetails(MediaDetails mediaDetails) {
        this.mediaDetails = mediaDetails;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public static class MediaDetails {

        @SerializedName("sizes")
        private Sizes sizes;

        public MediaDetails(Sizes sizes) {
            this.sizes = sizes;
        }

        public MediaDetails() {
        }

        public Sizes getSizes() {
            return sizes;
        }

        public void setSizes(Sizes sizes) {
            this.sizes = sizes;
        }

        public static class Sizes {

            @SerializedName("medium")
            private Shaped medium;
            @SerializedName("thumbnail")
            private Shaped thumbnail;
            @SerializedName("shapely-featured")
            private Shaped shapelyFeatured;
            @SerializedName("shapely-grid")
            private Shaped shapelyGrid;
            @SerializedName("full")
            private Shaped full;

            public Sizes() {
            }

            public Sizes(Shaped medium, Shaped thumbnail, Shaped shapelyFeatured, Shaped shapelyGrid, Shaped full) {
                this.medium = medium;
                this.thumbnail = thumbnail;
                this.shapelyFeatured = shapelyFeatured;
                this.shapelyGrid = shapelyGrid;
                this.full = full;
            }

            public Shaped getMedium() {
                return medium;
            }

            public void setMedium(Shaped medium) {
                this.medium = medium;
            }

            public Shaped getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(Shaped thumbnail) {
                this.thumbnail = thumbnail;
            }

            public Shaped getShapelyFeatured() {
                return shapelyFeatured;
            }

            public void setShapelyFeatured(Shaped shapelyFeatured) {
                this.shapelyFeatured = shapelyFeatured;
            }

            public Shaped getShapelyGrid() {
                return shapelyGrid;
            }

            public void setShapelyGrid(Shaped shapelyGrid) {
                this.shapelyGrid = shapelyGrid;
            }

            public Shaped getFull() {
                return full;
            }

            public void setFull(Shaped full) {
                this.full = full;
            }

            public static class Shaped {
                @SerializedName("source_url")
                private String sourceUrl;

                public Shaped() {
                }

                public Shaped(String sourceUrl) {
                    this.sourceUrl = sourceUrl;
                }

                public String getSourceUrl() {
                    return sourceUrl;
                }

                public void setSourceUrl(String sourceUrl) {
                    this.sourceUrl = sourceUrl;
                }
            }
        }
    }

}
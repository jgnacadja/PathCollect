package org.odk.collect.android.adapters.model;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;

@Keep
public class Hospital implements Serializable {
    List<Prestation> prestations;
    private String id;
    private String name;
    private Double longitude;
    private Double latitude;
    private String openingTime;
    private String closingTime;
    private List<String> phones;
    private List<String> mails;
    private String type;
    private String level;

    public Hospital() {
    }

    public Hospital(String id, String name, Double longitude, Double latitude, String openingTime, String closingTime, List<String> phones, List<String> mails, String type, String level, List<Prestation> prestations) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.phones = phones;
        this.mails = mails;
        this.type = type;
        this.level = level;
        this.prestations = prestations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getMails() {
        return mails;
    }

    public void setMails(List<String> mails) {
        this.mails = mails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Prestation> getPrestations() {
        return prestations;
    }

    public void setPrestations(List<Prestation> prestations) {
        this.prestations = prestations;
    }

    @Keep
    public static class Prestation implements Serializable {
        private String id;
        private String name;
        private String type;
        private Double price;
        private Boolean available;

        public Prestation(String id, String name, String type, Double price, Boolean available) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.price = price;
            this.available = available;
        }

        public Prestation() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Boolean getAvailable() {
            return available;
        }

        public void setAvailable(Boolean available) {
            this.available = available;
        }
    }
}

package org.odk.collect.android.adapters.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Hospital {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("openingTime")
    private String openingTime;
    @SerializedName("closingTime")
    private String closingTime;
    @SerializedName("phones")
    private List<String> phones;
    @SerializedName("mails")
    private List<String> mails;
    private CareLevel type;
    private HospitalType level;
    @SerializedName("prestations")
    List<Prestation> prestations;

    public Hospital(String id, String name, double longitude, double latitude, String openingTime, String closingTime, List<String> phones, List<String> mails, CareLevel type, HospitalType level, List<Prestation> prestations) {
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

    public Hospital() {
    }

    public CareLevel getType() {
        return type;
    }

    public void setType(CareLevel type) {
        this.type = type;
    }

    public HospitalType getLevel() {
        return level;
    }

    public void setLevel(HospitalType level) {
        this.level = level;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
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

    public List<Prestation> getPrestations() {
        return prestations;
    }

    public void setPrestations(List<Prestation> prestations) {
        this.prestations = prestations;
    }

    public static enum CareLevel {
        CSPS,
        CMA,
        DISPENSAIRE,
        CLINIQUE,
        CENTRE_COMMUNAUTAIRE,
        CABINET_PRIVEE
    }

    public static enum HospitalType {
        PRIVATE,
        PUBLIC
    }


    public static class Prestation {
        private String id;
        private String name;
        private PrestationType type;
        private double price;
        private boolean available;

        public static enum PrestationType {
            SERVICE,
            PRODUCT
        }

        public Prestation(String id, String name, PrestationType type, double price, boolean available) {
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

        public PrestationType getType() {
            return type;
        }

        public void setType(PrestationType type) {
            this.type = type;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }
    }
}

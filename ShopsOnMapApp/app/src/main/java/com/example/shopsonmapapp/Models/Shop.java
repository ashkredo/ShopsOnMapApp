package com.example.shopsonmapapp.Models;

public class Shop {
    private long ID;
    private String name;
    private double longitude;
    private double latitude;

    public Shop() { }

    public Shop(String name, double latitude, double longitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Shop(int ID, String name, double latitude, double longitude) {
        this.ID = ID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public long getID() {
        return ID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

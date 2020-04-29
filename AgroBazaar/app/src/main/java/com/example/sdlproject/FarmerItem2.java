package com.example.sdlproject;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class FarmerItem2 {

    private String name;

    private String category;
    private String price;
    private String date;
    private String time;
    private String uid;
    private String qty;
    private String id;
    private String description;
    private String fertilizer;
    private String user_name;
    private String address;
    private String phone;
    private String upi;
    private double latitude, longitude, dist;

    public FarmerItem2(String name, String category, String price, String date, String time, String uid, String qty, String id, String description, String fertilizer, String user_name, String address, double latitude, double longitude, String phone, double dist, String upi) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = date;
        this.time = time;
        this.uid = uid;
        this.qty = qty;
        this.id = id;
        this.description = description;
        this.fertilizer = fertilizer;
        this.user_name = user_name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dist = dist;
        this.phone = phone;
        this.upi = upi;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(String fertilizer) {
        this.fertilizer = fertilizer;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }
}

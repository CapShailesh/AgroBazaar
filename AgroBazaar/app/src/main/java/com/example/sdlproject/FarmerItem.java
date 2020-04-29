package com.example.sdlproject;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class FarmerItem {

    private String name, category, price, date, time, uid, qty, id, description, fertilizer, upi;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FarmerItem(String name, String category, String price, String date, String time, String uid, String qty, String id, String description, String fertilizer, String upi) {
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

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }
}

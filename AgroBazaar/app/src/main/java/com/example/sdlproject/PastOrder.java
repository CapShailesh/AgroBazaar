package com.example.sdlproject;

public class PastOrder{

    String name, price, date, uid, qty, approval;

    public PastOrder(String name, String price, String date, String uid, String qty, String approval) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.uid = uid;
        this.qty = qty;
        this.approval = approval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }
}
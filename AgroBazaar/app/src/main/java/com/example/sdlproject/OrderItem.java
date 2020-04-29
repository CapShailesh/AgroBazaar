package com.example.sdlproject;

public class OrderItem {

    private String name, category, date, quantity, price ,id;

    public OrderItem(String name, String category, String date, String quantity, String price, String id) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.id  = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.example.sdlproject;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Products {

    String fertilizer, price, quantity, description, uid;
    String productRandomKey, saveCurrentDate, saveCurrentTime;
    String prod_category, prod_name;
    String UPI;

    public Products(String fertilizer, String price, String quantity, String description, String uid, String prod_name, String prod_category, String UPI) {
        this.fertilizer = fertilizer;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.uid = uid;
        this.prod_category = prod_category;
        this.prod_name = prod_name;
        this.UPI = UPI;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProductRandomKey() {
        return productRandomKey;
    }

    public void setProductRandomKey(String productRandomKey) {
        this.productRandomKey = productRandomKey;
    }

    public String getSaveCurrentDate() {
        return saveCurrentDate;
    }

    public void setSaveCurrentDate(String saveCurrentDate) {
        this.saveCurrentDate = saveCurrentDate;
    }

    public String getSaveCurrentTime() {
        return saveCurrentTime;
    }

    public void setSaveCurrentTime(String saveCurrentTime) {
        this.saveCurrentTime = saveCurrentTime;
    }

    public String getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(String fertilizer) {
        this.fertilizer = fertilizer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProd_category() {
        return prod_category;
    }

    public void setProd_category(String prod_category) {
        this.prod_category = prod_category;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getUPI() {
        return UPI;
    }

    public void setUPI(String UPI) {
        this.UPI = UPI;
    }
}

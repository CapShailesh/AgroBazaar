package com.example.sdlproject;

public class users {

    private String email;
    private String type;
    private String name;
    private String phone;
    private String address;
    private String state;
    private String city;
    private String pincode;
    private double latitude;
    private double longitude;

    public users()
    {

    }

    public users(String email, String type, String name, String phone, String address, String state, String city, String pincode, double latitude, double longitude) {
        this.email = email;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }


    public void setLongitude(double longitude){
        this.latitude = longitude;
    }
}


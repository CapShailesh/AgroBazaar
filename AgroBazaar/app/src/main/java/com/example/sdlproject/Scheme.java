package com.example.sdlproject;

public class Scheme {

    private String schemeName, link;

    public Scheme() {
    }

    public Scheme(String scheme_name, String link) {
        schemeName = scheme_name;
        this.link = link;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public String getLink() {
        return link;
    }

    public void setSchemeName(String scheme_name) {
        schemeName = scheme_name;
    }

    public void setCategory(String link) {
        this.link = link;
    }
}

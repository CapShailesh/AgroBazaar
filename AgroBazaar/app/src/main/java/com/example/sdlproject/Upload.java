package com.example.sdlproject;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mCategory;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl, String category) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mCategory = category;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }
}
package com.nmg.bmi_app.model;

import android.net.Uri;

public class Food {
    String userId;
    String calary;
    String name;
    String fireUri;
    String category;

    public Food() {
    }

    public Food(String userId, String calary, String name, String fireUri, String category) {
        this.userId = userId;
        this.calary = calary;
        this.name = name;
        this.fireUri = fireUri;
        this.category = category;
    }

    public String getFireUri() {
        return fireUri;
    }

    public void setFireUri(String fireUri) {
        this.fireUri = fireUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCalary() {
        return calary;
    }

    public void setCalary(String calary) {
        this.calary = calary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

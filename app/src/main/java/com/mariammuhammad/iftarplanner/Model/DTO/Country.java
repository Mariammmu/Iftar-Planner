package com.mariammuhammad.iftarplanner.Model.DTO;

public class Country {
        public String strArea;
    private int imageResourceId;

    public Country(String strArea, int imageResourceId) {
        this.strArea = strArea;
        this.imageResourceId = imageResourceId;
    }

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}

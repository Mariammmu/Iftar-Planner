package com.mariammuhammad.iftarplanner.Model.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RootCountries {
    @SerializedName("meals")
    public ArrayList<Country> countries;

    public ArrayList<Country> getCountries() {
        return countries;
    }


}

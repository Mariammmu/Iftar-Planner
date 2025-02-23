package com.mariammuhammad.iftarplanner.Model.Repo;

import com.google.gson.annotations.SerializedName;
import com.mariammuhammad.iftarplanner.Model.DataModel.Country;

import java.util.ArrayList;

public class RootCountries {
    @SerializedName("meals")
    public ArrayList<Country> countries;

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setMeals(ArrayList<Country> meals) {
        this.countries = meals;
    }

}

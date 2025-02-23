package com.mariammuhammad.iftarplanner.Model.Remote.Countries;

import com.mariammuhammad.iftarplanner.Model.DataModel.Country;
import com.mariammuhammad.iftarplanner.Model.DataModel.Ingredient;

import java.util.ArrayList;

public interface CountriesNetworkCallback {

    public void onSuccessfulCountries(ArrayList<Country> meals);

    public void onFaliureCountries(String errorMessage);

}

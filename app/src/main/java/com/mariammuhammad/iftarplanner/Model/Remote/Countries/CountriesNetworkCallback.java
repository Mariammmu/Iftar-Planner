package com.mariammuhammad.iftarplanner.Model.Remote.Countries;

import com.mariammuhammad.iftarplanner.Model.DTO.Country;

import java.util.ArrayList;

public interface CountriesNetworkCallback {

    public void onSuccessfulCountries(ArrayList<Country> meals);

    public void onFaliureCountries(String errorMessage);

}

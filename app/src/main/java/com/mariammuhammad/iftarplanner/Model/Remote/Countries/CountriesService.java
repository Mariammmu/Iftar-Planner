package com.mariammuhammad.iftarplanner.Model.Remote.Countries;

import com.mariammuhammad.iftarplanner.Model.Repo.RootCountries;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountriesService {

    @GET("list.php?a=list")
    Call<RootCountries> getAllCountries();
}

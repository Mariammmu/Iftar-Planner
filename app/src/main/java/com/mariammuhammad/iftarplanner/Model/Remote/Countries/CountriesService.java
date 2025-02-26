package com.mariammuhammad.iftarplanner.Model.Remote.Countries;

import com.mariammuhammad.iftarplanner.Model.DTO.RootCountries;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface CountriesService {

    @GET("list.php?a=list")
    Single<RootCountries> getAllCountries();
}

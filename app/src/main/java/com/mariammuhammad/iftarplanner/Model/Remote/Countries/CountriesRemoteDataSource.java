package com.mariammuhammad.iftarplanner.Model.Remote.Countries;

import com.mariammuhammad.iftarplanner.Model.DTO.RootCountries;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountriesRemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private CountriesService countriesService;
    private static CountriesRemoteDataSource countriesRemoteDataSource = null;
    private String TAG ="AreaNetwok";
    Retrofit retrofit;

    private CountriesRemoteDataSource(){
        retrofit= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
        countriesService = retrofit.create(CountriesService.class);
    }

    public static CountriesRemoteDataSource getInstance(){

        if(countriesRemoteDataSource==null){
            countriesRemoteDataSource= new CountriesRemoteDataSource();
        }
        return countriesRemoteDataSource;
    }

    public Single<RootCountries> getAllCountries( ) {

       return countriesService.getAllCountries();
    }




    }

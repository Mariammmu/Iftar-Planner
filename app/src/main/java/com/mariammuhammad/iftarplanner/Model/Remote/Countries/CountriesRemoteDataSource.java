package com.mariammuhammad.iftarplanner.Model.Remote.Countries;

import android.util.Log;

import com.mariammuhammad.iftarplanner.Model.Repo.RootCountries;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountriesRemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private CountriesService countriesService;
    private static CountriesRemoteDataSource countriesRemoteDataSource = null;
    private String TAG ="AreaNetwok";
    Retrofit retrofit;

    private CountriesRemoteDataSource(){
        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static CountriesRemoteDataSource getInstance(){

        if(countriesRemoteDataSource==null){
            countriesRemoteDataSource= new CountriesRemoteDataSource();
        }
        return countriesRemoteDataSource;
    }

    public void getAllCountries(CountriesNetworkCallback countriesNetworkCallback) {

        countriesService  = retrofit.create(CountriesService.class);
        Call<RootCountries>  callCountries = countriesService.getAllCountries();
        callCountries.enqueue(new Callback<RootCountries>() {
            @Override
            public void onResponse(Call<RootCountries> call, Response<RootCountries> response) {
                if(response.isSuccessful()){
                    countriesNetworkCallback.onSuccessfulCountries(response.body().getCountries());
                    Log.i(TAG, "onResponse: " + response.body().countries.get(0));
                }
                else{
                    countriesNetworkCallback.onFaliureCountries("Failed to fetch Countries: " + response.message());

                }
            }

            @Override
            public void onFailure(Call<RootCountries> call, Throwable throwable) {

                countriesNetworkCallback.onFaliureCountries(throwable.getMessage());
                Log.i(TAG, "onFailure: ");
            }
        });
    }




    }

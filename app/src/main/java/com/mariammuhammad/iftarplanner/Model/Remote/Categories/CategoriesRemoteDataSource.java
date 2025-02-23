package com.mariammuhammad.iftarplanner.Model.Remote.Categories;

import android.util.Log;

import com.mariammuhammad.iftarplanner.Model.Repo.RootCategories;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriesRemoteDataSource {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private CategoriesService categoriesService;
    private static CategoriesRemoteDataSource categoriesRemoteDataSource = null;
    private String TAG="CategoryNetwork";
    private CategoriesRemoteDataSource(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesService = retrofit.create(CategoriesService.class);
    }

    public static CategoriesRemoteDataSource getInstance(){
        if(categoriesRemoteDataSource == null)
        {
            categoriesRemoteDataSource = new CategoriesRemoteDataSource();
        }
        return categoriesRemoteDataSource;
    }

    public void getAllCategories(CategoriesNetworkCallback categoriesNetworkCallback) {
        categoriesService.getAlCategory().enqueue(new Callback<RootCategories>() {
            @Override
            public void onResponse(Call<RootCategories> call, Response<RootCategories> response) {
                if(response.isSuccessful()){
                    categoriesNetworkCallback.onSuccessResultCategory(response.body().getCategories());
                    Log.i("RESPONSE", "onResponse: "+response.body().getCategories().get(0));

                }
                else{

                }
            }

            @Override
            public void onFailure(Call<RootCategories> call, Throwable throwable) {
                    categoriesNetworkCallback.onFailureResult(throwable.getMessage());
            }

        });
    }
}

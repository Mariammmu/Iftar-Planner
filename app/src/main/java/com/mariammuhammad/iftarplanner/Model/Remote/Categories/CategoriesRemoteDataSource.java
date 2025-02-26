package com.mariammuhammad.iftarplanner.Model.Remote.Categories;

import com.mariammuhammad.iftarplanner.Model.DTO.RootCategories;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriesRemoteDataSource {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private CategoriesService categoriesService;
    private static CategoriesRemoteDataSource categoriesRemoteDataSource = null;
    private String TAG="CategoryNetwork";
    Retrofit retrofit;
    private CategoriesRemoteDataSource(){
        retrofit= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
        categoriesService = retrofit.create(CategoriesService.class);
    }

    public static CategoriesRemoteDataSource getInstance(){
        if(categoriesRemoteDataSource == null)
        {
            categoriesRemoteDataSource = new CategoriesRemoteDataSource();
        }
        return categoriesRemoteDataSource;
    }

    public Single<RootCategories> getAllCategories( ) {
        return categoriesService.getAlCategory();
    }
}

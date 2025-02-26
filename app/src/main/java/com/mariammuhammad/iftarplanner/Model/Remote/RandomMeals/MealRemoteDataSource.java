package com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals;

import com.mariammuhammad.iftarplanner.Model.DTO.RootMeal;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSource {
    public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static MealRemoteDataSource mealRemoteDataSource = null;
    private static final String TAG="MealAPI";

    Retrofit retrofit;
    CallMeals callMeals;

    public MealRemoteDataSource(){
        retrofit= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
        callMeals = retrofit.create(CallMeals.class);
    }

    public static MealRemoteDataSource getInstance(){
        if(mealRemoteDataSource==null){
            mealRemoteDataSource=new MealRemoteDataSource();
        }
        return mealRemoteDataSource;
    }

    public Single<RootMeal> getAllMeals(String dataType, String filterValue) {

        switch (dataType) {
            case "ingredient":
                return callMeals.getMealsByIngredient(filterValue);
            case "category":
                return callMeals.getMealsByCategory(filterValue);
            case "area":
                return callMeals.getMealsByCountry(filterValue);
            case "random":
                return callMeals.getRandomMeal();
            default:
                return Single.error(new IllegalArgumentException("Unsupported data type: " + dataType));
        }

    }

    public Single<RootMeal> getMealById(int mealId){
        return callMeals.getMealDetails(mealId);
    }


}

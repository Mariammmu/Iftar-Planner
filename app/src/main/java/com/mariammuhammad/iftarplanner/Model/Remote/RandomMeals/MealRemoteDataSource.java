package com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals;

import com.mariammuhammad.iftarplanner.Model.Repo.RootMeal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSource {
    public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static MealRemoteDataSource mealRemoteDataSource = null;
    private static final String TAG="MealAPI";

    Retrofit retrofit;

    public MealRemoteDataSource(){
        retrofit= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static MealRemoteDataSource getInstance(){
        if(mealRemoteDataSource==null){
            mealRemoteDataSource=new MealRemoteDataSource();
        }
        return mealRemoteDataSource;
    }

    public void getAllMeals(NetworkCallback networkCallback, String dataType, String filterValue) {
        CallMeals callMeals = retrofit.create(CallMeals.class);
        Call<RootMeal> rootMealCall;
        switch (dataType) {
            case "ingredient":
                rootMealCall = callMeals.getMealsByIngredient(filterValue);
                break;
            case "category":
                rootMealCall = callMeals.getMealsByCategory(filterValue);
                break;
            case "area":
                rootMealCall = callMeals.getMealsByCountry(filterValue);
                break;
            case "random":
                rootMealCall = callMeals.getRandomMeal();
                break;
            default:
                networkCallback.onFailureResult("Unsupported data type: " + dataType);
                return;
        }

        rootMealCall.enqueue(new Callback<RootMeal>() {


            @Override
            public void onResponse(Call<RootMeal> call, Response<RootMeal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallback.onSuccessResult(response.body().meals);


                } else {
                    networkCallback.onFailureResult("Failed to fetch products: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RootMeal> call, Throwable throwable) {
                networkCallback.onFailureResult(throwable.getMessage());

            }
        });

    }


}

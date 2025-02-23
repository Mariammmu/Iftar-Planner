package com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals;

import com.mariammuhammad.iftarplanner.Model.Repo.RootMeal;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallMeals {

    @GET("random.php")
    Call<RootMeal> getRandomMeal();

    @GET("search.php")
    Call<RootMeal> getAllMealsByLetter(@Query("f") String letter);

    @GET("filter.php")
    Call<RootMeal> getMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Call<RootMeal> getMealsByCountry(@Query("a") String country);


    @GET("filter.php")
    Call<RootMeal> getMealsByCategory(@Query("c") String category);

}

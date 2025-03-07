package com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals;

import com.mariammuhammad.iftarplanner.Model.DTO.RootMeal;


import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallMeals {

    @GET("random.php")
    Single<RootMeal> getRandomMeal();

        @GET("lookup.php")
    Single<RootMeal> getMealDetails(@Query("i") int mealId);
//

    @GET("filter.php")
    Single<RootMeal> getMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Single<RootMeal> getMealsByCountry(@Query("a") String country);


    @GET("filter.php")
    Single<RootMeal> getMealsByCategory(@Query("c") String category);
    @GET("search.php")
    Single<RootMeal> getMealByName(@Query("s") String meal);

//    @GET("search.php")
//    Call<RootMeal> getAllMealsByLetter(@Query("f") String letter);
//    @GET("categories.php")
//    Single<CategoryDTO> getCategories();
//
//    @GET("list.php?a=list")
//    Single<CountryDTO> getCountry();
//
//
//



}

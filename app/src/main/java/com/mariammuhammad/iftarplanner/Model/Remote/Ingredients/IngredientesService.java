package com.mariammuhammad.iftarplanner.Model.Remote.Ingredients;

import com.mariammuhammad.iftarplanner.Model.Repo.RootIngredients;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IngredientesService {

    @GET("list.php?i=list")
    Call<RootIngredients> getAllIngredients();
}

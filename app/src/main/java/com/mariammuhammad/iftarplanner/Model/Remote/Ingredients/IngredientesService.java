package com.mariammuhammad.iftarplanner.Model.Remote.Ingredients;

import com.mariammuhammad.iftarplanner.Model.DTO.RootIngredients;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface IngredientesService {

    @GET("list.php?i=list")
    Single<RootIngredients> getAllIngredients();


}

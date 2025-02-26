package com.mariammuhammad.iftarplanner.Model.Remote.Ingredients;

import com.mariammuhammad.iftarplanner.Model.DTO.Ingredient;

import java.util.ArrayList;

public interface IngredientsNetworkCallback {
        public void onSuccessResultIngrediants(ArrayList<Ingredient> meals);
        public void onFailureResult(String errorMsg);
    }

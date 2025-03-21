package com.mariammuhammad.iftarplanner.Views.filter;

import com.mariammuhammad.iftarplanner.Common.Loading;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;

import java.util.ArrayList;

public interface FilteringView extends Loading {

    public void showMealsByCategory(ArrayList<Meal> meals);
    public void showMealsByCountry(ArrayList<Meal> meals);
    public void showMealsByIngredient(ArrayList<Meal> meals);
    public void showError(String errorMsg);
}

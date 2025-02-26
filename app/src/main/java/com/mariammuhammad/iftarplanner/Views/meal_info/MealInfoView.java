package com.mariammuhammad.iftarplanner.Views.meal_info;

import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.MealStorage;

public interface MealInfoView {
    public void showMealDetailsById(Meal meal);
    public void showSuccessMessage(String message);
    void deleteMealFromFavourite(MealStorage mealStorage);

    void showError(String message);
}

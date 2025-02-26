package com.mariammuhammad.iftarplanner.Presenter.meal_info;

import com.mariammuhammad.iftarplanner.Model.MealStorage;

public interface MealInfoContract {
    void getMealDetailsById(int id);
    void addToFavourite(MealStorage mealStorage);
    void addToPlan(MealStorage mealStorage);
    void deleteMealFromFavourite(MealStorage mealStorage);
    void sendData(MealStorage mealStorage);
}

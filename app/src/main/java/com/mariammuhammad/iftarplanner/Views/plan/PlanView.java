package com.mariammuhammad.iftarplanner.Views.plan;

import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.MealStorage;

import java.util.List;

public interface PlanView {
    void showMeals(List<MealStorage> mealStorages);
    void displayMeal(MealStorage mealStorage);
//    void showEmptyDay();
    void showError(String message);
    void showSuccess(String message);
    void showMealCount(int count);
    void showEmptyDay();
}

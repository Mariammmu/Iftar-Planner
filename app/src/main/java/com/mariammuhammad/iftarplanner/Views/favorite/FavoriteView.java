package com.mariammuhammad.iftarplanner.Views.favorite;

import com.mariammuhammad.iftarplanner.Model.MealStorage;

import java.util.List;

public interface FavoriteView {
    void showDataSuccess(List<MealStorage> mealStorage);
    void displaySuccess(MealStorage mealStorage);
    void showError(String message);
}

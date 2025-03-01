package com.mariammuhammad.iftarplanner.Presenter.favorite;

import com.mariammuhammad.iftarplanner.Model.MealStorage;

public interface FavoriteContract {
    void getAllFavoriteMeals();
    void deleteMealFromFavourite(MealStorage mealStorage);
    void deleteData(MealStorage mealStorage);
}

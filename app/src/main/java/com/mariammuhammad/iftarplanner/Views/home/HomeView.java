package com.mariammuhammad.iftarplanner.Views.home;

import com.mariammuhammad.iftarplanner.Model.DTO.Category;
import com.mariammuhammad.iftarplanner.Model.DTO.Country;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;

import java.util.ArrayList;

public interface HomeView {

    void showMealOfTheDay(ArrayList<Meal> meals);
    void showCategories(ArrayList<Category> categories);

   void showCountries(ArrayList<Country> countries);
    void showError(String errorMsg);

    void showLoading();
    void hideLoading();

   void onLogoutSuccess();
}

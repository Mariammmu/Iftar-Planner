package com.mariammuhammad.iftarplanner.Views.search;

import com.mariammuhammad.iftarplanner.Model.DTO.Category;
import com.mariammuhammad.iftarplanner.Model.DTO.Country;
import com.mariammuhammad.iftarplanner.Model.DTO.Ingredient;

import java.util.ArrayList;

public interface SearchView {
    void ShowIngredients(ArrayList<Ingredient> ingredients);
    void showCategories(ArrayList<Category> categories);
    void showCountries(ArrayList<Country> countries);
    void showError(String errorMsg);
}

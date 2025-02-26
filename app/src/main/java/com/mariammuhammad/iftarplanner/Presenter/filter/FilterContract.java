package com.mariammuhammad.iftarplanner.Presenter.filter;

public interface FilterContract {
    void getMealsByCategory(String category);
    void getMealsByCountry(String area);
    void getMealsByIngredient(String ingredient);
}

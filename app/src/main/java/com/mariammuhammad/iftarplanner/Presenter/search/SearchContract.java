package com.mariammuhammad.iftarplanner.Presenter.search;

public interface SearchContract {
    void getIngredients();
    void getCategories();
    void getCountries();
    void getMealByName(String name);
}

package com.mariammuhammad.iftarplanner.Presenter.home;

public interface HomeContract {

    void getRandomMeals();
    void getAllCategories();

    void getAllCountries();
    void getDataFromFirebase();
}

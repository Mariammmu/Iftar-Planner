package com.mariammuhammad.iftarplanner.Model.Repo;

import com.mariammuhammad.iftarplanner.Model.DataModel.Meal;

import java.util.ArrayList;

public class RootMeal {
        private ArrayList<Meal> meals;

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }
}

package com.mariammuhammad.iftarplanner.Model.Repo;

import com.mariammuhammad.iftarplanner.Model.DataModel.Ingredient;

import java.util.ArrayList;

public class RootIngredients {
    private ArrayList<Ingredient> meals;

    public ArrayList<Ingredient> getIngredients() {
        return meals;
    }

    public void setIngredients(ArrayList<Ingredient> meals) {
        this.meals = meals;
    }
}

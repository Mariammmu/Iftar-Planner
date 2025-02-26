package com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals;

import com.mariammuhammad.iftarplanner.Model.DTO.Meal;

import java.util.ArrayList;

public interface RandomNetworkCallback {

    public void onSuccessResult(ArrayList<Meal> meals);
    public void onFailureResult(String errorMsg);
}

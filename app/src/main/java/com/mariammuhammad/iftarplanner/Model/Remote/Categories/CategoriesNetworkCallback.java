package com.mariammuhammad.iftarplanner.Model.Remote.Categories;

import com.mariammuhammad.iftarplanner.Model.DTO.Category;

import java.util.ArrayList;

public interface CategoriesNetworkCallback {
    public void onSuccessResultCategory(ArrayList<Category> categories);
    public void onFailureResult(String errorMsg);

}

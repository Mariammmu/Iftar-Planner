package com.mariammuhammad.iftarplanner.Model.Remote.Categories;

import com.mariammuhammad.iftarplanner.Model.DataModel.Category;
import com.mariammuhammad.iftarplanner.Model.Repo.RootCategories;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesNetworkCallback {
    public void onSuccessResultCategory(ArrayList<Category> categories);
    public void onFailureResult(String errorMsg);

}

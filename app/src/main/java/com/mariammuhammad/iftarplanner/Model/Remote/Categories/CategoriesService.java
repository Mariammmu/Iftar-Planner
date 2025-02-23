package com.mariammuhammad.iftarplanner.Model.Remote.Categories;

import com.mariammuhammad.iftarplanner.Model.Repo.RootCategories;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesService {

    @GET("categories.php")
    Call<RootCategories> getAlCategory();
}

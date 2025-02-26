package com.mariammuhammad.iftarplanner.Model.Remote.Categories;

import com.mariammuhammad.iftarplanner.Model.DTO.RootCategories;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface CategoriesService {

    @GET("categories.php")
    Single<RootCategories> getAlCategory();
}

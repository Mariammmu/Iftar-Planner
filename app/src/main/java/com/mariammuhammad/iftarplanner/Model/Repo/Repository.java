package com.mariammuhammad.iftarplanner.Model.Repo;

import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.DTO.RootCategories;
import com.mariammuhammad.iftarplanner.Model.DTO.RootCountries;
import com.mariammuhammad.iftarplanner.Model.DTO.RootIngredients;
import com.mariammuhammad.iftarplanner.Model.DTO.RootMeal;
import com.mariammuhammad.iftarplanner.Model.Local.MealLocalDataSource;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Remote.Categories.CategoriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Countries.CountriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Ingredients.IngredientsRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals.MealRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Repository {

    private static Repository repository = null;
    private MealRemoteDataSource mealRemoteDataSource;
    private IngredientsRemoteDataSource ingredientsRemoteDataSource;
    private CountriesRemoteDataSource countriesRemoteDataSource;
    private CategoriesRemoteDataSource categoriesRemoteDataSource;
    private MealLocalDataSource mealLocalDataSource;

    private Repository(MealLocalDataSource mealLocalDataSource,MealRemoteDataSource mealRemoteDataSource, CategoriesRemoteDataSource categoriesRemoteDataSource, CountriesRemoteDataSource countriesRemoteDataSource, IngredientsRemoteDataSource ingredientsRemoteDataSource ) {
        this.mealLocalDataSource=mealLocalDataSource;
        this.mealRemoteDataSource=mealRemoteDataSource;
        this.categoriesRemoteDataSource=categoriesRemoteDataSource;
        this.countriesRemoteDataSource=countriesRemoteDataSource;
        this.ingredientsRemoteDataSource=ingredientsRemoteDataSource;
    }

    public static Repository getInstance (MealLocalDataSource mealLocalDataSource,MealRemoteDataSource mealRemoteDataSource, CategoriesRemoteDataSource categoriesRemoteDataSource, CountriesRemoteDataSource countriesRemoteDataSource, IngredientsRemoteDataSource ingredientsRemoteDataSource ) {
        if (repository == null) {
            repository = new Repository(mealLocalDataSource,mealRemoteDataSource,categoriesRemoteDataSource,countriesRemoteDataSource,ingredientsRemoteDataSource);
        }
        return repository;
    }

    public Single<RootMeal> getMeals() {
        return mealRemoteDataSource.getAllMeals("random","" );
    }

    public Single<RootCategories> getCategories() {
        return categoriesRemoteDataSource.getAllCategories();
    }

    public Single<RootCountries> getCountries() {
        return countriesRemoteDataSource.getAllCountries();
    }
    public  Single<RootIngredients> getIngredients() {
        return ingredientsRemoteDataSource.getIngredients();
    }
    public Single<RootMeal> getMealByName(String name){
        return mealRemoteDataSource.getMealByName(name);
    }
    public Single<RootMeal> getMealsByCategory(String category) {
        return mealRemoteDataSource.getAllMeals("category", category);
    }

    public Single<RootMeal> getMealsByCountry(String area) {
        return mealRemoteDataSource.getAllMeals("area", area);
    }

    public Single<RootMeal> getMealsByIngredient(String ingredient) {
        return mealRemoteDataSource.getAllMeals("ingredient", ingredient);
    }

    public Single<RootMeal> getMealById(int mealId) {
        return mealRemoteDataSource.getMealById(mealId);
    }

    public Completable insertMeal(MealStorage mealStorage) {
        return mealLocalDataSource.insert(mealStorage);
    }

    public Single<List<MealStorage>> getAllFavouriteMeals() {
        return mealLocalDataSource.getAllFavourites();
    }
    public Single<List<MealStorage>> getAllMealsFromPlan() {
        return mealLocalDataSource.getAllPlans();
    }

    public Completable deleteMeal(MealStorage mealStorage) {
        return mealLocalDataSource.delete(mealStorage);
    }
    public Completable clearAllData() {
        return mealLocalDataSource.deleteAll();
    }

    public void fetchDataFromFirebase() {
        mealLocalDataSource.fetchDataFromFirebase();
    }
    public Completable addToPlan(MealStorage mealStorage) {
        return mealLocalDataSource.insert(mealStorage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Single<List<MealStorage>> getMealsByDate(String date) {
        return mealLocalDataSource.getMealsByDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    public Single<Integer> countMealsByDate(String date) {
        return mealLocalDataSource.countMealsByDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteMealsByDate(String date) {
        return mealLocalDataSource.deleteMealsByDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}

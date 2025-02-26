package com.mariammuhammad.iftarplanner.Model.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mariammuhammad.iftarplanner.Model.MealStorage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

//DAO (Data Access Object) is responsible for defining the database operations based on your needs.
// You should write methods in your DAO that match what you want to do with your data.
@Dao
public interface MealDAO {
    @Query("SELECT * FROM meals_table WHERE is_favourite = 1")
    Single<List<MealStorage>> getAllFavouriteMeals();

    @Query("SELECT * FROM meals_table WHERE is_planned = 1")
    Single<List<MealStorage>> getAllPlannedMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMeal(MealStorage mealStorage);

    @Delete
    Completable deleteMeal(MealStorage mealStorage);

    @Query("DELETE FROM meals_table")
    Completable deleteAllMeals();

}
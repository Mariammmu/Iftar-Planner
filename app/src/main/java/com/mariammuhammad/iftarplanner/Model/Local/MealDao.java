package com.mariammuhammad.iftarplanner.Model.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.mariammuhammad.iftarplanner.Model.DataModel.Meal;

import java.util.ArrayList;

import retrofit2.http.Query;

@Dao

public interface MealDao {

        //@Query("sele* From meal_table")
       // Observable<ArrayList<Meal>> getAllMeals();

//   @Query("SELECT * FROM singleMeal WHERE name LIKE :first " + "LIMIT 1")
//    Movie findMovieByName(String first);

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insertAll(Meal meal);

        @Delete
        void delete(Meal meal);
    }


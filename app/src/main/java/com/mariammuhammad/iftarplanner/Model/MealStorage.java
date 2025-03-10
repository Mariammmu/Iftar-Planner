package com.mariammuhammad.iftarplanner.Model;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import com.mariammuhammad.iftarplanner.Model.DTO.Meal;

@Entity(tableName = "meals_table",
        primaryKeys = {"meal_id", "user_id", "date"})
public class MealStorage {

    @NonNull
    @ColumnInfo(name = "meal_id")
    private String mealId;

    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;


    private String type;

    @Embedded // Stores Meal fields as separate columns
    private Meal meal;

    @ColumnInfo(name = "is_favourite")
    private boolean isFavourite;

    @ColumnInfo(name = "is_planned")
    private boolean isPlanned;

    // Default constructor
    public MealStorage() {
    }

    // Constructor
    public MealStorage(boolean isPlanned, boolean isFavourite, Meal meal,
                       @NonNull String date, @NonNull String userId, @NonNull String mealId) {
        this.isPlanned = isPlanned;
        this.isFavourite = isFavourite;
        this.meal = meal;
        this.date = date;
        this.userId = userId;
        this.mealId = mealId;
    }

    public boolean isPlanned() {
        return isPlanned;
    }

    public void setPlanned(boolean planned) {
        isPlanned = planned;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public String getDate() {
        return date;
    }

    public void setDate( String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getMealId() {
        return mealId;
    }

    public void setMealId(@NonNull String mealId) {
        this.mealId = mealId;
    }
}

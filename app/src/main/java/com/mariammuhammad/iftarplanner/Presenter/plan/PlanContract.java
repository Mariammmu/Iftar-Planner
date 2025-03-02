package com.mariammuhammad.iftarplanner.Presenter.plan;

import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.MealStorage;

public interface PlanContract {
    void getAllMealsFromPlan();
    void deleteMealFromCalendar(MealStorage mealStorage);
    void deleteData(MealStorage mealStorage);
   // void loadMealsForDate(String date);
 //   void deleteMealFromCalendar(Meal meal);
}

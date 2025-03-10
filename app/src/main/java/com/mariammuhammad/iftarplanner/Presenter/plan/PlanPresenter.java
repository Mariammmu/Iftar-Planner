package com.mariammuhammad.iftarplanner.Presenter.plan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mariammuhammad.iftarplanner.Common.MySharedPrefs;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.plan.PlanView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanPresenter implements PlanContract
{
    private final PlanView planView;
    private final Repository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    DatabaseReference databaseReference;

    public PlanPresenter( Context context, PlanView planView, Repository repository) {
        this.planView = planView;
        this.repository = repository;
        databaseReference = FirebaseDatabase.getInstance().getReference("Meals");
    }

//    @Override
//    public void getAllMealsFromPlan() {
//        compositeDisposable.add(
//                repository.getAllMealsFromPlan()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                mealStorages -> {
//                                    if (mealStorages != null) {
//                                        planView.showMeals(mealStorages);
//                                    } else {
//                                        planView.showError("No meals found");
//                                    }
//                                }));
//    }

    @Override
    public void deleteMealFromCalendar(MealStorage mealStorage) {
        repository.deleteMeal(mealStorage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            planView.displayMeal(mealStorage);
                        }
                        , throwable -> {
                            planView.showError(throwable.getMessage());
                        });
    }

    @Override
    public void deleteData(MealStorage mealStorage) {
        String userId = MySharedPrefs.getInstance().getString("userId", null);
        if (userId != null) {
            databaseReference.child("Users")
                    .child(userId)
                    .child(mealStorage.getMealId())
                    .child(mealStorage.getDate())
                    .removeValue();
        }
    }

    @Override
    public void loadMealsForDate(String date) {
        compositeDisposable.add(
                repository.getMealsByDate(date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (meals.isEmpty()) {
                                        Log.i("TAG", "loadMealsForDate: No meals found for this date");
                                        planView.showEmptyDay();
                                    } else {
                                        planView.showMeals(meals);
                                    }
                                },
                                throwable -> planView.showError(throwable.getMessage())
                        ));
    }

    public Single<List<MealStorage>> getMealsByDate(String date) {
        return repository.getMealsByDate(date)
                .doOnSuccess(meals -> Log.d("DEBUG", "Fetched Planned Meals for " + date + ": " + meals.size()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }


//    @Override
//    public void countMealsForDate(String date) {
//        compositeDisposable.add(
//                repository.countMealsByDate(date)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                count -> planView.showMealCount(count),
//                                throwable -> planView.showError(throwable.getMessage())
//                        ));
//    }



//    @Override
//    public void deleteMealsByDate(String date) {
//        compositeDisposable.add(
//                repository.deleteMealsByDate(date)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                () -> planView.showSuccess("Meals deleted for " + date),
//                                throwable -> planView.showError(throwable.getMessage())
//                        ));
//    }

//    public void addToPlan(MealStorage mealStorage) {
//        repository.addToPlan(mealStorage)
//                .subscribe(() -> {
//                    planView.showMeals((List<MealStorage>) mealStorage);
//                }, throwable -> {
//                    // Handle error
//                    planView.showError("Failed to add meal: " + throwable.getMessage());
//                });
//    }

}


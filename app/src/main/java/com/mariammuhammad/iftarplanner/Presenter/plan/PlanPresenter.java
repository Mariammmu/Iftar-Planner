package com.mariammuhammad.iftarplanner.Presenter.plan;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.plan.PlanView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanPresenter implements PlanContract
{
    private final PlanView planView;
    private final Repository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    public PlanPresenter( Context context, PlanView planView, Repository repository) {
        this.planView = planView;
        this.repository = repository;
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Meals");
    }

    @Override
    public void getAllMealsFromPlan() {
        compositeDisposable.add(
                repository.getAllMealsFromPlan()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mealStorages -> {
                                    if (mealStorages != null) {
                                        planView.showMeals(mealStorages);
                                    } else {
                                        planView.showError("No meals found");
                                    }
                                }));
    }

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
        String userId = sharedPreferences.getString("userId", null);
        if (userId != null) {
            databaseReference.child("Users")
                    .child(userId)
                    .child(mealStorage.getMealId())
                    .child(mealStorage.getDate())
                    .removeValue();
        }
    }

    public void addToPlan(MealStorage mealStorage) {
        repository.addToPlan(mealStorage)
                .subscribe(() -> {
                    planView.showMeals((List<MealStorage>) mealStorage);
                }, throwable -> {
                    // Handle error
                    planView.showError("Failed to add meal: " + throwable.getMessage());
                });
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}


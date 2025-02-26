package com.mariammuhammad.iftarplanner.Presenter.meal_info;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.meal_info.MealInfoView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealInfoPresenter implements MealInfoContract{
    private final MealInfoView mealInfoView;
    private final Repository repository;

    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences sharedPreferences;

    public MealInfoPresenter(MealInfoView mealInfoView, Repository repository, Context context) {
        this.mealInfoView = mealInfoView;
        this.repository = repository;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Meals");
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void getMealDetailsById(int id) {
        Disposable disposable = repository.getMealById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealResponse -> {
                    mealInfoView.showMealDetailsById(mealResponse.getMeals().get(0));
                }, throwable -> mealInfoView.showError(throwable.getMessage()));
    }

    @Override
    public void addToFavourite(MealStorage mealStorage) {
        repository.insertMeal(mealStorage).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mealInfoView.showSuccessMessage("Meal added to favourites"),
                        throwable -> mealInfoView.showError(throwable.getMessage()));


    }

    @Override
    public void addToPlan(MealStorage mealStorage) {
        repository.insertMeal(mealStorage).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mealInfoView.showSuccessMessage("Meal added to plan"),
                        throwable -> mealInfoView.showError(throwable.getMessage()));
    }

    @Override
    public void deleteMealFromFavourite(MealStorage mealStorage) {
        repository.deleteMeal(mealStorage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            mealInfoView.showSuccessMessage("Sccessfully deleted");
                        }
                        , throwable -> {
                            mealInfoView.showError(throwable.getMessage());
                        });
    }

    @Override
    public void sendData(MealStorage mealStorage) {
        String userId = sharedPreferences.getString("userId", null);
        myRef.child("Users")
                .child(userId)
                .child(mealStorage.getMealId())
                .child(mealStorage.getDate())
                .setValue(mealStorage);
    }
}

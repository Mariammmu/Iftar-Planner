package com.mariammuhammad.iftarplanner.Presenter.meal_info;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mariammuhammad.iftarplanner.Common.MySharedPrefs;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.meal_info.MealInfoView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealInfoPresenter implements MealInfoContract{
    private final MealInfoView mealInfoView;
    private final Repository repository;

    DatabaseReference myRef;

    public MealInfoPresenter(MealInfoView mealInfoView, Repository repository) {
        this.mealInfoView = mealInfoView;
        this.repository = repository;

        myRef = FirebaseDatabase.getInstance().getReference("Meals");
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
      // String userId = MySharedPrefs.getInstance().getString("userId", null);
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.child("Users")
                .child(userId)
                .child(mealStorage.getMealId())
                .child(mealStorage.getDate())
                .setValue(mealStorage).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("TAG", "onSuccess:sendData ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TAG", "onFailure:send ");
                    }
                });
    }
}

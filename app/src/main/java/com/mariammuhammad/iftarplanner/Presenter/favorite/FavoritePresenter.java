package com.mariammuhammad.iftarplanner.Presenter.favorite;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mariammuhammad.iftarplanner.Common.MySharedPrefs;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.favorite.FavoriteView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenter implements FavoriteContract {
    private final FavoriteView favoriteView;
    private final Repository repository;
    DatabaseReference databaseReference;

    public FavoritePresenter(FavoriteView favoriteView, Repository repository) {
        this.favoriteView = favoriteView;
        this.repository = repository;
        databaseReference = FirebaseDatabase.getInstance().getReference("Meals");
    }

    @Override
    public void getAllFavoriteMeals() {
        repository.getAllFavouriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealStorages -> {
                            if (mealStorages != null) {
                                favoriteView.showDataSuccess(mealStorages);
                            } else {
                                favoriteView.showError("No meals found");
                            }
                        });
    }


    @Override
    public void deleteMealFromFavourite(MealStorage mealStorage) {
        repository.deleteMeal(mealStorage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            favoriteView.displaySuccess(mealStorage);
                        }
                        , throwable -> {
                            favoriteView.showError(throwable.getMessage());
                        });
    }

    @Override
    public void deleteData(MealStorage mealStorage) {
       // String userId = MySharedPrefs.getInstance().getString("userId", null);
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId != null) {
            databaseReference.child("Users")
                    .child(userId)
                    .child(mealStorage.getMealId())
                    .child(mealStorage.getDate())
                    .removeValue();
        }
    }
}

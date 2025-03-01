package com.mariammuhammad.iftarplanner.Presenter.favorite;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.favorite.FavoriteView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenter implements FavoriteContract {
    private final FavoriteView favoriteView;
    private final Repository repository;

    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    public FavoritePresenter(FavoriteView favoriteView, Repository repository, Context context) {
        this.favoriteView = favoriteView;
        this.repository = repository;
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Meals");
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
        String userId = sharedPreferences.getString("userId", null);
        if (userId != null) {
            databaseReference.child("Users")
                    .child(userId)
                    .child(mealStorage.getMealId())
                    .child(mealStorage.getDate())
                    .removeValue();
        }
    }
}

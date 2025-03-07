package com.mariammuhammad.iftarplanner.Presenter.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mariammuhammad.iftarplanner.Model.DTO.RootCategories;
import com.mariammuhammad.iftarplanner.Model.DTO.RootCountries;
import com.mariammuhammad.iftarplanner.Model.DTO.RootMeal;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.home.HomeView;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContract{


    private final HomeView homeView;
    private final Repository repository;
    private final CompositeDisposable disposable;



    public HomePresenter(HomeView homeView, Repository repository) {
        this.homeView = homeView;
        this.repository = repository;
        this.disposable = new CompositeDisposable();
    }


    @Override
    public void getRandomMeals() {
        homeView.showLoading();

        disposable.add(
                repository.getMeals()
                        .toObservable()
                        .repeat(5)
                        .subscribeOn(Schedulers.io())
                        .map(RootMeal::getMeals)
                        .flatMapIterable(meals -> meals)
                        .toList()
                        .map(ArrayList::new)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    homeView.hideLoading();
                                    homeView.showMealOfTheDay(meals);
                                },
                                throwable -> {
                                    homeView.hideLoading();
                                    homeView.showError(throwable.getMessage());
                                }
                        )
        );
    }


    @Override
    public void getAllCategories() {
        homeView.showLoading();
        disposable.add(
                repository.getCategories()
                .subscribeOn(Schedulers.io())
                .map(RootCategories::getCategories)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categoryResponse -> {
                    homeView.hideLoading();
                    if (categoryResponse != null && !categoryResponse.isEmpty()) {
                        homeView.showCategories(categoryResponse);
                    } else {
                        homeView.showError("No categories found");
                    }
                }, throwable -> homeView.showError(throwable.getMessage())

                )
        );

    }



    @Override
    public void getAllCountries() {
        homeView.showLoading();
        disposable.add(
                repository.getCountries().subscribeOn(Schedulers.io())
                .map(RootCountries::getCountries).observeOn(AndroidSchedulers.mainThread())
                .subscribe(countryRespnse -> {
                    homeView.hideLoading();
                    homeView.showCountries(countryRespnse);
                },
                        throwable ->{
                    homeView.showError(throwable.getMessage());

                })
        );
    }

//    @Override
//    public void logout() {
////        firebaseAuth.signOut();
////        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        editor.clear().apply();
//    }

    @Override
    public void getDataFromFirebase() {
        repository.fetchDataFromFirebase();

    }




    public void releaseResources() {
        disposable.clear();
    }

}

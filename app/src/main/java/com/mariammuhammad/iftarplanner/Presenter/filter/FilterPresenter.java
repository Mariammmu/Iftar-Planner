package com.mariammuhammad.iftarplanner.Presenter.filter;

import com.mariammuhammad.iftarplanner.Model.DTO.RootCategories;
import com.mariammuhammad.iftarplanner.Model.DTO.RootMeal;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.filter.FilteringView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FilterPresenter implements FilterContract{
    private final FilteringView mealFilteringView;
    private final Repository repository;
    private final CompositeDisposable compositeDisposable;

    public FilterPresenter(FilteringView mealFilteringView, Repository repository) {
        this.mealFilteringView = mealFilteringView;
        this.repository = repository;
        this.compositeDisposable = new CompositeDisposable();
    }
    @Override
    public void getMealsByCategory(String category) {

        mealFilteringView.showLoading();
        Disposable disposable = repository.getMealsByCategory(category)
                .subscribeOn(Schedulers.io())
                .map(RootMeal::getMeals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealCategoryResponse -> {
                    mealFilteringView.hideLoading();
                    mealFilteringView.showMealsByCategory(mealCategoryResponse);
                }, throwable -> mealFilteringView.showError(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void getMealsByCountry(String area) {
        mealFilteringView.showLoading();
        Disposable disposable = repository.getMealsByCountry(area)
                .subscribeOn(Schedulers.io())
                .map(RootMeal::getMeals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealCategoryResponse -> {
                    mealFilteringView.hideLoading();
                    mealFilteringView.showMealsByCountry(mealCategoryResponse);
                }, throwable -> mealFilteringView.showError(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void getMealsByIngredient(String ingredient) {
        mealFilteringView.showLoading();
        Disposable disposable = repository.getMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .map(RootMeal::getMeals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealIngredientResponse -> {
                    mealFilteringView.hideLoading();
                    mealFilteringView.showMealsByIngredient(mealIngredientResponse);
                }, throwable -> mealFilteringView.showError(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }


}

package com.mariammuhammad.iftarplanner.Presenter.filter;

import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.filter.FilteringView;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FilterPresenter implements FilterContract{
    private final FilteringView filteringView;
    private final Repository repository;

    private final CompositeDisposable compositeDisposable;

    public FilterPresenter(FilteringView mealFilteringView, Repository repository) {
        this.filteringView = mealFilteringView;
        this.repository = repository;
        this.compositeDisposable = new CompositeDisposable();
    }
    @Override
    public void getMealsByCategory(String category) {

    }

    @Override
    public void getMealsByCountry(String area) {

    }

    @Override
    public void getMealsByIngredient(String ingredient) {

    }
}

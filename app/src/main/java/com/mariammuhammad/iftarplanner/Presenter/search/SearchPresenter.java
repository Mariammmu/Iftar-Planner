package com.mariammuhammad.iftarplanner.Presenter.search;

import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.search.SearchView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter implements SearchContract {
    private final SearchView searchView;
    private final Repository repository;
    private final CompositeDisposable compositeDisposable;

    public SearchPresenter(SearchView searchView, Repository repository) {
        this.searchView = searchView;
        this.repository = repository;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getIngredients() {
        Disposable disposable = repository.getIngredients()
                .map(item -> item.getIngredients())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> searchView.ShowIngredients(item),
                        err -> searchView.showError(err.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void getCategories() {
        Disposable disposable = repository.getCategories()
                .map(item -> item.getCategories())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> searchView.showCategories(item),
                        err -> searchView.showError(err.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void getCountries() {
        Disposable disposable = repository.getCountries()
                .map(item -> item.getCountries())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> searchView.showCountries(item),
                        err -> searchView.showError(err.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    public void getMeal() {
        Disposable disposable = repository.getMeals()
                .map(item -> item.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> searchView.showMeals(item),
                        error -> searchView.showError(error.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    public void clearDisposables() {
        compositeDisposable.clear();
    }
}

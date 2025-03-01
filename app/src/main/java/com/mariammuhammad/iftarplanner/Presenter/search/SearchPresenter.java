package com.mariammuhammad.iftarplanner.Presenter.search;

import com.mariammuhammad.iftarplanner.Model.DTO.Category;
import com.mariammuhammad.iftarplanner.Model.DTO.Country;
import com.mariammuhammad.iftarplanner.Model.DTO.Ingredient;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.search.SearchFragment;
import com.mariammuhammad.iftarplanner.Views.search.SearchView;

import java.util.ArrayList;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter implements SearchContract {
    private SearchView searchView;
    private Repository repository;
    private  CompositeDisposable compositeDisposable;

    public SearchPresenter(SearchView searchView, Repository repository){
        this.searchView=searchView;
        this.repository=repository;
        compositeDisposable=new CompositeDisposable();
    }
    @Override
    public void getIngredients() {
        repository.getIngredients()
                .map(item->item.getIngredients())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item->searchView.ShowIngredients(item),
                        err->searchView.showError(err.getMessage())
                );
    }

    @Override
    public void getCategories() {
        repository.getCategories()
                .map(item->item.getCategories())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item-> searchView.showCategories(item),
                        err->searchView.showError(err.getMessage())
                );
    }

    @Override
    public void getCountries() {
        repository.getCountries()
                .map(item->item.getCountries())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item->searchView.showCountries(item),
                        err->searchView.showError(err.getMessage())
                );
    }

    public void getMeal(){
        repository.getMeals().
                map(item-> item.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item->searchView.showMeals(item),
                        error->searchView.showError(error.getMessage())
                );
    }



}

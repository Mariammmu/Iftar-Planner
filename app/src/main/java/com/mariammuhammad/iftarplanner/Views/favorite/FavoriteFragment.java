package com.mariammuhammad.iftarplanner.Views.favorite;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.Local.MealLocalDataSource;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Remote.Categories.CategoriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Countries.CountriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Ingredients.IngredientsRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals.MealRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Presenter.favorite.FavoritePresenter;
import com.mariammuhammad.iftarplanner.R;
import com.mariammuhammad.iftarplanner.Views.plan.SpecificMealListener;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteView, RemoveListener, SpecificMealListener {
    RecyclerView favoriteRecyclerView;
    private FavoritePresenter favoritePresenter;

    private FavoriteAdapter favoriteAdapter;
    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favoriteRecyclerView =view.findViewById(R.id.AllFavRecyclerView);

        favoritePresenter= new FavoritePresenter(this,Repository.getInstance(
                MealLocalDataSource.getInstance(requireContext()),
                MealRemoteDataSource.getInstance(),
                CategoriesRemoteDataSource.getInstance(),
                CountriesRemoteDataSource.getInstance(),
                IngredientsRemoteDataSource.getInstance())
        );
        favoritePresenter.getAllFavoriteMeals();

        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));


    }

    @Override
    public void showDataSuccess(List<MealStorage> mealStorage) {
        if (mealStorage.isEmpty()) {
            favoriteRecyclerView.setVisibility(View.GONE);
        } else {
            favoriteRecyclerView.setVisibility(View.VISIBLE);
             favoriteAdapter = new FavoriteAdapter(requireContext(), mealStorage, this);
            favoriteRecyclerView.setAdapter(favoriteAdapter);
            favoriteAdapter.specificMealListener=this::onMealClick; //
        }

    }

    @Override
    public void displaySuccess(MealStorage mealStorage) {
        favoritePresenter.getAllFavoriteMeals();

    }

    @Override
    public void showError(String message) {
        Log.i("TAG", "showError: "+ message);

    }

    @Override
    public void onMealDelete(MealStorage mealStorage) {
        favoritePresenter.deleteMealFromFavourite(mealStorage);
        favoritePresenter.deleteData(mealStorage);
        displaySuccess(mealStorage);
        showSnackBar("Meal removed from favourites");
    }

    private void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        int color = ContextCompat.getColor(requireContext(), R.color.primary_dark);
        snackbarView.setBackgroundTintList(ColorStateList.valueOf(color));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }


    @Override
    public void onMealClick(int id, Meal meal) {
        NavController navController = Navigation.findNavController(requireView());
        FavoriteFragmentDirections.ActionFavoriteFragmentToItemInfoFragment action=
                FavoriteFragmentDirections.actionFavoriteFragmentToItemInfoFragment(id,meal);
        navController.navigate(action);

    }
}

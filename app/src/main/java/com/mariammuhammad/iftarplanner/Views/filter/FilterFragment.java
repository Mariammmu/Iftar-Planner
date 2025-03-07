package com.mariammuhammad.iftarplanner.Views.filter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.Local.MealLocalDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Categories.CategoriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Countries.CountriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Ingredients.IngredientsRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals.MealRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Presenter.filter.FilterPresenter;
import com.mariammuhammad.iftarplanner.R;

import java.util.ArrayList;


public class FilterFragment extends Fragment implements FilteringView{
    private RecyclerView mealRecyclerView;
    private FilterAdapter filterAdapter;

    FilterPresenter filterPresenter;
    LottieAnimationView lottieAnimationView;

    public FilterFragment() {
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
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lottieAnimationView = view.findViewById(R.id.waitingAnimation);

        mealRecyclerView = view.findViewById(R.id.mealFilteringRecyclerView);
        mealRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));


         filterPresenter = new FilterPresenter(this, Repository.getInstance(
                 MealLocalDataSource.getInstance(requireContext()),
                 MealRemoteDataSource.getInstance(),
                 CategoriesRemoteDataSource.getInstance(),
                 CountriesRemoteDataSource.getInstance(),
                 IngredientsRemoteDataSource.getInstance())
         );
        FilterFragmentArgs args = FilterFragmentArgs.fromBundle(getArguments());

        //String id = args.getMealId();
        //String type = args.getType();
        loadMeals(args.getType(), args.getMealId());


    }
    private void loadMeals(String type, String id) {

        switch (type){
            case "category":
            filterPresenter.getMealsByCategory(id);
            break;
            case "country":
                filterPresenter.getMealsByCountry(id);
                break;
            case "ingredient":
                filterPresenter.getMealsByIngredient(id);
                break;
            default:
                Log.e("TAG", "Invalid type: " + type);
        }

    }

    private void showMeals(ArrayList<Meal> meals) {
        if (meals == null || meals.isEmpty()) {
            showError("Sorry, no meals found");
            return;
        }
        filterAdapter = new FilterAdapter(requireContext(), meals, meal -> navigateToItemInfo(meal));// this::navigateToItemInfo);
        mealRecyclerView.setAdapter(filterAdapter);
    }

    private void navigateToItemInfo(int mealId) {
        NavController navController= Navigation.findNavController(requireView());
        String mealName = "Default Meal";
        FilterFragmentDirections.ActionFilterFragmentToItemInfoFragment action=
                FilterFragmentDirections.actionFilterFragmentToItemInfoFragment(mealId, null);

        navController.navigate(action);
    }
    @Override
    public void showMealsByCategory(ArrayList<Meal> meals) {
        showMeals(meals);

    }




    @Override
    public void showMealsByCountry(ArrayList<Meal> meals) {
        showMeals(meals);

    }

    @Override
    public void showMealsByIngredient(ArrayList<Meal> meals) {
        showMeals(meals);

    }

    @Override
    public void showError(String errorMsg) {
        Log.i("TAG", "showError: "+errorMsg);
    }

    @Override
    public void showLoading() {
        lottieAnimationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        lottieAnimationView.setVisibility(View.GONE);


    }
}
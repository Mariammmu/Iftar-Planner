package com.mariammuhammad.iftarplanner.Views.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mariammuhammad.iftarplanner.Common.NetworkConnectionListener;
import com.mariammuhammad.iftarplanner.Model.DTO.Category;
import com.mariammuhammad.iftarplanner.Model.DTO.Country;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.Local.MealLocalDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Categories.CategoriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Countries.CountriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Ingredients.IngredientsRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals.MealRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Presenter.home.HomePresenter;
import com.mariammuhammad.iftarplanner.R;
import com.mariammuhammad.iftarplanner.Views.MainActivity;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements HomeView, NetworkConnectionListener {

   private RecyclerView randomMealRecycler, categoryRecycler, countryRecycler;
    private HomePresenter homePresenter;
    
    LottieAnimationView lottieAnimationView;

    BottomNavigationView bottomNavigationView;
    NestedScrollView nestedScrollView;

    CategoryAdapter categoryAdapter;

    public HomeFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        randomMealRecycler = view.findViewById(R.id.recycler_view_random);
        categoryRecycler = view.findViewById(R.id.recycler_view_category);
        countryRecycler=view.findViewById(R.id.recyclerViewCountries);
        lottieAnimationView = view.findViewById(R.id.no_internet_image);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        nestedScrollView=view.findViewById(R.id.nestedScroller);



        randomMealRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        countryRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        // countryRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>(), categoryName -> {
            HomeFragmentDirections.ActionHomeFragmentToFilterFragment action =
                    HomeFragmentDirections.actionHomeFragmentToFilterFragment(categoryName, "category");

            Navigation.findNavController(requireView()).navigate(action);
        });

        categoryRecycler.setAdapter(categoryAdapter);



        categoryRecycler.setAdapter(categoryAdapter);



        Repository repository = Repository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteDataSource.getInstance(), CategoriesRemoteDataSource.getInstance(), CountriesRemoteDataSource.getInstance(), IngredientsRemoteDataSource.getInstance());
        homePresenter = new HomePresenter(this, repository, requireContext());



        ((MainActivity) requireActivity()).setNetworkStateListener(this);

        loadHomeData();

    }


    private void loadHomeData() {
        homePresenter.getRandomMeals();
        homePresenter.getAllCategories();
        homePresenter.getAllCountries();
        homePresenter.getDataFromFirebase();
    }


    @Override
    public void showMealOfTheDay(ArrayList<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            DailyInspirationAdapter dailyInspirationAdapter = new DailyInspirationAdapter(getContext(), meals);
            randomMealRecycler.setAdapter(dailyInspirationAdapter);
            dailyInspirationAdapter.setOnItemClickListener(meal ->

//                    Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_itemInfoFragment));
                    navigateToMealDetails(Integer.parseInt(meal.idMeal), meal));

        } else {
            showError("No meals found");
        }
    }

    private void navigateToMealDetails(int mealId, Meal meal){
        if(getView()!=null){
            Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToItemInfoFragment(mealId,meal));
        }
    }

    public void showCategories(ArrayList<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            Log.i("Category Debug", "Categories received: " + categories.size());

            categoryAdapter.setList(categories);
            categoryAdapter.notifyDataSetChanged();
        } else {
            showError("No categories found");
        }
    }

    @Override
    public void showCountries(ArrayList<Country> countries) {
        if (countries != null && !countries.isEmpty()) {
            Log.d("HomeFragment", "Countries received: " + countries.size());

            CountryAdapter countryAdapter = new CountryAdapter(getContext(), countries, countryName -> {
             HomeFragmentDirections.ActionHomeFragmentToFilterFragment action= HomeFragmentDirections.actionHomeFragmentToFilterFragment(countryName, "country");
                Navigation.findNavController(getView()).navigate(action);
            });
            Log.d("HomeFragment", "Countries received: " + countries.size());
            countryRecycler.setAdapter(countryAdapter);
            countryAdapter.notifyDataSetChanged();
        } else {
            showError("No countries found");
        }
    }

    @Override
    public void showError(String errorMessage) {
        Log.i("TAG", "showError: "+errorMessage);

    }

    @Override
    public void showLoading() {
        nestedScrollView.setVisibility(View.GONE);
        lottieAnimationView.setVisibility(View.VISIBLE);
       // nestedScrollView.post(() -> nestedScrollView.fullScroll(View.FOCUS_DOWN));
    }

    @Override
    public void hideLoading() {
        nestedScrollView.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.GONE);
    }

    @Override
    public void onLogoutSuccess() {
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_welcomeFragment);


    }

    @Override
    public void onNetworkAvailable() {
        loadHomeData();

    }

    @Override
    public void onNetworkLost() {
        showError("Network is not available");
        hideLoading();
       // lottieAnimationView.setVisibility(View.VISIBLE);

    }
}
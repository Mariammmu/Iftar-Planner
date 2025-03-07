package com.mariammuhammad.iftarplanner.Views.plan;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.Local.MealLocalDataSource;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Remote.Categories.CategoriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Countries.CountriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Ingredients.IngredientsRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals.MealRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Presenter.plan.PlanPresenter;
import com.mariammuhammad.iftarplanner.R;
import com.mariammuhammad.iftarplanner.Views.favorite.FavoriteAdapter;
import com.mariammuhammad.iftarplanner.Views.favorite.RemoveListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PlanFragment extends Fragment implements PlanView, RemoveListener {
    private CalendarView calendarView;
    private RecyclerView planRecyclerView;
    private PlanAdapter planAdapter;
    private PlanPresenter presenter;
    SpecificMealListener specificMealListener;
    private long selectedDateInMillis = System.currentTimeMillis();

    public PlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        planRecyclerView = view.findViewById(R.id.recyclerView);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        planRecyclerView.setLayoutManager(layoutManager);

        calendarView.setMinDate(System.currentTimeMillis());
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            selectedDateInMillis = calendar.getTimeInMillis();
        });


        presenter= new PlanPresenter(requireContext(),this,Repository.getInstance(
                MealLocalDataSource.getInstance(requireContext()),
                MealRemoteDataSource.getInstance(),
                CategoriesRemoteDataSource.getInstance(),
                CountriesRemoteDataSource.getInstance(),
                IngredientsRemoteDataSource.getInstance())
        );

        presenter.getAllMealsFromPlan();
    }


    @Override
    public void showMeals(List<MealStorage> mealStorages) {
            if(mealStorages.isEmpty()){
                planRecyclerView.setVisibility(View.GONE);
            }else{
                planRecyclerView.setVisibility(View.VISIBLE);
                planAdapter= new PlanAdapter(requireContext(),mealStorages,specificMealListener);
                planRecyclerView.setAdapter(planAdapter);
                planAdapter.specificMealListener =new SpecificMealListener() {
                    @Override
                    public void onMealClick(int id, Meal meal) {
                        NavController navController = Navigation.findNavController(requireView());
                        PlanFragmentDirections.ActionPlanFragmentNavToItemInfoFragment action =
                                PlanFragmentDirections.actionPlanFragmentNavToItemInfoFragment(id, meal);
                        navController.navigate(action);
                    }
                };
                //important // don't forget it
                  planAdapter.removeListener=this::onMealDelete;
            }
    }

    @Override
    public void displayMeal(MealStorage mealStorage) {
        presenter.getAllMealsFromPlan();
    }

//    @Override
//    public void showEmptyCalendar() {}
//
//    @Override
//    public void showEmptyDay() {}

    @Override
    public void showError(String message) {
        Log.i("TAG", "showError: " + message);
    }



    @Override
    public void onMealDelete(MealStorage mealStorage) {
        presenter.deleteMealFromCalendar(mealStorage);
        presenter.deleteData(mealStorage);
        displayMeal(mealStorage);
        showSnackBar("Meal removed from planned meals");
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
}

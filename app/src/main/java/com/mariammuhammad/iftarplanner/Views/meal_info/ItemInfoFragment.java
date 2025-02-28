package com.mariammuhammad.iftarplanner.Views.meal_info;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.Local.MealLocalDataSource;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.Model.Remote.Categories.CategoriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Countries.CountriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Ingredients.IngredientsRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals.MealRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Presenter.meal_info.MealInfoPresenter;
import com.mariammuhammad.iftarplanner.R;

public class ItemInfoFragment extends Fragment implements MealInfoView {

    ItemInfoFragment binding;
    IngredientsAdapter adapter;
    MealInfoPresenter presenter;
    private LottieAnimationView mealImage;
    private TextView mealName, steps, mealCountry;
    private WebView videoWebView;
    private RecyclerView ingredientsRecycler;
    private SharedPreferences sharedPreferences;
    private Boolean isFavourite = false;
    private Boolean isPlan = false;
    private ImageButton imageButtonFavourite, imageButtonPlan;


    public ItemInfoFragment() {
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
        return inflater.inflate(R.layout.fragment_item_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mealImage=view.findViewById(R.id.lottie);
         mealName=view.findViewById(R.id.meal_name);
         mealCountry=view.findViewById(R.id.countryName);
         steps=view.findViewById(R.id.instructions_txt);
         videoWebView=view.findViewById(R.id.video);
         imageButtonFavourite=view.findViewById(R.id.btnFav);
         imageButtonPlan=view.findViewById(R.id.btn_calenar);

         sharedPreferences=getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        ingredientsRecycler= view.findViewById(R.id.ingrediant_recycler);

        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


//        isFavourite = MealDetailsFragmentArgs.fromBundle(getArguments()).getIsFavourite();
//        isPlan = MealDetailsFragmentArgs.fromBundle(getArguments()).getIsPlanned();

        presenter= new MealInfoPresenter(this,Repository.getInstance(MealLocalDataSource.getInstance(requireContext()), MealRemoteDataSource.getInstance(), CategoriesRemoteDataSource.getInstance(), CountriesRemoteDataSource.getInstance(), IngredientsRemoteDataSource.getInstance()),requireContext());

//        Meal meal = MealDetailsFragmentArgs.fromBundle(getArguments()).getRandomMeal();
//        int mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getId();

//        if (meal != null) {
//            displayMealDetails(meal);
//        } else {
//            presenter.getMealDetailsById(m);
//        }
        updateSaveButtonState(isFavourite);
        updatePlanButtonState(isPlan);
    }






    private void updateSaveButtonState(boolean isSaved) {
        if (isSaved) {
            imageButtonFavourite.setImageResource(R.drawable.bookmark);
        } else {
            imageButtonFavourite.setImageResource(R.drawable.fav);
        }
    }

    private void updatePlanButtonState(boolean isPlanned) {
        if (isPlanned) {
            imageButtonPlan.setEnabled(false);
            imageButtonPlan.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.accent));
        } else {
            imageButtonPlan.setEnabled(true);
            imageButtonPlan.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_dark));
        }
    }

    @Override
    public void showMealDetailsById(Meal meal) {

    }

    @Override
    public void showSuccessMessage(String message) {

    }

    @Override
    public void deleteMealFromFavourite(MealStorage mealStorage) {

    }

    @Override
    public void showError(String message) {

    }
}
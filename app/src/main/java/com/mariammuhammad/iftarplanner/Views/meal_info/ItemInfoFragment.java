package com.mariammuhammad.iftarplanner.Views.meal_info;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
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
import com.mariammuhammad.iftarplanner.Presenter.meal_info.MealInfoPresenter;
import com.mariammuhammad.iftarplanner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ItemInfoFragment extends Fragment implements MealInfoView {

    IngredientsAdapter adapter;
    MealInfoPresenter presenter;
    private LottieAnimationView mealImage;
    private TextView mealName, steps, mealCountry, mealCategory;
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

        mealImage = view.findViewById(R.id.lottieWait);
        mealName = view.findViewById(R.id.meal_name);
        mealCountry = view.findViewById(R.id.areaNameItem);
        mealCategory=view.findViewById(R.id.category_name);
        steps = view.findViewById(R.id.instructions_txt);
        videoWebView = view.findViewById(R.id.video);
        imageButtonFavourite = view.findViewById(R.id.btnFav);
        imageButtonPlan = view.findViewById(R.id.btn_calendar);
        ingredientsRecycler = view.findViewById(R.id.ingredient_recycler);
        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        presenter = new MealInfoPresenter(this, Repository.getInstance(
                MealLocalDataSource.getInstance(requireContext()),
                MealRemoteDataSource.getInstance(),
                CategoriesRemoteDataSource.getInstance(),
                CountriesRemoteDataSource.getInstance(),
                IngredientsRemoteDataSource.getInstance()),
                requireContext()
        );



        isFavourite = ItemInfoFragmentArgs.fromBundle(getArguments()).getIsFavourite();
        isPlan = ItemInfoFragmentArgs.fromBundle(getArguments()).getIsPlanned();

        Meal meal = ItemInfoFragmentArgs.fromBundle(getArguments()).getRandomMeal();
        int mealId = ItemInfoFragmentArgs.fromBundle(getArguments()).getMealId();
        Log.i("TAG", "onViewCreated: "+mealId);

        if (meal != null) {
            displayMealDetails(meal);
        } else {
            presenter.getMealDetailsById(mealId);
        }
        updateSaveButtonState(isFavourite);
        updatePlanButtonState(isPlan);
    }

    private void displayMealDetails(Meal meal) {
        Glide.with(getContext()).load(meal.strMealThumb).placeholder(R.drawable.load).into(mealImage);
        mealName.setText(meal.strMeal);
        mealCountry.setText(meal.strArea);
        mealCategory.setText(meal.strCategory);
        steps.setText(meal.strInstructions);
        IngredientsAdapter adapter = new IngredientsAdapter(getContext(), meal.getIngredients(), meal.getMeasurements());
        ingredientsRecycler.setAdapter(adapter);
        loadYouTubeVideo(meal.strYoutube);

        updateSaveButtonState(isFavourite);
        updatePlanButtonState(isPlan);

        imageButtonFavourite.setOnClickListener(v -> {
            String userId = sharedPreferences.getString("userId", null);
            if (userId != null && !userId.isEmpty()) {
                if (isFavourite) {
                   MealStorage mealStorage = new MealStorage(false, true, meal, "Favourite", userId, meal.idMeal);
                    presenter.deleteMealFromFavourite(mealStorage);
                   updateSaveButtonState(false);
                    isFavourite = false;
                } else {
                    MealStorage mealStorage = new MealStorage(false, true, meal, "Favourite", userId, meal.idMeal);
                    presenter.addToFavourite(mealStorage);
                    presenter.sendData(mealStorage);
                    //updateSaveButtonState(true);
                    isFavourite = true;
                }
                updateSaveButtonState(isFavourite);
            } else {
                Toast.makeText(getContext(), "Please login to add to favourites", Toast.LENGTH_SHORT).show();
            }
        });

        imageButtonPlan.setOnClickListener(v -> {
            showDatePicker(meal);
        });
    }


    private void loadYouTubeVideo(String youtubeUrl) {
        videoWebView.getSettings().setJavaScriptEnabled(true);
        videoWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        videoWebView.setWebChromeClient(new WebChromeClient());
        Uri uri = Uri.parse(youtubeUrl);
        String videoId = uri.getQueryParameter("v");

        if (videoId != null) {
            String iframe = "<iframe width=\"100%\" height=\"100%\" " +
                    "src=\"https://www.youtube.com/embed/" + videoId + "?autoplay=0&mute=0\" " +
                    "frameborder=\"0\" allowfullscreen></iframe>";

            videoWebView.loadData(iframe, "text/html", "utf-8");
        } else {
            videoWebView.setVisibility(View.GONE);
        }
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
         //   imageButtonPlan.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.));
        }
    }

    @Override
    public void showMealDetailsById(Meal meal) {
        if (meal != null) {
            displayMealDetails(meal);
        } else {
            showError(getString(R.string.not_have_account)); //change it to no meal found
        }
    }

    @Override
    public void showSuccessMessage(String message) {
        showSnackBar(message);

    }

    @Override
    public void deleteMealFromFavourite(MealStorage mealStorage) {
        presenter.deleteMealFromFavourite(mealStorage);

    }

    @Override
    public void showError(String message) {
        Log.e("ItemInfoFragment", "Error: " + message);

    }

    private void showDatePicker(Meal meal) {
        Calendar calendar = Calendar.getInstance();
        long startOfWeek = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_WEEK, 7);
        long endOfWeek = calendar.getTimeInMillis();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(startOfWeek);
        constraintsBuilder.setEnd(endOfWeek);
        constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                return date >= startOfWeek && date <= endOfWeek;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel parcel, int i) {
            }
        });

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a day this week")
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePicker.show(getParentFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar selectedCalendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Cairo"));
            selectedCalendar.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String selectedDate = format.format(selectedCalendar.getTime());
            saveMealToPlan(meal, selectedDate);
        });
    }


    private void saveMealToPlan(Meal meal, String selectedDate) {
        String userId = sharedPreferences.getString("userId", null);
        if (userId != null && !userId.isEmpty()) {
            String formattedDate = formatDate(selectedDate); // Use the helper method to format the date
            MealStorage mealStorage = new MealStorage(true, false, meal, formattedDate, userId, meal.idMeal);
            presenter.addToPlan(mealStorage);
            presenter.sendData(mealStorage);
            updatePlanButtonState(isPlan);
            imageButtonPlan.setEnabled(false);
            isPlan = true;
        }
    }

    private String formatDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Adjust input format if needed
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()); // Desired output format

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate;
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        int color = ContextCompat.getColor(requireContext(), R.color.primary_dark);
        snackbarView.setBackgroundTintList(ColorStateList.valueOf(color));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }


}
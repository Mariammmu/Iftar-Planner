package com.mariammuhammad.iftarplanner.Views.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.mariammuhammad.iftarplanner.Common.NetworkConnectionListener;
import com.mariammuhammad.iftarplanner.Model.DTO.Category;
import com.mariammuhammad.iftarplanner.Model.DTO.Country;
import com.mariammuhammad.iftarplanner.Model.DTO.Ingredient;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.Local.MealLocalDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Categories.CategoriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Countries.CountriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Ingredients.IngredientsRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals.MealRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Presenter.NetworkConnection;
import com.mariammuhammad.iftarplanner.Presenter.filter.FilterPresenter;
import com.mariammuhammad.iftarplanner.Presenter.search.SearchPresenter;
import com.mariammuhammad.iftarplanner.R;
import com.mariammuhammad.iftarplanner.Views.filter.FilterListener;
import com.mariammuhammad.iftarplanner.Views.filter.FilteringView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchFragment extends Fragment implements SearchView, SearchListener, NetworkConnectionListener {
    private TextInputLayout textInputLayout;
    private EditText searchEditText;
    private RecyclerView searchRecycler;
    private ChipGroup chipGroup;
    private Chip ingredientChip, categoryChip, countryChip;
    private ConstraintLayout noInternetLayout;
    private NetworkConnection networkConnection;
    private SearchPresenter presenter;
    private SearchAdapter searchAdapter;
    private FilterPresenter filterPresenter;
    TextView tvemptySearch;
    ImageView noSearchImage;
    private final ArrayList<Object> allItems = new ArrayList<>();
    private final ArrayList<Object> filteredItems = new ArrayList<>();
    private boolean isCategory = false, isIngredient = false, isCountry = false;
    private String selectedFilterName = null;

    private CompositeDisposable disposable = new CompositeDisposable();


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textInputLayout = view.findViewById(R.id.search_box_layout);
        searchEditText = textInputLayout.getEditText();
        searchRecycler = view.findViewById(R.id.search_recycler);
        searchRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        noInternetLayout = view.findViewById(R.id.noInternet_layout_search);
        noSearchImage=view.findViewById(R.id.searchImg);
        tvemptySearch=view.findViewById(R.id.emptySearch);

        chipGroup = view.findViewById(R.id.chipGroup);
        ingredientChip = view.findViewById(R.id.chipIngredients);
        categoryChip = view.findViewById(R.id.chipCategories);
        countryChip = view.findViewById(R.id.chipCountry);





        presenter = new SearchPresenter(this, Repository.getInstance(
                MealLocalDataSource.getInstance(requireContext()),
                MealRemoteDataSource.getInstance(),
                CategoriesRemoteDataSource.getInstance(),
                CountriesRemoteDataSource.getInstance(),
                IngredientsRemoteDataSource.getInstance())
        );

        networkConnection = new NetworkConnection(requireContext(), this);

        searchAdapter = new SearchAdapter(getContext(), filteredItems, item -> {
            if (item instanceof Ingredient) {
                navigateToMealFilter(((Ingredient) item).getStrIngredient(), "ingredient");
            } else if (item instanceof Category) {
                navigateToMealFilter(((Category) item).getStrCategory(), "category");
            } else if (item instanceof Country) {
                navigateToMealFilter(((Country) item).getStrArea(), "country");
            } else if (item instanceof Meal) {
               onItemClick((Meal)item);
            }
        });
        searchRecycler.setAdapter(searchAdapter);
        visibility(View.GONE, View.VISIBLE);

        tvemptySearch.setVisibility(View.GONE);
        noSearchImage.setVisibility(View.GONE);


        fetchAllData();
        setupChipListeners();
        setupSearchObservable();
        setupSearchObservable();
    }

    private void fetchAllData() {
        allItems.clear();
        filteredItems.clear();

        if (isCategory) {
            presenter.getCategories();
        } else if (isIngredient) {
            presenter.getIngredients();
        } else if (isCountry) {
            presenter.getCountries();
        } else {
            presenter.getMealByName(searchEditText.getText().toString().trim());
        }
    }

    private void setupChipListeners() {
        categoryChip.setOnClickListener(v -> {
            selectedFilterName = null;
            isCategory = true;
            isIngredient = false;
            isCountry = false;
            presenter.getCategories();
        });

        ingredientChip.setOnClickListener(v -> {
            selectedFilterName = null;
            isCategory = false;
            isIngredient = true;
            isCountry = false;
            presenter.getIngredients();
        });

        countryChip.setOnClickListener(v -> {
            selectedFilterName = null;
            isCategory = false;
            isIngredient = false;
            isCountry = true;
            presenter.getCountries();
        });
    }

    private void setupSearchObservable() {
        if (searchEditText == null) return;

        disposable.add(
                Observable.create((ObservableEmitter<String> emitter) ->
                                searchEditText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                    @Override
                                    public void afterTextChanged(Editable s) {}

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        emitter.onNext(s.toString());
                                    }
                                })
                        )
                        .debounce(350, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::filterData, throwable -> Log.e("SearchFragment", "Error in search", throwable))
        );
    }
    private void filterData(String query) {
        query = query.toLowerCase();
        filteredItems.clear();

//        if (query.isEmpty()) {
//            visibility(View.GONE, View.GONE);  // Hide both meals and "No Results"
//            searchAdapter.updateSearch(new ArrayList<>());
//            return;
//        }
        Log.d("SearchFragment", "Filtering for query: " + query);
        Log.d("SearchFragment", "Current filter: " + (isCategory ? "Category" : isIngredient ? "Ingredient" : "Country"));

        HashSet<Object> uniqueItems = new HashSet<>(); //3ashan prevent duplicate data

        for (Object item : allItems) {
            boolean matchesQuery = false;

            if (item instanceof Ingredient && isIngredient) {
                matchesQuery = ((Ingredient) item).getStrIngredient().toLowerCase().contains(query);
            } else if (item instanceof Category && isCategory) {
                matchesQuery = ((Category) item).getStrCategory().toLowerCase().contains(query);
            } else if (item instanceof Country && isCountry) {
                matchesQuery = ((Country) item).getStrArea().toLowerCase().contains(query);
            } else if (item instanceof Meal && !isCategory && !isIngredient && !isCountry) {
                matchesQuery = ((Meal) item).strMeal.toLowerCase().contains(query);
            }
            if (matchesQuery) {
                uniqueItems.add(item);
            }
        }

        filteredItems.addAll(uniqueItems);

        // If searching for a meal, hide meals until user enters a query
        if (!isCategory && !isIngredient && !isCountry) {
            if (query.isEmpty()) {
                // Hide the meals list until the user searches
                visibility(View.GONE, View.GONE);
                searchAdapter.updateSearch(new ArrayList<>());
                return;
            } else if (filteredItems.isEmpty()) {
                // Show "No Results" if no meals match and query is not empty
                presenter.getMealByName(query);
                visibility(View.GONE, View.VISIBLE);
            } else {
                // Show meals when search results are available
                visibility(View.VISIBLE, View.GONE);
            }
        } else {
            // If filtering categories, ingredients, or countries, show them directly
            visibility(filteredItems.isEmpty() ? View.GONE : View.VISIBLE, View.GONE);
        }

        searchAdapter.updateSearch(filteredItems);
    }

    private void visibility(int visible, int gone) {
        searchRecycler.setVisibility(visible);
       tvemptySearch.setVisibility(gone);
      noSearchImage.setVisibility(gone);
    }

    private void navigateToMealFilter(String id, String type) {
        NavController navController = Navigation.findNavController(requireView());
        SearchFragmentDirections.ActionSearchFragmentToFilterFragment action =
                SearchFragmentDirections.actionSearchFragmentToFilterFragment(id, type);
        navController.navigate(action);
    }




    @Override
    public void showMeals(ArrayList<Meal> meals) {
        allItems.clear();
        allItems.addAll(meals);
        filterData(searchEditText.getText().toString());
    }

    @Override
    public void ShowIngredients(ArrayList<Ingredient> ingredients) {
        allItems.clear();
        allItems.addAll(ingredients);
        filterData(searchEditText.getText().toString());
    }

    @Override
    public void showCategories(ArrayList<Category> categories) {
        allItems.clear();
        allItems.addAll(categories);
        filterData(searchEditText.getText().toString());
    }

    @Override
    public void showCountries(ArrayList<Country> countries) {
        allItems.clear();
        allItems.addAll(countries);
        filterData(searchEditText.getText().toString());
    }

    @Override
    public void showError(String errorMsg) {
        Log.i("TAG", "showError: " + errorMsg);

    }

    @Override
    public void onNetworkAvailable() {

    }

    @Override
    public void onNetworkLost() {}

    @Override
    public void onItemClick(Object item) {
        //  SearchFragmentDirections.ActionSearchFragmentToItemInfoFragment action =
        //        SearchFragmentDirections.actionSearchFragmentToItemInfoFragment();
        // if (item instanceof Meal) {
        Meal meal = (Meal) item;
        //     NavController navController = Navigation.findNavController(requireView());
        SearchFragmentDirections.ActionSearchFragmentToItemInfoFragment action =
                SearchFragmentDirections.actionSearchFragmentToItemInfoFragment(Integer.parseInt(meal.idMeal), meal);

        Navigation.findNavController(requireView()).navigate(action);
    //}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clearDisposables();
    }
}


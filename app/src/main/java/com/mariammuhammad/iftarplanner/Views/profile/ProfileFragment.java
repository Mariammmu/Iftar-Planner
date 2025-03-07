package com.mariammuhammad.iftarplanner.Views.profile;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mariammuhammad.iftarplanner.Common.MySharedPrefs;
import com.mariammuhammad.iftarplanner.Common.NetworkConnectionListener;
import com.mariammuhammad.iftarplanner.Model.Local.MealLocalDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Categories.CategoriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Countries.CountriesRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.Ingredients.IngredientsRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Remote.RandomMeals.MealRemoteDataSource;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Presenter.NetworkConnection;
import com.mariammuhammad.iftarplanner.Presenter.profile.ProfilePresenter;
import com.mariammuhammad.iftarplanner.R;


public class ProfileFragment extends Fragment implements ProfileView, NetworkConnectionListener {

        TextView tvFavorite, tvPlan, tvName, tvChef;

        ImageView imageUser;
        View layoutNoInternet;
        Button btnLogout,btnGuest;

        NetworkConnection connectionPresenter;
        ProfilePresenter profilePresenter;

    public ProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutNoInternet=view.findViewById(R.id.noInternet_layout_profile);

        tvFavorite=view.findViewById(R.id.txtFavorite);
        tvPlan=view.findViewById(R.id.txtPlanned);
        tvName=view.findViewById(R.id.txtUserName);
        tvChef=view.findViewById(R.id.txtHelloChef);
        imageUser=view.findViewById(R.id.imageUser);
        btnGuest=view.findViewById(R.id.btnSignUpGuest);
        btnLogout=view.findViewById(R.id.btnLogout);
        profilePresenter= new ProfilePresenter(this,
                Repository.getInstance(MealLocalDataSource.getInstance(requireContext()),
                        MealRemoteDataSource.getInstance(),
                        CategoriesRemoteDataSource.getInstance(),
                        CountriesRemoteDataSource.getInstance(),
                        IngredientsRemoteDataSource.getInstance()));

        connectionPresenter= new NetworkConnection(requireContext(),this);

        btnGuest.setOnClickListener(view1 -> { //I updated that
            Navigation.findNavController(view)
                    .navigate(R.id.action_profileFragment_to_welcomeFragment);
            tvPlan.setVisibility(GONE);
            tvFavorite.setVisibility(GONE);

        });
        btnLogout.setOnClickListener(view1 -> {
            showLogoutConfirmationDialog();
            });

        tvFavorite.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_favoriteFragment);
        });

        tvPlan.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_planFragmentNav);

        });
    }

    @Override
    public void signOut() {

        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_welcomeFragment);
        Toast.makeText(getContext(),"Logged out Successfully", Toast.LENGTH_SHORT).show();

    }

    private boolean isGuestUser() {
        // SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = MySharedPrefs.getInstance().getString("userId", "");
        return "guest".equals(userId);
    }


    private void showLogoutConfirmationDialog() {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout Confirmation")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialogInterface, which) -> {
                    profilePresenter.logout();
                })
                .setNegativeButton("No", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.show();

        if (dialog.getWindow() != null) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor("#19374F"));
            drawable.setCornerRadius(16f);
            dialog.getWindow().setBackgroundDrawable(drawable);
        }
    }


    @Override
    public void setUserName(String userName) {

        tvName.setText(userName);
    }


    @Override
    public void onNetworkAvailable() {
        if(profilePresenter.isUserLoggedOut()||isGuestUser()){
            layoutNoInternet.setVisibility(GONE);
            tvFavorite.setVisibility(GONE);
            tvPlan.setVisibility(GONE);
            tvName.setVisibility(GONE);
            btnLogout.setVisibility(GONE);
            btnGuest.setVisibility(GONE);
        }else {
            layoutNoInternet.setVisibility(GONE);
            tvFavorite.setVisibility(VISIBLE);
            tvPlan.setVisibility(VISIBLE);
            tvName.setVisibility(VISIBLE);
            btnLogout.setVisibility(VISIBLE);
            imageUser.setVisibility(VISIBLE);
            tvChef.setVisibility(VISIBLE);
            btnGuest.setVisibility(GONE);
        }
        if(isGuestUser()){
            btnGuest.setVisibility(VISIBLE);
        }


    }

    @Override
    public void onNetworkLost() {
        layoutNoInternet.setVisibility(VISIBLE);
        tvFavorite.setVisibility(GONE);
        tvPlan.setVisibility(GONE);
        tvName.setVisibility(GONE);
        imageUser.setVisibility(GONE);
        tvChef.setVisibility(GONE);
        btnGuest.setVisibility(GONE);
        btnLogout.setVisibility(GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        profilePresenter.clearDisposable();
    }
}
package com.mariammuhammad.iftarplanner.Views.profile;

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
import android.widget.TextView;
import android.widget.Toast;

import com.mariammuhammad.iftarplanner.Common.NetworkConnectionListener;
import com.mariammuhammad.iftarplanner.Presenter.NetworkConnection;
import com.mariammuhammad.iftarplanner.Presenter.profile.ProfilePresenter;
import com.mariammuhammad.iftarplanner.R;


public class ProfileFragment extends Fragment implements ProfileView, NetworkConnectionListener {

        TextView tvFavorite, tvPlan, tvName;

        View layoutNoInternet;
        Button btnLogout;

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
        layoutNoInternet=view.findViewById(R.id.noInternet_layout);

        tvFavorite=view.findViewById(R.id.txtFavorite);
        tvPlan=view.findViewById(R.id.txtPlanned);
        tvName=view.findViewById(R.id.txtUserName);
        btnLogout=view.findViewById(R.id.btnLogout);
        profilePresenter= new ProfilePresenter(this,
                requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE));

        connectionPresenter= new NetworkConnection(requireContext(),this);

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
            drawable.setColor(Color.parseColor("#FDFDFD"));
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
        if(profilePresenter.isUserLoggedOut()){
            layoutNoInternet.setVisibility(View.GONE);
            tvFavorite.setVisibility(View.GONE);
            tvPlan.setVisibility(View.GONE);
            tvName.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }else {
            layoutNoInternet.setVisibility(ViewGroup.GONE);
            tvFavorite.setVisibility(View.VISIBLE);
            tvPlan.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNetworkLost() {
        layoutNoInternet.setVisibility(ViewGroup.VISIBLE);
        tvFavorite.setVisibility(View.GONE);
        tvPlan.setVisibility(View.GONE);
        tvName.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
    }
}
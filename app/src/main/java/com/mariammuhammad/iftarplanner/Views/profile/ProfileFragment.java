package com.mariammuhammad.iftarplanner.Views.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mariammuhammad.iftarplanner.Common.NetworkConnectionListener;
import com.mariammuhammad.iftarplanner.Presenter.profile.ProfilePresenter;
import com.mariammuhammad.iftarplanner.R;


public class ProfileFragment extends Fragment implements ProfileView, NetworkConnectionListener {

        TextView favorite, plan;
        Button logout;
        ProfilePresenter presenter;
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
    public void logout() {

    }

    @Override
    public void setUserName(String userName) {

    }

    @Override
    public void onNetworkAvailable() {

    }

    @Override
    public void onNetworkLost() {

    }
}
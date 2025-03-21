package com.mariammuhammad.iftarplanner.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mariammuhammad.iftarplanner.R;
import com.mariammuhammad.iftarplanner.Views.authentication.google.WelcomeFragmentDirections;


public class WelcomeFragment extends Fragment {

Button  btnSignUp, btnLogin, btnGoogle;
TextView txtGuest;
    public WelcomeFragment() {
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
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.main);

        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnGoogle = view.findViewById(R.id.btnGoogle);
        txtGuest = view.findViewById(R.id.txtGuest);

        btnLogin.setOnClickListener(v -> navController.navigate(R.id.action_welcomeFragment_to_signinFragment));
        btnGoogle.setOnClickListener(v -> navController.navigate(R.id.action_welcomeFragment_to_homeFragment));
        txtGuest.setOnClickListener(v -> navController.navigate(R.id.action_welcomeFragment_to_homeFragment));
        btnSignUp.setOnClickListener(v -> navController.navigate(WelcomeFragmentDirections.actionWelcomeFragmentToSignupFragment()));

    }
}
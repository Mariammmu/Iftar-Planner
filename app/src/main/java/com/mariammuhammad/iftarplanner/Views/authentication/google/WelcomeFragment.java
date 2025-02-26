package com.mariammuhammad.iftarplanner.Views.authentication.google;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.mariammuhammad.iftarplanner.Presenter.authentication.google.GooglePresenter;
import com.mariammuhammad.iftarplanner.R;


public class WelcomeFragment extends Fragment implements GoogleView {

    Button btnSignUp, btnSignIn, btnGoogle;
    TextView txtGuest;
    private GoogleSignInClient googleSignInClient;
    Button guestBtn;
    private GooglePresenter googlePresenter;
    SharedPreferences sharedPreferences;

    private static final String TAG = "Welcome_Fragment";

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null && account.getIdToken() != null) {
                    googlePresenter.signInGoogle(account);
                } else {
                    this.successSignInGoogle("Google Sign-In succeeded");
                }
            } catch (ApiException e) {
                this.failSignInGoogle("Google Sign-In failed");
            }
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);


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
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignIn = view.findViewById(R.id.btnLogin);
        btnGoogle = view.findViewById(R.id.btnGoogle);
        txtGuest = view.findViewById(R.id.txtGuest);

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        googlePresenter = new GooglePresenter(this, requireContext());

        btnSignIn.setOnClickListener(v -> navController.navigate(R.id.action_welcomeFragment_to_signinFragment));

        btnSignUp.setOnClickListener(v -> navController.navigate(R.id.action_welcomeFragment_to_signupFragment));

        txtGuest.setOnClickListener(v -> {
            sharedPreferences.edit().putString("userId", "guest").apply();
            navController.navigate(R.id.action_welcomeFragment_to_homeFragment);
        });

        btnGoogle.setOnClickListener(v -> {
            signInWithGoogle();
        });

    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 123);
    }


    @Override
    public void successSignInGoogle(String successfulMessage) {
        showSnackBar("Login Successfully");
        Navigation.findNavController(getView()).navigate(R.id.action_welcomeFragment_to_homeFragment);

    }

    @Override
    public void failSignInGoogle(String errorMessage) {
        Log.i(TAG, "failSignInGoogle: ");
        showSnackBar("Sign In with Google Failed: " + errorMessage);

    }

    @Override
    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        int color = ContextCompat.getColor(requireContext(), R.color.primary_dark);
        snackbarView.setBackgroundTintList(ColorStateList.valueOf(color));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
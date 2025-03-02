package com.mariammuhammad.iftarplanner.Views.authentication.google;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.material.snackbar.Snackbar;
import com.mariammuhammad.iftarplanner.Presenter.authentication.google.GooglePresenter;
import com.mariammuhammad.iftarplanner.R;

public class WelcomeFragment extends Fragment implements GoogleView {

    private static final String TAG = "WelcomeFragment";
    private static final int RC_SIGN_IN = 123;

    private Button btnSignUp, btnSignIn, btnGoogle;
    private TextView txtGuest;
    private GooglePresenter googlePresenter;
    private NavController navController;

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    googlePresenter.handleGoogleSignInResult(result.getData());
                }
            });

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googlePresenter = new GooglePresenter(this, requireContext(), googleSignInLauncher);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignIn = view.findViewById(R.id.btnLogin);
        btnGoogle = view.findViewById(R.id.btnGoogle);
        txtGuest = view.findViewById(R.id.txtGuest);

        if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
            navigateToHome();
        }


        clickButtonListener();
    }

    private void clickButtonListener(){
        btnSignIn.setOnClickListener(v -> navigateToSignIn());
        btnSignUp.setOnClickListener(v -> navigateToSignUp());
        txtGuest.setOnClickListener(v -> googlePresenter.loginAsGuest());
        btnGoogle.setOnClickListener(v -> googlePresenter.signInWithGoogle());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        googlePresenter.detachView();
    }

    private void navigateToSignIn() {
        navController.navigate(R.id.action_welcomeFragment_to_signinFragment);
    }

    private void navigateToSignUp() {
        navController.navigate(R.id.action_welcomeFragment_to_signupFragment);
    }

    @Override
    public void successSignInGoogle(String successfulMessage) {
        showSnackBar(successfulMessage);
        if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
            navigateToHome();
        }    }

    @Override
    public void failSignInGoogle(String errorMessage) {
        Log.i(TAG, "failSignInGoogle: " + errorMessage);
        showSnackBar("Sign In with Google Failed: " + errorMessage);
    }

    @Override
    public void onGuestLoginSuccess() {
        showSnackBar("Logged in as Guest");
        navigateToHome();
    }

    private void navigateToHome() {
        navController.navigate(R.id.action_welcomeFragment_to_homeFragment);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        int color = ContextCompat.getColor(requireContext(), R.color.primary_dark);
        snackbarView.setBackgroundColor(color);
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}

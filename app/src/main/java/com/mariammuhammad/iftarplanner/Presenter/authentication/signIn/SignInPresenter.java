package com.mariammuhammad.iftarplanner.Presenter.authentication.signIn;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mariammuhammad.iftarplanner.Common.MySharedPrefs;
import com.mariammuhammad.iftarplanner.Views.authentication.signin.SignInView;
import com.mariammuhammad.iftarplanner.Views.authentication.signup.SignUpView;

public class SignInPresenter implements SignInContract {

    private final SignInView signInView;
    private final View view;

    public SignInPresenter(SignInView signInView, View view) {
        this.signInView = signInView;
        this.view = view;

    }


    @Override
    public void signIn(String email, String password) {
        if (!validateInputs(email, password)) {
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        signInView.successSignIn();

                        SharedPreferences.Editor editor = MySharedPrefs.getInstance().edit();

                        String userName = (currentUser.getDisplayName() != null) ? currentUser.getDisplayName() : "Unknown";
                        editor.putString("userId", currentUser.getUid());
                        editor.putString("userName", userName);
                        editor.putString("userEmail", currentUser.getEmail());
                        editor.apply();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("SignInError", "Error signing in: " + e.getMessage());
                    signInView.failSignIn(e.getMessage());
                });
    }
    private boolean validateInputs(String email, String password) {

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showSnackBar("Please enter a valid email address.");
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            showSnackBar("Password must be at least 6 characters.");
            return false;
        }

        return true;
    }

    private void showSnackBar(String message) {
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } else {
            Log.e("SignUpPresenter", "View is null, cannot show Snackbar.");
        }
    }
}
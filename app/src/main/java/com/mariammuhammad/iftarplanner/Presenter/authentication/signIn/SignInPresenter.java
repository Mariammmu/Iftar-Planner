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
import com.mariammuhammad.iftarplanner.Views.authentication.signin.SignInView;
import com.mariammuhammad.iftarplanner.Views.authentication.signup.SignUpView;

public class SignInPresenter implements SignInContract {

    private final SignInView signInView;
    private final FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    private final View view;

    public SignInPresenter(SignInView signInView, Context context, View view) {
        this.signInView = signInView;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.view = view;
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

    }


    @Override
    public void signIn(String email, String password) {
        if (!validateInputs(email, password)) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        signInView.successSignIn();
//                        sharedPreferences.edit().putString("userId", currentUser.getUid()).apply();
//                        sharedPreferences.edit().putString("userName", currentUser.getDisplayName()).apply();
//                        sharedPreferences.edit().putString("userEmail", currentUser.getEmail()).apply();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId", currentUser.getUid());

                        // Handle null display name
                        String userName = (currentUser.getDisplayName() != null) ? currentUser.getDisplayName() : "Unknown";
                        editor.putString("userName", userName);
                        editor.putString("userEmail", currentUser.getEmail());
                        editor.apply();
                    }
                })
                .addOnFailureListener(e -> signInView.failSignIn(e.getMessage()));
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
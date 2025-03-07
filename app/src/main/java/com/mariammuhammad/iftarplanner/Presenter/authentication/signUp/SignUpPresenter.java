package com.mariammuhammad.iftarplanner.Presenter.authentication.signUp;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mariammuhammad.iftarplanner.Views.authentication.signup.SignUpView;

public class SignUpPresenter implements SignUpContract {
    //it acts as a bridge between the View (SignUpView) and Firebase Authentication.
    private final SignUpView signUpView;


    public SignUpPresenter(SignUpView signUpView) {
        this.signUpView = signUpView;
    }

    @Override
    public void signUpWithEmail(String username, String email, String password, String confirmPassword) {
        if (!validateInputs(username, email, password, confirmPassword)) {
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(success -> updateUserProfile(username,email,password))
                .addOnFailureListener(error -> signUpView.onSignupFailure(error.getMessage()));
    }

    private boolean validateInputs(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty()) {
            showSnackBar("Username cannot be empty.");
            return false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showSnackBar("Please enter a valid email address.");
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            showSnackBar("Password must be at least 6 characters.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showSnackBar("Passwords do not match.");
            return false;
        }

        return true;
    }

    private void showSnackBar(String message) {
        signUpView.showSnackBar(message);
    }


    @Override
    public void updateUserProfile(String username, String email, String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnSuccessListener(unused -> {
                        Log.i("SignUpPresenter", "User profile updated successfully.");
                       // user.reload().addOnSuccessListener(reloaded -> {
                            signUpView.onSignupSuccess();
                       // });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("SignUpPresenter", "Profile update failed: " + e.getMessage());
                        signUpView.onSignupFailure(e.getMessage());
                    });

        } else {
            Log.e("SignUpPresenter", "User is null, cannot update profile.");
            signUpView.onSignupFailure("User not signed in.");
        }
    }
}





//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(username)
//                    .build();
//
//            user.updateProfile(profileUpdates)
//                    .addOnSuccessListener(unused -> Log.i("TAG", "updateUserProfile: "))
//                    .addOnFailureListener(e -> Log.i("TAG", "updateUserProfile: "));
//        }


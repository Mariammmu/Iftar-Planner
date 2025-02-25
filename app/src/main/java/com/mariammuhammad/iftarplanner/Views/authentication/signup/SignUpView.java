package com.mariammuhammad.iftarplanner.Views.authentication.signup;

public interface SignUpView {

    void onSignupSuccess();
    void onSignupFailure(String errorMessage);
    void showSnackBar(String message);
}

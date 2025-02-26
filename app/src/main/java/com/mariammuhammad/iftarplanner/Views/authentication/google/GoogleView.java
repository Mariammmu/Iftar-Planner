package com.mariammuhammad.iftarplanner.Views.authentication.google;

public interface GoogleView {
    void successSignInGoogle(String successfulMessage);
    void failSignInGoogle(String errorMessage);
    void showSnackBar(String message);
}

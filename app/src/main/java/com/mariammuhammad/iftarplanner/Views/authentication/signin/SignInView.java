package com.mariammuhammad.iftarplanner.Views.authentication.signin;

public interface SignInView {
    void successSignIn();
    void failSignIn(String errorMessage);
     void showSnackBar(String message);
}

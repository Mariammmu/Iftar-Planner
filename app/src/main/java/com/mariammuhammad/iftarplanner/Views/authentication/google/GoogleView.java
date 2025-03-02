package com.mariammuhammad.iftarplanner.Views.authentication.google;

import android.content.Intent;

public interface GoogleView {
    void successSignInGoogle(String successfulMessage);
    void failSignInGoogle(String errorMessage);
    void onGuestLoginSuccess();
    void showSnackBar(String message);
    //void startGoogleSignIn(Intent signInIntent, int requestCode);
}

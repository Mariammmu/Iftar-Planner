package com.mariammuhammad.iftarplanner.Presenter.authentication.google;

import android.content.Context;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.mariammuhammad.iftarplanner.Views.authentication.google.GoogleView;

public class GooglePresenter {
    private GoogleView view;
    private Context context;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    public GooglePresenter(GoogleView view, Context context, ActivityResultLauncher<Intent> googleSignInLauncher) {
        this.view = view;
        this.context = context;
        this.googleSignInLauncher = googleSignInLauncher;
        configureGoogleSignIn();
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    public void handleGoogleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(Exception.class);
            if (account != null) {
                view.successSignInGoogle("Google Sign-In successful!");
            }
        } catch (Exception e) {
            if (view != null) {
                view.failSignInGoogle(e.getMessage());
            }
        }
    }

    public void loginAsGuest() {
        if (view != null) {
            view.onGuestLoginSuccess();
        }
    }

    public void detachView() {
        this.view = null; // Prevent memory leaks
    }
}

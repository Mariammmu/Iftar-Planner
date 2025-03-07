package com.mariammuhammad.iftarplanner.Presenter.authentication.google;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mariammuhammad.iftarplanner.Views.authentication.google.GoogleView;

public class GooglePresenter implements GoogleContract {
    private final GoogleView view;
    private final FirebaseAuth myAuthentication;
    SharedPreferences sharedPreferences;


    public GooglePresenter(GoogleView view, Context context) {
        this.view = view;
        this.myAuthentication = FirebaseAuth.getInstance();
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }


    @Override
    public void signInGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        myAuthentication.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = myAuthentication.getCurrentUser();
                    if (user != null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId", user.getUid());
                        editor.putString("userEmail", user.getEmail() != null ? user.getEmail() : "No Email");
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userName", user.getDisplayName() != null ? user.getDisplayName() : "No Name");
                        editor.apply();
                        view.successSignInGoogle("Login successfully");
                    }
                })
                .addOnFailureListener(e ->{
                        Log.e("GoogleSignInError", "Signup Failed: ", e);

        view.failSignInGoogle("Signup Failed: " + e.getMessage());
    });
        }
}

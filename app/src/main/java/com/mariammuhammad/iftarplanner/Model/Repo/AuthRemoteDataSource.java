package com.mariammuhammad.iftarplanner.Model.Remote;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.rxjava3.core.Single;

public class AuthRemoteDataSource {

    private final FirebaseAuth firebaseAuth;

    public AuthRemoteDataSource() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public Single<FirebaseUser> signInWithGoogle(GoogleSignInAccount account) {
        return Single.create(emitter -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            emitter.onSuccess(user);
                        } else {
                            emitter.onError(new Exception("User is null"));
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}
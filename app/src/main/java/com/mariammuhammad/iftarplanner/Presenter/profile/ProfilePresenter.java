package com.mariammuhammad.iftarplanner.Presenter.profile;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mariammuhammad.iftarplanner.Common.MySharedPrefs;
import com.mariammuhammad.iftarplanner.Model.Repo.Repository;
import com.mariammuhammad.iftarplanner.Views.profile.ProfileView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfilePresenter implements ProfileContract{

    private final ProfileView profileView;
    private Repository repository;
   private  CompositeDisposable disposable=new CompositeDisposable();;

    public ProfilePresenter(ProfileView profileView,Repository repository){
        this.profileView = profileView;
        this.repository=repository;
    }

    @Override
    public void logout(){
        SharedPreferences.Editor editor = MySharedPrefs.getInstance().edit();
        editor.clear();
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        disposable.add( repository.clearAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() ->
                                profileView.signOut(),
                        throwable -> Log.e("Logout", "Failed to clear local database", throwable)
                )
        );

    }

    @Override
    public boolean isUserLoggedOut() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return true;
        }

        String username = currentUser.getDisplayName();
        if (username != null && !username.isEmpty()) {
            profileView.setUserName(username);
        } else if (currentUser.getEmail() != null) {
            profileView.setUserName(currentUser.getEmail().split("@")[0]);  // Fallback
        }

        return false;
    }

    public void clearDisposable(){
        disposable.clear();
    }
}




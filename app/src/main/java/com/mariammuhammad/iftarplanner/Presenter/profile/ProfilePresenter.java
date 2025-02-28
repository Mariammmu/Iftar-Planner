package com.mariammuhammad.iftarplanner.Presenter.profile;

import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mariammuhammad.iftarplanner.Views.profile.ProfileView;

public class ProfilePresenter {
        //implements ProfileContract{

//    private final FirebaseAuth auth;
//    private final SharedPreferences preferences;
//    private final ProfileView profileView;
//
//    public ProfilePresenter(ProfileView profileView,SharedPreferences preferences){
//        this.profileView = profileView;
//        this.preferences=preferences;
//        this.auth=FirebaseAuth.getInstance();
//    }
//
//    @Override
//    public void logout(){
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.commit();
//        auth.signOut();
//        profileView.logout();
//    }
//
//    @Override
//    public boolean isUserLoggedOut() {
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser == null) {
//            return true;
//        }
//
//        String username = currentUser.getDisplayName();
//        if (username != null && !username.isEmpty()) {
//            profileView.setUserName(username);
//        } else if (currentUser.getEmail() != null) {
//            profileView.setUserName(currentUser.getEmail().split("@")[0]);  // Fallback
//        }
//
//        return false;
//    }
}




package com.mariammuhammad.iftarplanner.Presenter.authentication.signUp;

public interface SignUpContract {

    void signUpWithEmail(String username, String email, String password, String confirmPassword);
    public void updateUserProfile( String username,String email, String password);
}

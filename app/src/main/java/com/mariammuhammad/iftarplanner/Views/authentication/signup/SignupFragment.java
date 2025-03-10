package com.mariammuhammad.iftarplanner.Views.authentication.signup;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mariammuhammad.iftarplanner.Presenter.authentication.signUp.SignUpPresenter;
import com.mariammuhammad.iftarplanner.R;


public class SignupFragment extends Fragment implements SignUpView {

    SignUpPresenter signUpPresenter;
Button btnSignUpFrag;
EditText etName,etEmail,etPass,etConfirmPass;

TextView txtLogin;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignUpFrag=view.findViewById(R.id.btnSignUpFrag);
        etName=view.findViewById(R.id.editTextName);
        etEmail=view.findViewById(R.id.editTextEmail);
        etPass=view.findViewById(R.id.editTextPass);
        etConfirmPass=view.findViewById(R.id.editTxtConfirmPass);
        txtLogin=view.findViewById(R.id.loginTxt);
        signUpPresenter = new SignUpPresenter(this);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        NavController navController = Navigation.findNavController(view);
                        navController.navigate(R.id.welcomeFragment);
                    }
                });

        btnSignUpFrag.setOnClickListener(v -> {
            String username = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPass.getText().toString().trim();
            String confirmPassword = etConfirmPass.getText().toString().trim();
            signUpPresenter.signUpWithEmail(username, email, password, confirmPassword);
        });

        txtLogin.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_signinFragment)
        );

        etPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableEnd = 2; // Index of drawableEnd
                    if (event.getRawX() >= (etPass.getRight() - etPass.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                        if (etPass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }
                        etPass.setSelection(etPass.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onSignupSuccess() {
        showSnackBar("SignUp Successfully");
        Navigation.findNavController(requireView()).navigate(R.id.action_signupFragment_to_signinFragment);
    }

    @Override
    public void onSignupFailure(String errorMessage) {
        requireActivity().runOnUiThread(() -> showSnackBar("SignUp Failed: " + errorMessage));

    }

@Override
    public void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        int color = ContextCompat.getColor(requireContext(), R.color.primary_dark);
        snackbarView.setBackgroundTintList(ColorStateList.valueOf(color));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
package com.mariammuhammad.iftarplanner.Views.authentication.signin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mariammuhammad.iftarplanner.Presenter.authentication.signIn.SignInPresenter;
import com.mariammuhammad.iftarplanner.R;


public class SigninFragment extends Fragment implements SignInView {

    Button btnSignin;
    TextView txtSignUp;
    EditText etEmail, etPass;
    SignInPresenter signInPresenter;
    SharedPreferences sharedPreferences;

    public SigninFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignin = view.findViewById(R.id.buttonSignIn);
        etEmail = view.findViewById(R.id.editTextEmail);
        etPass = view.findViewById(R.id.editTextPass);

        txtSignUp = view.findViewById(R.id.txtSignUp);
        signInPresenter = new SignInPresenter(this, requireContext(), view);
//        if (getActivity() != null) {
//            sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        }
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        txtSignUp.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_signinFragment_to_signupFragment);
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                signInPresenter.signIn(email, password);
            }
        });

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
    public void successSignIn() {

        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
        showSnackBar("Login Successfully");
        Navigation.findNavController(requireView()).navigate(R.id.action_signinFragment_to_homeFragment);

    }

    @Override
    public void failSignIn(String errorMessage) {
        showSnackBar("Sign In Failed: " + errorMessage);

    }
    @Override

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        int color = ContextCompat.getColor(requireContext(), R.color.primary_dark);
        snackbarView.setBackgroundTintList(ColorStateList.valueOf(color));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();

    }



}
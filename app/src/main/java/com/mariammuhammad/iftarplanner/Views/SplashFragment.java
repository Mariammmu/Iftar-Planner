package com.mariammuhammad.iftarplanner.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariammuhammad.iftarplanner.R;


public class SplashFragment extends Fragment {
    private Handler handler=new Handler();
   // private Runnable runnable;


    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_splash, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                // Perform fragment transaction to switch to WelcomeFragment
//                requireActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.splash, new WelcomeFragment()) // Replace with your container ID
//                        .addToBackStack(null)
//                        .commit();
//            }
//        }, 12000);

                handler.postDelayed(() -> {
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_splashFragment_to_welcomeFragment);
                }, 12000);

            }



}
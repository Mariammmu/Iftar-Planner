package com.mariammuhammad.iftarplanner.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariammuhammad.iftarplanner.R;


public class SplashFragment extends Fragment {
    private Handler handler=new Handler();
    private Runnable runnable;


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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Perform fragment transaction to switch to WelcomeFragment
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.splash, new WelcomeFragment()) // Replace with your container ID
                        .addToBackStack(null)
                        .commit();
            }
        }, 9000); 
        return inflater.inflate(R.layout.fragment_splash, container, false);

    }
    }

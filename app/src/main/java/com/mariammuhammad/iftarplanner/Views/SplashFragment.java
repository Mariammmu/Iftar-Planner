package com.mariammuhammad.iftarplanner.Views;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mariammuhammad.iftarplanner.R;


public class SplashFragment extends Fragment {
    private Handler handler=new Handler();

    TextView textViewIftar,textViewPlan;
    SharedPreferences sharedPreferences;

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

        View view= inflater.inflate(R.layout.fragment_splash, container, false);
        textViewIftar=view.findViewById(R.id.txtViewIftar);
        textViewPlan=view.findViewById(R.id.txtViewPlan);

        Animation moveAnimation = AnimationUtils.loadAnimation(requireContext(),R.anim.move);
        Animation fadeAnimation = AnimationUtils.loadAnimation(requireContext(),R.anim.fade);

        textViewIftar.startAnimation(moveAnimation );
        textViewPlan.startAnimation(fadeAnimation );
        return view;




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                requireActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.splash, new WelcomeFragment()) // Replace with your container ID
//                        .addToBackStack(null)
//                        .commit();
//            }
//        }, 12000);


        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        NavController navController = Navigation.findNavController(view);


                handler.postDelayed(() ->

            {
                if ((sharedPreferences.getBoolean("login", false)) ||
                        (sharedPreferences.getBoolean("google", false))) {
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
                } else {
                navController.navigate(R.id.action_splashFragment_to_welcomeFragment);
            }
                }, 12000);

            }



}
package com.mariammuhammad.iftarplanner.Views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.pdf.content.PdfPageGotoLinkContent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.mariammuhammad.iftarplanner.Common.NetworkConnectionListener;
import com.mariammuhammad.iftarplanner.Presenter.NetworkConnection;
import com.mariammuhammad.iftarplanner.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity implements NetworkConnectionListener {
    private BottomNavigationView bottomNav;
    private NavController navController;
    private LottieAnimationView lottieAnimationView;
    private FragmentContainerView fragmentContainerView;
    private ConnectivityManager.NetworkCallback networkCallback;
    private ConnectivityManager connectivityManager;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    NetworkConnection networkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeStatusBarColor();



        bottomNav = findViewById(R.id.bottom_navigation);
        lottieAnimationView=findViewById(R.id.waitingAnimation);
        fragmentContainerView=findViewById(R.id.navHostFragment);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNav, navController);

        bottomNav.setOnItemSelectedListener(item -> handleBottomNavigation(item));

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            int destinationId = destination.getId();
            Log.d("NavController", "Current Destination ID: " + destinationId);
                determineBottomNavVisibility(destinationId);

            if (destinationId == R.id.favorite_fragment || destinationId == R.id.planFragment) {
                lottieAnimationView.setVisibility(View.GONE);
                fragmentContainerView.setVisibility(View.VISIBLE);
            } else {
               // updateUIBasedOnNetwork();
                Log.i("TAG", "onCreate: ");
            }

            networkConnection = new NetworkConnection(this, this);
            networkConnection.registerNetworkListener();
        });

}
private void determineBottomNavVisibility(int destinationId){
    if (destinationId == R.id.homeFragment ||
            destinationId == R.id.searchFragment ||
            destinationId == R.id.favoriteFragment ||
            destinationId == R.id.planFragment) {
        Log.i("TAG", "onCreate: BottomNav");
        bottomNav.setVisibility(View.VISIBLE);

    } else {
        Log.i("TAG", "else:BottomNav");
        bottomNav.setVisibility(View.GONE);
    }
}
private void changeStatusBarColor(){
    Window window = getWindow();
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark));
}
    private boolean handleBottomNavigation(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            navController.navigate(R.id.homeFragment);
            return true;
        } else if (itemId == R.id.favorite) {
            navController.navigate(R.id.favoriteFragment);
            return true;
        } else if (itemId == R.id.calendar) {
            navController.navigate(R.id.planFragmentNav);
            return true;
        } else if (itemId == R.id.search) {
            navController.navigate(R.id.searchFragment);
            return true;
        }
        else if (itemId == R.id.profile) {
            navController.navigate(R.id.profileFragment);
            return true;
        }
        return false;
   }

    private void updateUIBasedOnNetwork() {
        if (isNetworkAvailable()) {
            lottieAnimationView.setVisibility(View.GONE);
            fragmentContainerView.setVisibility(View.VISIBLE);
        } else {
            lottieAnimationView.setVisibility(View.VISIBLE);
            fragmentContainerView.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        return networkConnection != null;
    }

    @Override
    public void onNetworkAvailable() {
        runOnUiThread(() -> {
            updateUIBasedOnNetwork();
           // showErrorSnackBar("Network is available", R.color.primary_dark);
        });
    }

    @Override
    public void onNetworkLost() {
        runOnUiThread(() -> {
            updateUIBasedOnNetwork();
            showErrorSnackBar("Network is not available", R.color.accent);
        });

    }

    private void showErrorSnackBar(String message, int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundTintList(ColorStateList.valueOf(color));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Dispose of RxJava subscriptions
        networkConnection.unregisterNetworkListener();
    }


}
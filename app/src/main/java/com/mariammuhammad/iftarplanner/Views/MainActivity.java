package com.mariammuhammad.iftarplanner.Views;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mariammuhammad.iftarplanner.Common.MySharedPrefs;
import com.mariammuhammad.iftarplanner.Common.NetworkConnectionListener;
import com.mariammuhammad.iftarplanner.Presenter.NetworkConnection;
import com.mariammuhammad.iftarplanner.R;

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

            if (destinationId == R.id.favorite_fragment || destinationId == R.id.scrollerPlan) {
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
            destinationId == R.id.planFragmentNav ||
            destinationId==R.id.profileFragment) {
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

    private boolean isGuestUser() {
       // SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = MySharedPrefs.getInstance().getString("userId", "");
        return "guest".equals(userId);
    }
    private boolean handleBottomNavigation(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            navController.navigate(R.id.homeFragment);
            return true;
        } else if (itemId == R.id.favorite || itemId == R.id.calendar ) { //I updated that
            if (isGuestUser()) {
                showErrorSnackBar("Please log in to access this section.", R.color.primary_light);
                return false; // Prevents navigation
            }
        }

        if (itemId == R.id.favorite) {
            navController.navigate(R.id.favoriteFragment);
            return true;
        } else if (itemId == R.id.calendar) {
            navController.navigate(R.id.planFragmentNav);
            return true;
        } else if (itemId == R.id.search) {
            navController.navigate(R.id.searchFragment);
            return true;
        } else if (itemId == R.id.profile) {
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
            showErrorSnackBar("Network is not available.\nCheck your Internet Connection", R.color.primary_dark);
        });

    }

    private void showErrorSnackBar(String message, int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        colorResId = ContextCompat.getColor(this,R.color.background_on);
        snackbarView.setBackgroundTintList(ColorStateList.valueOf(colorResId));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
    public void setNetworkStateListener(NetworkConnectionListener listener) {
        if (networkConnection != null) {
            networkConnection.setNetworkListener(listener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Dispose of RxJava subscriptions
        networkConnection.unregisterNetworkListener();
    }


}
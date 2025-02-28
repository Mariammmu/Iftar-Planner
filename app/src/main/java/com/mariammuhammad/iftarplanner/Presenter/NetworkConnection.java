package com.mariammuhammad.iftarplanner.Presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import com.mariammuhammad.iftarplanner.Common.NetworkConnectionListener;

public class NetworkConnection {
    private final ConnectivityManager connectivityManager;
    private final NetworkConnectionListener networkConnectionListener;
    private ConnectivityManager.NetworkCallback networkCallback;

    public NetworkConnection(Context context, NetworkConnectionListener listener) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkConnectionListener = listener;
        checkInitialNetworkState();
    }

    public void registerNetworkListener() {
        NetworkRequest networkRequest = new NetworkRequest.Builder().build();
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                networkConnectionListener.onNetworkAvailable();
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                networkConnectionListener.onNetworkLost();
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                networkConnectionListener.onNetworkLost();
            }
        };
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    public void unregisterNetworkListener() {
        if (networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    private void checkInitialNetworkState() {
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        boolean isConnected = capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));

        if (isConnected) {
            networkConnectionListener.onNetworkAvailable();
        } else {
            networkConnectionListener.onNetworkLost();
        }
    }

}

package com.mariammuhammad.iftarplanner.Common;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySharedPrefs.init(this);
    }
}

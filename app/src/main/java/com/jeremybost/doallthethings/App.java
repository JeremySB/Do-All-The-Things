package com.jeremybost.doallthethings;

import android.app.Application;

/**
 * Created by Jeremy on 11/15/2017.
 *
 * Purpose: global access to the application context
 */

public class App extends Application {
    private static App instance;
    public static App get() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}

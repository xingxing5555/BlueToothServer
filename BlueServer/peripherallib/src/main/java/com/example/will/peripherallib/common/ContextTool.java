package com.example.will.peripherallib.common;

import android.app.Application;
import android.content.Context;

/**
 * @author will4906.
 * @Time 2016/12/8.
 */

public class ContextTool extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }
}

package com.example.adrax.dely.core;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Создано Максимом Сунцевым 21.08.2017.
 */

public class App extends Application {
    private static WeakReference<Context> mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = new WeakReference<Context>(this);
    }

    public static Context getContext(){
        return mContext.get();
    }
}

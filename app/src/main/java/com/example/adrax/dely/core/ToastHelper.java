package com.example.adrax.dely.core;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.adrax.dely.MActivity;

/**
 * Создан Максимом Сунцвым 21.08.2017.
 */

public class ToastHelper {
    public static void createToast(
            @NonNull Activity activity,
            @NonNull String message
    ) {
        createToast(activity.getApplicationContext(), message);
    }

    public static void createToast(
            @NonNull Context context,
            @NonNull String message
    ) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
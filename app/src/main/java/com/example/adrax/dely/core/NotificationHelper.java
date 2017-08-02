package com.example.adrax.dely.core;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.example.adrax.dely.R;

/**
 * Created by Максим on 30.07.2017.
 */

public class NotificationHelper {
    public static void createNotification(
            @NonNull Activity activity,
            @NonNull String title,
            @NonNull String text
    ) {
        Context context = activity.getApplicationContext();

        Intent notificationIntent = new Intent(context, activity.getClass());
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.icon)
                // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.icon))
                //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                // .setTicker("Текст в строке состояния.")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle(title)
                //.setContentText(res.getString(R.string.notifytext))
                .setContentText(text); // Текст уведомления

        // Notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    private static final int NOTIFY_ID = 101;
}

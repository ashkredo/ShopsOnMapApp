package com.example.shopsonmapapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyAlert extends BroadcastReceiver {

    private int id = 0;
    private String channe1Id = "chan1";
    private String channelName = "myChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notif = new NotificationCompat.Builder(context, channe1Id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Witaj w sklepie")
                .setContentText("Odwiedź naszą aplikacje!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channe1Id, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Simple notification");

            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(id++, notif.build());
    }
}

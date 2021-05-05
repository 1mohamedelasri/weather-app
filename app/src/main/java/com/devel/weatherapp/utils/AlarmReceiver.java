package com.devel.weatherapp.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.devel.weatherapp.R;
import com.devel.weatherapp.repositories.WeatherRepository;
import com.devel.weatherapp.view.MainActivity;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
  /*      // TODO Auto-generated method stub
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.app_icon)
                .setContentTitle(SharedPreferences.getInstance(context).getNotificationTitle())
                .setContentText(SharedPreferences.getInstance(context).getNotificationBody())
                .setWhen(when)
                .setContentIntent(pendingIntent);
        notificationManager.notify(200, mNotifyBuilder.build());*/

    }

}
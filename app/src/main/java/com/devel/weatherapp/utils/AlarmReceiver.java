package com.devel.weatherapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherList;
import com.google.gson.Gson;

public class AlarmReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Gson gson = new Gson();
        WeatherForecast newWeather = gson.fromJson(intent.getStringExtra("newWeather"), WeatherForecast.class);


        StringBuilder str = new StringBuilder();
        WeatherList forcast = newWeather.getDailyForecasts().get(0);
        str.append(UtilityHelper.formatTemperature(context, forcast.getMain().getTemp(), true));
        str.append(" at");
        str.append(UtilityHelper.formatHourly(forcast.getDt()));
        str.append(" in ");
        str.append(newWeather.getCity().getName());

        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "test"; // The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
// Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(context)
                .setContentTitle(str)
                .setContentText(UtilityHelper.computeNotificaitonMessage(forcast.getWeathers().get(0).getId()))
                .setSmallIcon(R.drawable.app_icon)
                .setChannelId(CHANNEL_ID)
                .build();


        // notificationId is a unique int for each notification that you must define
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);

// Issue the notification.
        mNotificationManager.notify(notifyID, notification);


    }

}
package ru.zelting.denis.clockwidget;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.content.Context;

import java.util.Calendar;
import java.util.ServiceConfigurationError;

/**
 * Created by denis.zelting on 17.08.2016.
 */
public class TickService extends Service {
    Intent intent;
    PendingIntent piPI;
    double dTime;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();


        Log.d("TickService", "OnCreate");
        Context context = getApplicationContext();
        intent=new Intent(context, SimpleClockWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        piPI=PendingIntent.getBroadcast(context, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        (new Thread() {
            public void run() {
                int i = 0;
                while(true)
                {
                    Log.d("TickService", "TickThread " + Long.toString(i));
                    //SystemClock.sleep(1000);
                    dTime = Calendar.getInstance().getTimeInMillis();
                    Log.d("TickService", Long.toString(1000 - (long)dTime % 1000 + 3));
                    SystemClock.sleep(1000 - (long)dTime % 1000 + 3);
                    try
                    {
                        piPI.send();
                    }
                    catch (PendingIntent.CanceledException e)
                    {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        }).start();
        startForeground(1,new Notification());
    }
}

package ru.zelting.denis.clockwidget;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.AlarmClock;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RemoteViews;
import android.util.Log;
import android.widget.Toast;


public class SimpleClockWidget extends AppWidgetProvider {

    Context cont;
    final String LOG_TAG = "ClockWithHandle";
    private Handler uiHandler = new Handler();
    private PendingIntent piPI, clickPI;
    private Intent i, clickIntent;
    private double dTime;
    private RemoteViews views;
    AppWidgetManager appWidgetManager;
    ComponentName widget;


    @Override
    public void onEnabled(Context context) {
        Log.d(LOG_TAG, "onEnabled");

    }

    @Override
    public void onReceive(Context context, Intent i) {
        Log.d(LOG_TAG, "OnReceive");
        super.onReceive(context, i);
        clockwidget cW = clockwidget.getInstance();
        final String action = i.getAction();
        Log.d(LOG_TAG, "Action = " + action);
        if(!cW.isTimerStarted)
        {
            cW.isTimerStarted = true;
            Intent serviceIntent = new Intent(context, TickService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, TickService.class));
                Log.d(LOG_TAG, "Den startForegroundService (Build26)");
            } else {
                context.startService(new Intent(context, TickService.class));
            }
           // context.startService(serviceIntent);
        }
        if (action == "SimpleClockWidget.ON_CLICK") {
            // if it's a single tap than start standard alarm application
            Log.d(LOG_TAG, "We are in onClick");
            if(!cW.isFirstTap)
            {
                cW.isFirstTap = true;
                Log.d(LOG_TAG, "First tap. Wait 300 ms and start alarm if there is not a second tap");
                cont = context;
                uiHandler.postDelayed(HandlerRunnable,300);
            }
            else
            {
                // this is a second tap
                cW.isFirstTap = false;
                Log.d(LOG_TAG, "Second tap. Do something. For example, reconfig");
                Intent ii = new Intent(context, ActivityConfig.class);
                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ii);

            }
        } else if (action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {

            // get current time
            dTime = Calendar.getInstance().getTimeInMillis();
            // format time and date
            SimpleDateFormat fHours = new SimpleDateFormat("HH");
            SimpleDateFormat fMinutes = new SimpleDateFormat(":mm");
            SimpleDateFormat fSeconds = new SimpleDateFormat("ss");
            SimpleDateFormat fDate = new SimpleDateFormat("E. d MMM");
            String strHours = fHours.format(dTime);
            String strMinutes = fMinutes.format(dTime);
            String strSeconds = fSeconds.format(dTime);
            String strDate = fDate.format(dTime);
            // get widget's view
            views = new RemoteViews(context.getPackageName(), R.layout.clock_widget_layout);
            // set fields
            //int iID = context.getResources().getIdentifier("Background", "id", context.getPackageName());
            //views.setTextColor(iID, Color.parseColor(clockwidget.sTextColor));
            views.setInt(R.id.Background, "setBackgroundColor", Color.parseColor(clockwidget.sBackgroundColor));

            int iID = context.getResources().getIdentifier("Hours", "id", context.getPackageName());
            views.setTextViewTextSize(iID, TypedValue.COMPLEX_UNIT_DIP, clockwidget.iHoursSize);
            views.setTextColor(iID, Color.parseColor(clockwidget.sTextColor));
            iID = context.getResources().getIdentifier("Minutes", "id", context.getPackageName());
            views.setTextViewTextSize(iID, TypedValue.COMPLEX_UNIT_DIP, clockwidget.iMinutesSize);
            views.setTextColor(iID, Color.parseColor(clockwidget.sTextColor));
            iID = context.getResources().getIdentifier("Seconds", "id", context.getPackageName());
            views.setTextViewTextSize(iID, TypedValue.COMPLEX_UNIT_DIP, clockwidget.iSecondsSize);
            views.setTextColor(iID, Color.parseColor(clockwidget.sTextColor));
            iID = context.getResources().getIdentifier("Date", "id", context.getPackageName());
            views.setTextViewTextSize(iID, TypedValue.COMPLEX_UNIT_DIP, clockwidget.iDateSize);
            views.setTextColor(iID, Color.parseColor(clockwidget.sTextColor));

            views.setTextViewText(R.id.Hours, strHours);
            views.setTextViewText(R.id.Minutes, strMinutes);
            views.setTextViewText(R.id.Seconds, strSeconds);
            views.setTextViewText(R.id.Date, strDate);


            // subscribe view to receive onClick events
            Log.d(LOG_TAG, "Set onClick event");
            cW.isClickEventSet = true;
            clickIntent = new Intent(context, SimpleClockWidget.class);
            clickIntent.setAction("SimpleClockWidget.ON_CLICK");
            clickPI = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.Background, clickPI);


            // update widget's view
            widget=new ComponentName(context, SimpleClockWidget.class);
            appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(widget, views);
            Log.d("clockwidget", "updateAppWidget");
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    Runnable HandlerRunnable = new Runnable()
    {
        public void run()
        {
            Log.d(LOG_TAG, "HandlerRunnable");
            clockwidget cW = clockwidget.getInstance();
            if(cW.isFirstTap)
            {
                cW.isFirstTap = false;
                Log.d(LOG_TAG, "There is no second tap in 300 ms. So do a single tap action - start alarm");
                Intent alarmIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try{
                    cont.startActivity(alarmIntent);
                }
                catch (Exception e)
                {
                    Toast toast = Toast.makeText(cW.getApplicationContext(), cW.getResources().getString(R.string.sAlarmNotFound), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    Log.d(LOG_TAG, "Alarm activity exception");
                    toast.show();
                }
            }

        }
    };

}

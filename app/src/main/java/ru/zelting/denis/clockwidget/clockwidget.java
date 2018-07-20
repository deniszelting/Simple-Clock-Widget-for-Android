package ru.zelting.denis.clockwidget;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

public class clockwidget extends Application
{
    public boolean isTimerStarted = false;
    public boolean isClickEventSet = false;
    public boolean isFirstTap = false;
    public static int iHoursSize;
    public static int iMinutesSize;
    public static int iSecondsSize;
    public static int iDateSize;
    public static String sTextColor;
    public static String sBackgroundColor;
    public static clockwidget Inst;
    @Override
    public void onCreate()
    {
        super.onCreate();
        Inst = this;
        Log.d("myLogs", "onCreate App");

        isTimerStarted = false;
        SharedPreferences sharedPref = getSharedPreferences("ru.zelting.denis.clockwidget", Context.MODE_PRIVATE);
        iHoursSize = sharedPref.getInt("HoursSize", 54);
        iMinutesSize = sharedPref.getInt("MinutesSize", 28);
        iSecondsSize = sharedPref.getInt("SecondsSize", 16);
        iDateSize = sharedPref.getInt("DateSize", 18);
        sTextColor = sharedPref.getString("TextColor", "#FFFFFFFF");
        try {
            Color.parseColor(sTextColor);
        }
        catch (Exception e)
        {
            sTextColor = "#FFFFFFFF";
        }
        sBackgroundColor = sharedPref.getString("BackgroundColor", "#30777777");
        try {
            Color.parseColor(sBackgroundColor);
        }
        catch (Exception e)
        {
            sBackgroundColor = "#30777777";
        }
    }
    public static clockwidget getInstance()
    {
        Log.d("myLogs", "on getInstance");
        Log.d("myLogs", String.valueOf(Inst.isTimerStarted));
        return Inst;
    }
}

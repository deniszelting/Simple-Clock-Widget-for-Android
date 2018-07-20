package ru.zelting.denis.clockwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityConfig extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        EditText et = (EditText)findViewById(R.id.etHoursSize);
        et.setText(Integer.toString(clockwidget.iHoursSize));
        et = (EditText)findViewById(R.id.etMinutesSize);
        et.setText(Integer.toString(clockwidget.iMinutesSize));
        et = (EditText)findViewById(R.id.etSecondsSize);
        et.setText(Integer.toString(clockwidget.iSecondsSize));
        et = (EditText)findViewById(R.id.etDateSize);
        et.setText(Integer.toString(clockwidget.iDateSize));
        et = (EditText)findViewById(R.id.etTextColor);
        et.setText(clockwidget.sTextColor);
        et = (EditText)findViewById(R.id.etBackgroundColor);
        et.setText(clockwidget.sBackgroundColor);

    }

    public void onApplySettings(View v)
    {


        EditText etHoursSize = (EditText)findViewById(R.id.etHoursSize);
        EditText etMinutesSize = (EditText)findViewById(R.id.etMinutesSize);
        EditText etSecondsSize = (EditText)findViewById(R.id.etSecondsSize);
        EditText etDateSize = (EditText)findViewById(R.id.etDateSize);
        EditText etTextColor = (EditText)findViewById(R.id.etTextColor);
        EditText etBackgroundColor = (EditText)findViewById(R.id.etBackgroundColor);
        SharedPreferences sharedPref = getSharedPreferences("ru.zelting.denis.clockwidget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        clockwidget CV = clockwidget.getInstance();
        String sHoursSize = etHoursSize.getText().toString();
        if(sHoursSize.length() == 0)
        {
            sHoursSize = etHoursSize.getHint().toString();
        }
        CV.iHoursSize = Integer.parseInt(sHoursSize);
        String sMinutesSize = etMinutesSize.getText().toString();
        if(sMinutesSize.length() == 0)
        {
            sMinutesSize = etMinutesSize.getHint().toString();
        }
        CV.iMinutesSize = Integer.parseInt(sMinutesSize);
        String sSecondsSize = etSecondsSize.getText().toString();
        if(sSecondsSize.length() == 0)
        {
            sSecondsSize = etSecondsSize.getHint().toString();
        }
        CV.iSecondsSize = Integer.parseInt(sSecondsSize);
        String sDateSize = etDateSize.getText().toString();
        if(sDateSize.length() == 0)
        {
            sDateSize = etDateSize.getHint().toString();
        }
        CV.iDateSize = Integer.parseInt(sDateSize);
        CV.sTextColor = etTextColor.getText().toString();
        if(CV.sTextColor.length() == 0)
        {
            CV.sTextColor = etTextColor.getHint().toString();
        }
        CV.sBackgroundColor = etBackgroundColor.getText().toString();
        if(CV.sBackgroundColor.length() == 0)
        {
            CV.sBackgroundColor = etBackgroundColor.getHint().toString();
        }

        try {
            Color.parseColor(CV.sTextColor);
        }
        catch (Exception e)
        {
            CV.sTextColor = "#FFFFFFFF";
        }
        try {
            Color.parseColor(CV.sBackgroundColor);
        }
        catch (Exception e)
        {
            CV.sBackgroundColor = "#30777777";
        }

        editor.putInt("HoursSize", CV.iHoursSize);
        editor.putInt("MinutesSize", CV.iMinutesSize);
        editor.putInt("SecondsSize", CV.iSecondsSize);
        editor.putInt("DateSize", CV.iDateSize);
        editor.putString("TextColor", CV.sTextColor);
        editor.putString("BackgroundColor", CV.sBackgroundColor);
        editor.commit();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null)
        {
            int mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            Log.d("clockwidget", "set results");
            setResult(RESULT_OK, resultValue);
        }

        finish();
    }
}

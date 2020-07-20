package com.paradisetechnologies.brithwine.startupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.activity.ActivityHome;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.utils.StatMethods;
import com.paradisetechnologies.brithwine.utils.UtilitySharedPreferences;

public class ActivitySplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (StatMethods.isSession(ActivitySplashScreen.this))
                {
                    StatMethods.startNewActivity(ActivitySplashScreen.this, ActivityHome.class);
                }
                else
                {
                    StatMethods.startNewActivity(ActivitySplashScreen.this, ActivityLogin.class);
                }
            }
        }, 3000);
    }
}
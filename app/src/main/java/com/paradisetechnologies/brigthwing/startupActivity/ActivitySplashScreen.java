package com.paradisetechnologies.brigthwing.startupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.paradisetechnologies.brigthwing.R;
import com.paradisetechnologies.brigthwing.activity.ActivityHome;
import com.paradisetechnologies.brigthwing.utils.StatMethods;

public class ActivitySplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);

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

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ActivitySplashScreen.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivitySplashScreen.this, permission)) {
                ActivityCompat.requestPermissions(ActivitySplashScreen.this, new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(ActivitySplashScreen.this, new String[]{permission}, requestCode);
            }
        } else if (ContextCompat.checkSelfPermission(ActivitySplashScreen.this, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            if (requestCode == 101)
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.paradisetechnologies.brithwine.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.startupActivity.ActivitySplashScreen;

public class StatMethods
{
    private static Dialog dialog;

    public static void showToastLong(Context context, String message)
    {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showToastShort(Context context, String message)
    {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showMsgCode(Context context, int msg_code)
    {
        if (msg_code == 1)
        {
            showToastShort(context, AppConstants.VALIDATION_FAIL);
        }
        else if (msg_code == 2)
        {
            showToastShort(context, AppConstants.EXCEPTION_ERROR);
        }
        else if (msg_code == 3)
        {
            showToastShort(context, AppConstants.TOKEN_MISMATCH);
        }
    }

    public static void loadingView(Activity activity, boolean status)
    {
        if (status)
        {
            if (dialog == null)
            {
                if (!activity.isFinishing())
                {
                    dialog = new ProgressDialog(activity, R.style.MyProgressDialogTheme);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    if (!dialog.isShowing())
                    {
                        dialog.show();
                    }
                }
            }

        }
        else
        {
            if (activity != null && dialog != null && dialog.isShowing())
            {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    public static void startNewActivity(Activity currentActivity, Class<? extends Activity> newTopActivityClass)
    {
        Intent intent = new Intent(currentActivity, newTopActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
        currentActivity.startActivity(intent);
    }

    public static boolean isSession(Context context)
    {
        return (UtilitySharedPreferences.getPrefs(context, AppConstants.SHAREDPREFERENCES.USER_ID) != null && !UtilitySharedPreferences.getPrefs(context, AppConstants.SHAREDPREFERENCES.USER_ID).equals(""));
    }

    public static String isToken(Context context)
    {
        return UtilitySharedPreferences.getPrefs(context, AppConstants.SHAREDPREFERENCES.USER_AUTH_TOKEN);
    }

    public static void showQuizBoxDialog(Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.myDialog);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lockedView = inflater.inflate(R.layout.quiz_dialog_box, null);
        builder.setView(lockedView);

        ImageView ivExit = lockedView.findViewById(R.id.ivExit);

        final AlertDialog lockedContentDialog = builder.create();

        ivExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                lockedContentDialog.dismiss();
            }
        });

        lockedContentDialog.getWindow().setGravity(Gravity.CENTER);
        lockedContentDialog.setCancelable(true);
        lockedContentDialog.show();
        lockedContentDialog.getWindow().setLayout(getWidth(activity, 1.5), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public static int getWidth(Activity activity, double ratio)
    {
        if (activity == null)
        {
            return 0;
        }
        DisplayMetrics metrics = new DisplayMetrics();

        try
        {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
            return (int) Math.round(width / ratio);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return metrics.widthPixels;
    }
}

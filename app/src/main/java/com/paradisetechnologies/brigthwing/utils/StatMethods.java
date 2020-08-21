package com.paradisetechnologies.brigthwing.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paradisetechnologies.brigthwing.R;
import com.paradisetechnologies.brigthwing.constants.AppConstants;
import com.paradisetechnologies.brigthwing.interfcae.DownloadClick;
import com.paradisetechnologies.brigthwing.interfcae.PlayVideoClick;
import com.paradisetechnologies.brigthwing.interfcae.SubscribedNowClicked;
import com.paradisetechnologies.brigthwing.startupActivity.ActivityLogin;


public class StatMethods
{
    private static CustomDialog customDialog;
    private static Dialog dialog;
    private static DownloadClick downloadClick;
    private static PlayVideoClick playVideoClick;
    private static SubscribedNowClicked subscribedNowClicked;

    public static void initializeViews(Activity activity) {
        customDialog = new CustomDialog(activity);
        customDialog.setCancelable(false);
    }

    public static void showDialog(Activity activity) {
        initializeViews(activity);
        if (customDialog != null)
            customDialog.show();
    }

    public static void dismissDialog() {
        if (customDialog != null)
            customDialog.cancel();
    }

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
            StatMethods.showTokenMistmacthBoxDialog((Activity) context);
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

    public static void showQuizBoxDialog(final Activity activity, final String quiz_file_path, final int videoID,
                                         final String video_path, final String title, final String thumbnail_path,
                                         final String desc)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.myDialog);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lockedView = inflater.inflate(R.layout.quiz_dialog_box, null);
        builder.setView(lockedView);

        ImageView ivExit = lockedView.findViewById(R.id.ivExit);
        TextView tvDownloadQuiz = lockedView.findViewById(R.id.tvDownloadQuiz);
        TextView tvPlayVideo = lockedView.findViewById(R.id.tvPlayVideo);

        final AlertDialog lockedContentDialog = builder.create();

        ivExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                lockedContentDialog.dismiss();
            }
        });

        tvPlayVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                playVideoClick = (PlayVideoClick) activity;
                playVideoClick.playVideoClicked(video_path, videoID, title, thumbnail_path, desc, videoID);
                lockedContentDialog.dismiss();
            }
        });

        tvDownloadQuiz.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                downloadClick = (DownloadClick) activity;
                downloadClick.dwnloadClicked(quiz_file_path, videoID);
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

    public static void showTokenMistmacthBoxDialog(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.myDialog);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lockedView = inflater.inflate(R.layout.token_misnatch_dialog_box, null);
        builder.setView(lockedView);

        TextView tvLoginAgain = lockedView.findViewById(R.id.tvLoginAgain);

        final AlertDialog lockedContentDialog = builder.create();

        tvLoginAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                StatMethods.startNewActivity(activity, ActivityLogin.class);
            }
        });

        lockedContentDialog.getWindow().setGravity(Gravity.CENTER);
        lockedContentDialog.setCancelable(false);
        lockedContentDialog.show();
        lockedContentDialog.getWindow().setLayout(getWidth(activity, 1.5), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public static void showSubscriptionBoxDialog(final Activity activity, String selectedClassId, String selectedClassFee, String selectedClassName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.myDialog);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lockedView = inflater.inflate(R.layout.subscription_dialog_box, null);
        builder.setView(lockedView);

        TextView tvSubscribeNow = lockedView.findViewById(R.id.tvSubscribeNow);

        final AlertDialog lockedContentDialog = builder.create();

        tvSubscribeNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                subscribedNowClicked = (SubscribedNowClicked) activity;
                subscribedNowClicked.subscribeNowClicked(selectedClassId, selectedClassFee, selectedClassName);
                lockedContentDialog.dismiss();
            }
        });

        lockedContentDialog.getWindow().setGravity(Gravity.CENTER);
        lockedContentDialog.setCancelable(true);
        lockedContentDialog.show();
        lockedContentDialog.getWindow().setLayout(getWidth(activity, 1.5), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public static void showLogoutDialog(Activity activity)
    {
        new AlertDialog.Builder(activity)
                .setMessage(R.string.confirm_logout)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        UtilitySharedPreferences.clearPref(activity);
                        StatMethods.startNewActivity(activity, ActivityLogin.class);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }
}

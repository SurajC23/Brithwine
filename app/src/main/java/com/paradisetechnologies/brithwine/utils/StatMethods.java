package com.paradisetechnologies.brithwine.utils;

import android.content.Context;
import android.widget.Toast;

import com.paradisetechnologies.brithwine.constants.AppConstants;

public class StatMethods
{
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
}

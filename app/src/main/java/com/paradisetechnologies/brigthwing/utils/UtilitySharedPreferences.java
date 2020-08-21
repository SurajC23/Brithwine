package com.paradisetechnologies.brigthwing.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class UtilitySharedPreferences
{
    static String prefName = "user";
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    public static void setPrefs(Context context, String prefKey, String prefValue)
    {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(prefKey, prefValue);
        editor.apply();
    }

    public static String getPrefs(Context context, String prefKey)
    {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return preferences.getString(prefKey, null);
    }

    public static void clearPref(Context context)
    {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.clear().apply();
    }

    public static void clearPrefKey(Context context, String prefKey)
    {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        preferences.edit().remove(prefKey).apply();
    }
}

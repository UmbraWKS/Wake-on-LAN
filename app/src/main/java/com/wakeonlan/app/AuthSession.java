package com.wakeonlan.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AuthSession {
    public static boolean isUserAuthenticated(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        return preferences.getBoolean("auth", false);
    }

    public static void setUserAuthStatus(Activity activity, boolean status){
        SharedPreferences preferences = activity.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("auth", status);
        editor.apply();
    }
}

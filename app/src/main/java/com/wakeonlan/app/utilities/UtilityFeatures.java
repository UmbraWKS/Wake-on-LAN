package com.wakeonlan.app.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UtilityFeatures {

    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService (Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }


}

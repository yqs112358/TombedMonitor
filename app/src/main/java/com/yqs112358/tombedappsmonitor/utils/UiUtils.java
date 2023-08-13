package com.yqs112358.tombedappsmonitor.utils;

import android.content.res.Configuration;
import android.util.TypedValue;

import com.yqs112358.tombedappsmonitor.R;

public class UiUtils {
    public static int getCurrentThemePrimaryColor(){
        TypedValue typedValue = new TypedValue();
        ApplicationUtils.getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public static boolean isNightMode(){
        int uiMode = ApplicationUtils.getContext().getResources().getConfiguration().uiMode;
        return (uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }
}

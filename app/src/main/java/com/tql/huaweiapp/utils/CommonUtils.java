package com.tql.huaweiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tql.huaweiapp.R;

public class CommonUtils {
    private static final String THEME_ID = "theme_id";
    public static final int THEME_PINK = 0;
    public static final int THEME_RED = 1;
    public static final int THEME_BLUE = 2;
    public static final int THEME_GREEN = 3;

    public static int getTheme(Context context) {
        SharedPreferences themePreference = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        int themeID = themePreference.getInt(THEME_ID, THEME_PINK);
        int theme = R.style.Theme_Application_Pink;
        switch (themeID) {
            case THEME_PINK:
                theme = R.style.Theme_Application_Pink;
                break;
            case THEME_RED:
                theme = R.style.Theme_Application_Red;
                break;
            case THEME_BLUE:
                theme = R.style.Theme_Application_Blue;
                break;
            case THEME_GREEN:
                break;
        }
        return theme;
    }

    public static void setTheme(Context context, int themeId) {
        SharedPreferences themePreference = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = themePreference.edit();
        editor.putInt(THEME_ID, themeId);
        editor.apply();
    }
}

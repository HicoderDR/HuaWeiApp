package com.tql.huaweiapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.tql.huaweiapp.R;
import com.tql.huaweiapp.activity.MainActivity;
import com.tql.huaweiapp.activity.RegisterOrLoginActivity;
import com.tql.huaweiapp.entry.User;

import java.util.ArrayList;

public class CommonUtils {
    private static final String THEME_ID = "theme_id";
    private static final String USER_TOKEN = "user_token";
    public static final int THEME_PINK = 0;
    public static final int THEME_RED = 1;
    public static final int THEME_BLUE = 2;
    public static final int THEME_GREEN = 3;
    private static ArrayList<AppCompatActivity> activities = new ArrayList<>();


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

    /**
     * 记录当前登录的用户
     *
     * @param context
     * @param email
     */
    public static void login(Context context, String email) {
        SharedPreferences userPreference = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(USER_TOKEN, email);
        editor.apply();
    }

    /**
     * 登出
     *
     * @param context
     */
    public static void logout(Context context) {
        SharedPreferences userPreference = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPreference.edit();
        editor.putString(USER_TOKEN, "");
        editor.apply();
        for (AppCompatActivity activity : activities) {
            activity.finish();
        }
        context.startActivity(new Intent(context, RegisterOrLoginActivity.class));
    }

    /**
     * 获取当前登录用户的邮箱账号
     *
     * @param context
     * @return
     */
    public static String getCurrentUserEmail(Context context) {
        SharedPreferences userPreference = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return userPreference.getString(USER_TOKEN, "");
    }

    public static void addActivity(AppCompatActivity activity) {
        activities.add(activity);
    }

    public static void exitApplication() {
        for (AppCompatActivity activity : activities) {
            activity.finish();
        }
        System.exit(0);
    }
}

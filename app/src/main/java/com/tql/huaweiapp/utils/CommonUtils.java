package com.tql.huaweiapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.tql.huaweiapp.R;
import com.tql.huaweiapp.activity.RegisterOrLoginActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static void saveMessageToLocal(Context context, int from, String message, String bot_id) {
        String path = context.getFilesDir().getAbsolutePath() + File.separator + "messages" + File.separator;
        if (!new File(path).exists()) new File(path).mkdirs();
        System.out.println(path);
        path = path + getCurrentUserEmail(context).split("@")[0] + "_" + bot_id;
        System.out.println(path);
        try {
            File file = new File(path);
            if (!file.exists()) file.createNewFile();
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            br.write(from + "::" + message + "\n");
            br.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取聊天记录
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getMessagesFromLocal(Context context, String bot_id) {
        String path = context.getFilesDir().getAbsolutePath() + File.separator + "messages" + File.separator;
        if (!new File(path).exists()) new File(path).mkdirs();
        System.out.println(path);
        path = path + getCurrentUserEmail(context).split("@")[0] + "_" + bot_id;
        System.out.println(path);
        ArrayList<String> messages = new ArrayList<>();
        try {
            File file = new File(path);
            if (!file.exists()) return messages;
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                messages.add(line);
//                messages.put(Integer.valueOf(line.split("::")[0]),line.split("::")[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static ArrayList<String> getLocalChatList(Context context) {
        ArrayList<String> res = new ArrayList<>();
        String path = context.getFilesDir().getAbsolutePath() + File.separator + "messages" + File.separator;
        if (!new File(path).exists()) return res;
        else {
            File file = new File(path);
            for (File f : file.listFiles()) {
                if (f.getName().split("_")[0].equals(getCurrentUserEmail(context).split("@")[0])) {
                    if (!res.contains(f.getName().split("_")[1]))
                        res.add(f.getName().split("_")[1]);
                }
            }
        }
        return res;
    }
}

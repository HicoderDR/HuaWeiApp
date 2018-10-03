package com.tql.huaweiapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tql.huaweiapp.R;
import com.tql.huaweiapp.utils.CommonUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
        setContentView(R.layout.window_background);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                if (CommonUtils.getCurrentUserEmail(SplashActivity.this).equals(""))
                    startActivity(new Intent(SplashActivity.this, RegisterOrLoginActivity.class));
                else startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}

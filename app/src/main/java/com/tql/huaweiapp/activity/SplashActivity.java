package com.tql.huaweiapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tql.huaweiapp.R;
import com.tql.huaweiapp.utils.CommonUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
        setContentView(R.layout.activity_splash);
        if (CommonUtils.getCurrentUserEmail(this).equals(""))
            startActivity(new Intent(SplashActivity.this, RegisterOrLoginActivity.class));
        else startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}

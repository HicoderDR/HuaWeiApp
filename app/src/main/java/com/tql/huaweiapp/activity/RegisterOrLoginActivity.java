package com.tql.huaweiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tql.huaweiapp.R;

public class RegisterOrLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEdittext;
    private EditText passwordEdittext;
    private EditText passwordConfirmEdittext;
    /**
     * 登录
     */
    private Button signInButton;
    /**
     * 注册
     */
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_login);
        initView();

    }

    private void initView() {
        emailEdittext = findViewById(R.id.email_edittext);
        passwordEdittext = findViewById(R.id.password_edittext);
        passwordConfirmEdittext = findViewById(R.id.password_confirm_edittext);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_up_button:
                signUp();
                break;
        }
    }

    /**
     * 注册
     */
    private void signUp() {
        // TODO: 2018/9/15 注册逻辑

    }

    /**
     * 登录
     */
    private void signIn() {
        // TODO: 2018/9/15 登录逻辑
        startActivity(new Intent(RegisterOrLoginActivity.this, MainActivity.class));
    }
}

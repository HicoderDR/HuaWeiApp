package com.tql.huaweiapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tql.huaweiapp.R;
import com.tql.huaweiapp.utils.CommonUtils;
import com.tql.huaweiapp.view.AlertDialogIOS;

public class RegisterOrLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final boolean SIGN_IN = true;
    private static final boolean SIGN_UP = false;
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

    /**
     * 默认是登录按钮被选中
     */
    private boolean buttonSelected = SIGN_IN;

    /**
     * 注册或者登录信息域
     *
     * @param savedInstanceState
     */
    private String email;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
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
                //登录
                signInButton.setTextColor(Color.WHITE);
                signInButton.setBackground(getResources().getDrawable(R.drawable.button_selected, getTheme()));
                signUpButton.setBackground(getResources().getDrawable(R.drawable.button_unselected, getTheme()));
                signUpButton.setTextColor(R.attr.colorPrimary);
                passwordConfirmEdittext.setVisibility(View.GONE);
                if (buttonSelected == SIGN_IN) signIn();
                buttonSelected = SIGN_IN;
                break;
            case R.id.sign_up_button:
                //注册
                signUpButton.setTextColor(Color.WHITE);
                signUpButton.setBackground(getResources().getDrawable(R.drawable.button_selected, getTheme()));
                signInButton.setBackground(getResources().getDrawable(R.drawable.button_unselected, getTheme()));
                signInButton.setTextColor(R.attr.colorPrimary);
                passwordConfirmEdittext.setVisibility(View.VISIBLE);
                if (buttonSelected == SIGN_UP) signUp();
                buttonSelected = SIGN_UP;
                break;
        }
    }

    /**
     * 注册
     */
    private void signUp() {
        // TODO: 2018/9/15 注册逻辑
        ProgressDialog waitingDialog = new ProgressDialog(this);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setMessage("发送验证码......");
        waitingDialog.setCancelable(false);
        waitingDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        waitingDialog.show();
        WindowManager.LayoutParams params = waitingDialog.getWindow().getAttributes();
        params.width = 450;
        params.gravity = Gravity.CENTER;
        waitingDialog.getWindow().setAttributes(params);

        waitingDialog.dismiss();

        new AlertDialogIOS(this).builder()
                .setCancelable(false)
                .setTitle("邮箱验证码")
                .setMsg("请填写你收到的六位验证码：")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 18-9-21 注册逻辑
                        getInputContent();


                        Intent intent = new Intent(RegisterOrLoginActivity.this, CompleteUserInfoActivity.class);
                        intent.putExtra("type", "1");
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    private void getInputContent() {
        email = emailEdittext.getText().toString();
        password = passwordEdittext.getText().toString();
        confirmPassword = passwordConfirmEdittext.getText().toString();
    }

    /**
     * 登录
     */
    private void signIn() {
        // TODO: 2018/9/15 登录逻辑
        startActivity(new Intent(RegisterOrLoginActivity.this, MainActivity.class));
    }
}

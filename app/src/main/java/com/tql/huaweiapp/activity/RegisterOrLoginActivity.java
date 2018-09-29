package com.tql.huaweiapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tql.huaweiapp.R;
import com.tql.huaweiapp.utils.CommonUtils;
import com.tql.huaweiapp.utils.ServerUtils;
import com.tql.huaweiapp.view.AlertDialogIOS;

import static com.tql.huaweiapp.utils.ServerUtils.FAILED;
import static com.tql.huaweiapp.utils.ServerUtils.SUCCESSFUL;

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
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
        setContentView(R.layout.activity_register_or_login);
        CommonUtils.addActivity(this);
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
        getInputContent(1);
//        if (!checkInputFormat(1)) return;

        // TODO: 2018/9/15 注册逻辑
        final ProgressDialog waitingDialog = getProgressDialog("正在发送验证码...");

        final AlertDialogIOS alertDialogIOS = new AlertDialogIOS(this).builder()
                .setCancelable(false)
                .setTitle("邮箱验证码")
                .setMsg("请填写你收到的六位验证码：")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog waitingDialog = getProgressDialog("正在注册...");
                        // TODO: 18-9-21 注册逻辑
                        getInputContent(1);
                        if (!AlertDialogIOS.verificationCode.equals(verificationCode)){
                            toast("验证码错误！");
                            waitingDialog.dismiss();
                            return;
                        }

                        Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                waitingDialog.dismiss();
                                switch (msg.what) {
                                    case SUCCESSFUL:
                                        CommonUtils.login(RegisterOrLoginActivity.this, email);
                                        Intent intent = new Intent(RegisterOrLoginActivity.this, CompleteUserInfoActivity.class);
                                        intent.putExtra("type", "1");
                                        intent.putExtra("email", email);
                                        intent.putExtra("password", password);
                                        toast("注册成功！");
                                        startActivity(intent);
                                        break;
                                    case FAILED:
                                        toast(msg.obj.toString());
                                        break;
                                }
                            }
                        };
                        ServerUtils.addUser(email, password, handler);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

        ServerUtils.getVerificationCode(email, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                waitingDialog.dismiss();
                if (msg.what == ServerUtils.FAILED)
                    toast("验证码发送失败，请重试！");
                else {
                    verificationCode = msg.obj.toString();
                    toast("验证码发送成功！");
                    alertDialogIOS.show();
                }
            }
        });
    }

    @NonNull
    private ProgressDialog getProgressDialog(String s) {
        ProgressDialog waitingDialog = new ProgressDialog(this);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setMessage(s);
        waitingDialog.setCancelable(false);
        waitingDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        waitingDialog.show();
        WindowManager.LayoutParams params = waitingDialog.getWindow().getAttributes();
        params.width = 450;
        params.gravity = Gravity.CENTER;
        waitingDialog.getWindow().setAttributes(params);
        return waitingDialog;
    }

    /**
     * 检查输入内容格式
     *
     * @param i
     */
    private boolean checkInputFormat(int i) {
        if (email.isEmpty() || password.isEmpty()) {
            toast("内容不能为空！");
            return false;
        } else if (email.matches("") || password.length() < 8) {
            toast("邮箱账号或者密码不正确！");
            return false;
        } else if (i == 1) {
            if (!password.equals(confirmPassword)) toast("两次密码输入不符！");
            return false;
        }
        return true;
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void getInputContent(int i) {
        email = emailEdittext.getText().toString();
        password = passwordEdittext.getText().toString();
        if (i == 1)
            confirmPassword = passwordConfirmEdittext.getText().toString();
    }

    /**
     * 登录
     */
    private void signIn() {
        // TODO: 2018/9/15 登录逻辑
        getInputContent(0);
        if (!checkInputFormat(0)) return;

        final ProgressDialog dialog = getProgressDialog("正在登录...");

        ServerUtils.login(email,password,new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dialog.dismiss();
                if (msg.what == ServerUtils.FAILED){
                    toast("登录失败！");
                }else {
                    toast("登录成功！");
                    CommonUtils.login(RegisterOrLoginActivity.this, email);
                    startActivity(new Intent(RegisterOrLoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}

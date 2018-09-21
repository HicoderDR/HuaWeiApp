package com.tql.huaweiapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.qzs.android.fuzzybackgroundlibrary.Fuzzy_Background;
import com.tql.huaweiapp.R;

public class CompleteUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView completeInfoBgImageview;
    private EditText nicknameEdittext;
    private EditText birthdayEdittext;
    private EditText ageEdittext;
    private EditText gendetEdittext;
    private EditText tagEdittext;
    /**
     * 保存
     */
    private Button saveInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_info);
        initView();

        setBackground();
    }

    /**
     * 设置高斯模糊背景
     */
    //补充
    //     -
    //     1.模糊半径的范围：大于0小于25
    //
    //     2.可以指定模糊前缩小的倍数
    //
    //     Bitmap finalBitmap = Fuzzy_Background.with(MainActivity.this)
    //     .bitmap(bitmap) //要模糊的图片
    //     .radius(10)//模糊半径<br>
    //     .scale(4)//指定模糊前缩小的倍数
    //     .blur();
    private void setBackground() {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.default_bg);

//        2.高斯模糊：
        Bitmap finalBitmap = Fuzzy_Background.with(this)
                .bitmap(bitmap) //要模糊的图片
                .radius(5)//模糊半径
                .blur();

//        3.设置bitmap：
        completeInfoBgImageview.setImageBitmap(finalBitmap);

    }

    private void initView() {
        completeInfoBgImageview = findViewById(R.id.complete_info_bg_imageview);
        nicknameEdittext = findViewById(R.id.nickname_edittext);
        birthdayEdittext = findViewById(R.id.birthday_edittext);
        birthdayEdittext.setOnClickListener(this);
        ageEdittext = findViewById(R.id.age_edittext);
        ageEdittext.setOnClickListener(this);
        gendetEdittext = findViewById(R.id.gendet_edittext);
        gendetEdittext.setOnClickListener(this);
        tagEdittext = findViewById(R.id.tag_edittext);
        tagEdittext.setOnClickListener(this);
        saveInfoButton = findViewById(R.id.save_info_button);
        saveInfoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.birthday_edittext:
                break;
            case R.id.age_edittext:
                break;
            case R.id.gendet_edittext:
                break;
            case R.id.tag_edittext:
                break;
            case R.id.save_info_button:
                saveInfo();
                break;
        }
    }

    /**
     * 保存用户信息
     */
    private void saveInfo() {
        // TODO: 18-9-21
        startActivity(new Intent(CompleteUserInfoActivity.this, MainActivity.class));
    }
}

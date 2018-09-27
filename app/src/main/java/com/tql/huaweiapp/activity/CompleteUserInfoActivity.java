package com.tql.huaweiapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.qzs.android.fuzzybackgroundlibrary.Fuzzy_Background;
import com.tql.huaweiapp.R;
import com.tql.huaweiapp.constant.Hobby;
import com.tql.huaweiapp.utils.CommonUtils;
import com.tql.huaweiapp.view.ActionSheetIOS;
import com.tql.huaweiapp.view.TagDialog;
import com.tql.huaweiapp.view.WrapLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private ImageView mCloseImageview;
    /**
     * 让Ta更了解你吧
     */
    private TextView mHintTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
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
        mCloseImageview = findViewById(R.id.close_imageview);
        mCloseImageview.setOnClickListener(this);
        mHintTextview = findViewById(R.id.hint_textview);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equals("0")){
            setHint("告诉Ta更真实的自己");
        }else {
            setHint("让Ta更了解你吧");
        }
    }

    /**
     * 设置页面标头提示文字
     *
     * @param hint
     */
    public void setHint(String hint){
        mHintTextview.setText(hint);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.birthday_edittext:
                selectBirthday();
                break;
            case R.id.age_edittext:
                break;
            case R.id.gendet_edittext:
                new ActionSheetIOS(this).builder()
                        .addSheetItem("女生", ActionSheetIOS.SheetItemColor.Blue, new ActionSheetIOS.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                gendetEdittext.setText("女生");
                            }
                        })
                        .addSheetItem("男生", ActionSheetIOS.SheetItemColor.Blue, new ActionSheetIOS.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                gendetEdittext.setText("男生");
                            }
                        })
                        .addSheetItem("其他?", ActionSheetIOS.SheetItemColor.Blue, new ActionSheetIOS.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                gendetEdittext.setText("第三性别");
                            }
                        }).show();
                break;
            case R.id.tag_edittext:
                new TagDialog(this).builder()
                        .setTitle("选择个性标签")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int[] selectedTag = WrapLayout.selectedTag;
                                StringBuffer tags = new StringBuffer();
                                for (int i1 = 0, selectedTagLength = selectedTag.length; i1 < selectedTagLength; i1++) {
                                    int i = selectedTag[i1];
                                    if (i == 1) {
                                        tags.append(Hobby.HOBBIES[i1]).append(",");
                                    }
                                }
                                if (!tags.toString().isEmpty())
                                    tagEdittext.setText(tags.substring(0, tags.length() - 1));
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .initTag(tagEdittext.getText().toString())
                        .setCancelable(false).show();
                break;
            case R.id.save_info_button:
                saveInfo();
                break;
            case R.id.close_imageview:
                startActivity(new Intent(CompleteUserInfoActivity.this, MainActivity.class));
                this.finish();
                break;
        }
    }

    /**
     * 选择生日
     */
    private void selectBirthday() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                birthdayEdittext.setText(getTime(date));
                // TODO: 18-9-21 修改年龄
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
//                .setTitleText("选择生日日期")//标题文字
//                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
//                .setTitleColor(getResources().getColor())//标题文字颜色
//                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.RED)//取消按钮文字颜色
//                //.setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
//                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
////                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
////                .setRangDate(startDate,endDate)//起始终止年月日设定
//                //.setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                //.isDialog(true)//是否显示为对话框样式
                .build();

        pvTime.show();
    }


    /**
     * 保存用户信息
     */
    private void saveInfo() {
        // TODO: 18-9-21
        startActivity(new Intent(CompleteUserInfoActivity.this, MainActivity.class));
        this.finish();
    }

    /**
     * 生日显示格式
     *
     * @param date
     * @return
     */
    private String getTime(Date date) {//可根据需要自行截取数据显示
        //"YYYY-MM-DD HH:MM:SS"        "yyyy-MM-dd"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}

package com.tql.huaweiapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qzs.android.fuzzybackgroundlibrary.Fuzzy_Background;
import com.tql.huaweiapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView avatarImageview;
    private ImageView avatarBackgroundImageview;
    private SlidingPaneLayout slidePanel;
    /**
     * 暂无收藏记录
     */
    private TextView noFavoriteTextview;
    private LinearLayout favoreitesLinearlayout;
    /**
     * 暂无聊天记录
     */
    private TextView noRecordsTextview;
    private LinearLayout otherChatListLinearlayout;
    private FloatingActionButton newChatActionbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        avatarImageview = findViewById(R.id.avatar_imageview);
        avatarImageview.setOnClickListener(this);
        avatarBackgroundImageview = findViewById(R.id.avatar_background_imageview);
        setBackground();//侧栏头像设置高斯模糊背景
        slidePanel = findViewById(R.id.slide_panel);
        avatarImageview = findViewById(R.id.avatar_imageview);
        noFavoriteTextview = findViewById(R.id.no_favorite_textview);
        favoreitesLinearlayout = findViewById(R.id.favoreites_linearlayout);
        noRecordsTextview = findViewById(R.id.no_records_textview);
        otherChatListLinearlayout = findViewById(R.id.other_chat_list_linearlayout);
        slidePanel = findViewById(R.id.slide_panel);

        newChatActionbutton = findViewById(R.id.new_chat_actionbutton);
        newChatActionbutton.setOnClickListener(this);

        initChatList();
    }

    //初始化聊天列表
    private void initChatList() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.avatar_imageview:
                slidePanel.openPane();
                break;
            case R.id.new_chat_actionbutton:
                startActivity(new Intent(MainActivity.this, NewChatActivity.class));
                break;
        }
    }

    private void setBackground() {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.default_menu_bg);

//        2.高斯模糊：
        Bitmap finalBitmap = Fuzzy_Background.with(this)
                .bitmap(bitmap) //要模糊的图片
                .radius(10)//模糊半径
                .blur();

//        3.设置bitmap：
        avatarBackgroundImageview.setImageBitmap(finalBitmap);

    }
}

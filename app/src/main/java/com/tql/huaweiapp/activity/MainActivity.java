package com.tql.huaweiapp.activity;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tql.huaweiapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView avatarImageview;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        avatarImageview = findViewById(R.id.avatar_imageview);
        avatarImageview.setOnClickListener(this);
        slidePanel = findViewById(R.id.slide_panel);
        avatarImageview = findViewById(R.id.avatar_imageview);
        noFavoriteTextview = findViewById(R.id.no_favorite_textview);
        favoreitesLinearlayout = findViewById(R.id.favoreites_linearlayout);
        noRecordsTextview = findViewById(R.id.no_records_textview);
        otherChatListLinearlayout = findViewById(R.id.other_chat_list_linearlayout);
        slidePanel = findViewById(R.id.slide_panel);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.avatar_imageview:
                slidePanel.openPane();
                break;
        }
    }
}

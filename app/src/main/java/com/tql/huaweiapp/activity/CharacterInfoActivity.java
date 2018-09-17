package com.tql.huaweiapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.tql.huaweiapp.R;

public class CharacterInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backImageview;
    private ImageView menuImageview;
    private FloatingActionButton newChatActionbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info);
        initView();
    }

    private void initView() {
        backImageview = findViewById(R.id.back_imageview);
        backImageview.setOnClickListener(this);
        menuImageview = findViewById(R.id.menu_imageview);
        menuImageview.setOnClickListener(this);
        newChatActionbutton = findViewById(R.id.new_chat_actionbutton);
        newChatActionbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.back_imageview:
                break;
            case R.id.menu_imageview:
                menuClickEvent(v);
                break;
            case R.id.new_chat_actionbutton:
                break;
        }
    }

    private void menuClickEvent(View v) {
        PopupMenu pm = new PopupMenu(this,v);
        pm.inflate(R.menu.character_info_menu);
        pm.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // TODO: 2018/9/17 添加“喜欢”逻辑

                return true;
            }
        });
        pm.show();
    }
}

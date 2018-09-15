package com.tql.huaweiapp.activity;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.tql.huaweiapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView avatarImageview;
    private SlidingPaneLayout slidePanel;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.avatar_imageview:
                break;
        }
    }
}

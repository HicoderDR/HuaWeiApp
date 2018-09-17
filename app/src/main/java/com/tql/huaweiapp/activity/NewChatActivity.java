package com.tql.huaweiapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.tql.huaweiapp.R;

public class NewChatActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout selectFictionsLinearlayout;
    private LinearLayout fictionsListLinearlayout;
    private HorizontalScrollView fictionsScrollview;
    private LinearLayout charactersListLinearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        initView();
    }

    private void initView() {
        selectFictionsLinearlayout = findViewById(R.id.select_fictions_linearlayout);
        selectFictionsLinearlayout.setOnClickListener(this);
        fictionsListLinearlayout = findViewById(R.id.fictions_list_linearlayout);
        fictionsScrollview = findViewById(R.id.fictions_scrollview);
        charactersListLinearlayout = findViewById(R.id.characters_list_linearlayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.select_fictions_linearlayout:
                break;
        }
    }
}

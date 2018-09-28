package com.tql.huaweiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.tql.huaweiapp.R;
import com.tql.huaweiapp.adapter.CharacterListAdapter;
import com.tql.huaweiapp.utils.CommonUtils;

import java.util.ArrayList;

public class NewChatActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout selectFictionsLinearlayout;
//    private LinearLayout fictionsListLinearlayout;
//    private HorizontalScrollView fictionsScrollview;
    private RecyclerView charactersListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
        setContentView(R.layout.activity_new_chat);
        CommonUtils.addActivity(this);
        initView();
    }

    private void initView() {
        selectFictionsLinearlayout = findViewById(R.id.select_fictions_linearlayout);
        selectFictionsLinearlayout.setOnClickListener(this);
//        fictionsListLinearlayout = findViewById(R.id.fictions_list_linearlayout);
//        fictionsScrollview = findViewById(R.id.fictions_scrollview);
        charactersListRecyclerView = findViewById(R.id.characters_list_recyclerview);

        initCharacterList();
    }

    /**
     * 初始化人物信息列表
     */
    private void initCharacterList() {
        ArrayList<Integer> avatars = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> informations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            avatars.add(R.mipmap.default_character_avatar);
            names.add("畜生叶哥");
            informations.add("这是畜生叶哥的非常长非常长非常长非常长非常长非常长非常长非常长非常长非常长非常长非常长的简介。");
        }
        CharacterListAdapter adapter = new CharacterListAdapter(avatars, names, informations);
        adapter.setOnItemClickListener(new CharacterListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                startActivity(new Intent(NewChatActivity.this, CharacterInfoActivity.class));
            }
        });
        charactersListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        charactersListRecyclerView.setHasFixedSize(false);
        charactersListRecyclerView.setAdapter(adapter);
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

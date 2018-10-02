package com.tql.huaweiapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tql.huaweiapp.R;
import com.tql.huaweiapp.adapter.CharacterListAdapter;
import com.tql.huaweiapp.utils.CommonUtils;
import com.tql.huaweiapp.utils.ServerUtils;

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
        final ProgressDialog dialog = getProgressDialog("正在疯狂加载中...");

        final ArrayList<Integer> avatars = new ArrayList<>();
        final ArrayList<String> names = new ArrayList<>();
        final ArrayList<String> informations = new ArrayList<>();
        final ArrayList<String> bot_ids = new ArrayList<>();

        ServerUtils.getAllBot(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dialog.dismiss();
                if (msg.what == ServerUtils.FAILED) {
                    toast("获取人物列表失败！");
                } else {
                    JSONArray array = JSON.parseArray(msg.obj.toString());
                    System.out.println(array);
                    for (int i = 0; i < 2; i++) {
                        JSONObject object = JSON.parseObject(array.getString(i));
                        System.out.println(object);
                        avatars.add(R.mipmap.default_character_avatar);
                        names.add(object.getString("name"));
                        informations.add(object.getString("introduction"));
                        bot_ids.add(object.getString("botID"));
                        System.out.println("bot_ID:"+object.getString("botID"));
                    }

                    final CharacterListAdapter adapter = new CharacterListAdapter(avatars, names, informations, bot_ids);
                    adapter.setOnItemClickListener(new CharacterListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            Intent intent = new Intent(NewChatActivity.this, ChatActivity.class);
                            intent.putExtra("bot_id", adapter.getCharacterId(position));
                            intent.putExtra("name",names.get(position));
                            startActivity(intent);
                        }
                    });
                    charactersListRecyclerView.setLayoutManager(new LinearLayoutManager(NewChatActivity.this, LinearLayoutManager.VERTICAL, false));
                    charactersListRecyclerView.setHasFixedSize(false);
                    charactersListRecyclerView.setAdapter(adapter);
                }
            }
        });

//        for (int i = 0; i < 5; i++) {
//            avatars.add(R.mipmap.default_character_avatar);
//            names.add("畜生叶哥");
//            informations.add("这是畜生叶哥的非常长非常长非常长非常长非常长非常长非常长非常长非常长非常长非常长非常长的简介。");
//        }


    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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

}

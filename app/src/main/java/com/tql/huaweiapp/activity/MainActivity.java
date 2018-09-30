package com.tql.huaweiapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qzs.android.fuzzybackgroundlibrary.Fuzzy_Background;
import com.tql.huaweiapp.R;
import com.tql.huaweiapp.adapter.ChatListAdapter;
import com.tql.huaweiapp.constant.Gender;
import com.tql.huaweiapp.utils.CommonUtils;
import com.tql.huaweiapp.utils.ServerUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView avatar_main_Imageview;
    private ImageView avatar_Imageview;
    private ImageView avatarBackgroundImageview;
    private SlidingPaneLayout slidePanel;
    /**
     * 暂无收藏记录
     */
    private TextView noFavoriteTextview;
    /**
     * 暂无聊天记录
     */
    private TextView noRecordsTextview;
    private FloatingActionButton newChatActionbutton;
    private RoundedImageView themeAImageview;
    private RoundedImageView themeBImageview;
    private RoundedImageView themeCImageview;
    private RecyclerView favoreitesRecyclerview;
    private RecyclerView otherChatListRecyclerview;
    private LinearLayoutManager layoutManager;
    private ChatListAdapter chatListAdapter;
    private ChatListAdapter favoriteAdapter;

    /**
     * 注销
     */
    private TextView logOutTextview;
    /**
     * 退出
     */
    private TextView exitTextview;
    /**
     * 修改
     */
    private TextView reviseInfomationTextview;

    private TextView mGenderTextview;
    private TextView mBirthdayTextview;
    private TextView mAgeTextview;
    private TextView mTagsTextview;
    private TextView mNicknameTextview;
    /**
     * 检查更新
     */
    private TextView checkUpdateTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
        setContentView(R.layout.activity_main);
        CommonUtils.addActivity(this);
        initView();
    }

    private void initView() {
        avatar_Imageview = findViewById(R.id.avatar_imageview);
        avatar_Imageview.setOnClickListener(this);
        avatarBackgroundImageview = findViewById(R.id.avatar_background_imageview);
        setBackground();//侧栏头像设置高斯模糊背景
        slidePanel = findViewById(R.id.slide_panel);
        avatar_main_Imageview = findViewById(R.id.avatar_main_imageview);
        avatar_main_Imageview.setOnClickListener(this);
        noFavoriteTextview = findViewById(R.id.no_favorite_textview);
        noRecordsTextview = findViewById(R.id.no_records_textview);
        slidePanel = findViewById(R.id.slide_panel);
        favoreitesRecyclerview = findViewById(R.id.favoreites_recyclerview);
        otherChatListRecyclerview = findViewById(R.id.other_chat_list_recyclerview);

        newChatActionbutton = findViewById(R.id.new_chat_actionbutton);
        newChatActionbutton.setOnClickListener(this);

        themeAImageview = findViewById(R.id.theme_a_imageview);
        themeAImageview.setOnClickListener(this);
        themeBImageview = findViewById(R.id.theme_b_imageview);
        themeBImageview.setOnClickListener(this);
        themeCImageview = findViewById(R.id.theme_c_imageview);
        themeCImageview.setOnClickListener(this);


        logOutTextview = findViewById(R.id.log_out_textview);
        logOutTextview.setOnClickListener(this);
        exitTextview = findViewById(R.id.exit_textview);
        exitTextview.setOnClickListener(this);
        reviseInfomationTextview = findViewById(R.id.revise_infomation_textview);
        reviseInfomationTextview.setOnClickListener(this);
        mGenderTextview = findViewById(R.id.gender_textview);
        mBirthdayTextview = findViewById(R.id.birthday_textview);
        mAgeTextview = findViewById(R.id.age_textview);
        mTagsTextview = findViewById(R.id.tags_textview);
        mNicknameTextview = findViewById(R.id.nickname_textview);

        initUserInfo();
        initChatList();
        initFavoriteList();
        checkUpdateTextview = (TextView) findViewById(R.id.check_update_textview);
        checkUpdateTextview.setOnClickListener(this);
    }

    /**
     * 收藏人物的初始化
     */
    private void initFavoriteList() {
        noFavoriteTextview.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ArrayList<Integer> avatars = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> lastMessages = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            avatars.add(R.mipmap.default_character_avatar);
            names.add("曾老师" + i);
            lastMessages.add("曾老师" + i + "是个畜生");
        }
        favoriteAdapter = new ChatListAdapter(avatars, names, lastMessages);
        favoriteAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });
        favoreitesRecyclerview.setHasFixedSize(true);
        favoreitesRecyclerview.setLayoutManager(layoutManager);
        favoreitesRecyclerview.setAdapter(favoriteAdapter);
    }

    //初始化聊天列表
    private void initChatList() {
        noRecordsTextview.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ArrayList<Integer> avatars = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> lastMessages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            avatars.add(R.mipmap.default_character_avatar);
            names.add("曾老师" + i);
            lastMessages.add("曾老师" + i + "是个傻逼");
        }
        chatListAdapter = new ChatListAdapter(avatars, names, lastMessages);
        chatListAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });
        otherChatListRecyclerview.setHasFixedSize(false);
        otherChatListRecyclerview.setLayoutManager(layoutManager);
        otherChatListRecyclerview.setAdapter(chatListAdapter);
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.avatar_main_imageview:
                slidePanel.openPane();
                break;
            case R.id.new_chat_actionbutton:
                startActivity(new Intent(MainActivity.this, NewChatActivity.class));
                break;
            case R.id.theme_a_imageview:
                CommonUtils.setTheme(this, CommonUtils.THEME_PINK);
                refreshTheme();
                break;
            case R.id.theme_b_imageview:
                CommonUtils.setTheme(this, CommonUtils.THEME_RED);
                refreshTheme();
                break;
            case R.id.theme_c_imageview:
                CommonUtils.setTheme(this, CommonUtils.THEME_BLUE);
                refreshTheme();
                break;
            case R.id.log_out_textview:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("确定要注销账号吗？");
                builder1.setPositiveButton("注销", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.logout(MainActivity.this);
                    }
                });
                builder1.create().show();
                break;
            case R.id.exit_textview:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("确定要退出吗？");
                builder2.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.exitApplication();
                    }
                });
                builder2.create().show();
                break;
            case R.id.revise_infomation_textview:
                Intent intent = new Intent(MainActivity.this, CompleteUserInfoActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);
                break;
            case R.id.check_update_textview:
                final ProgressDialog dialog = getProgressDialog("正在检测更新...");
                ServerUtils.checkUpdate(new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        dialog.dismiss();
                        if (msg.what == ServerUtils.FAILED){
                            toast("检查失败，请重试！");
                        }else {
                            if (msg.equals("1.0"))toast("当前已经是最新版本");
                            else toast("有新版本！");
                        }
                    }
                });
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

    //修改主题后刷新页面
    private void refreshTheme() {
        finish();
        final Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
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


    /**
     * 初始化用户资料
     */
    private void initUserInfo() {
        ServerUtils.getUsetInfo(CommonUtils.getCurrentUserEmail(this), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == ServerUtils.FAILED) {
                    toast("用户资料加载失败！");
                } else {
                    JSONObject object = JSONObject.parseObject(msg.obj.toString());
                    mNicknameTextview.setText(object.getString("nickName"));
                    mAgeTextview.setText(object.getString("age"));
                    mBirthdayTextview.setText(object.getString("birthday").split("T")[0]);
                    mGenderTextview.setText(Gender.GENDERS[Integer.parseInt(object.getString("gender"))]);
                    mTagsTextview.setText(object.getString("hobby"));
                    mTagsTextview.setText(object.getString("hobby"));
                    System.out.println(object.toJSONString());
                }
            }
        });
    }
}

package com.tql.huaweiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tql.huaweiapp.R;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backImageview;
    /**
     * 这是名字
     */
    private TextView characterNameTextview;
    private ImageView characterInfoImageview;
    private ImageView microPhoneImageview;
    private EditText messageEdittext;
    private LinearLayout inputAreaLinearlayout;
    private ImageView sendMessageImageview;
    private LinearLayout messageInputLinearlayout;
    private LinearLayout voiceRecordLinearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();


    }

    private void initView() {
        backImageview = findViewById(R.id.back_imageview);
        backImageview.setOnClickListener(this);
        characterNameTextview = findViewById(R.id.character_name_textview);
        characterInfoImageview = findViewById(R.id.character_info_imageview);
        characterInfoImageview.setOnClickListener(this);
        microPhoneImageview = findViewById(R.id.micro_phone_imageview);
        microPhoneImageview.setOnClickListener(this);
        messageEdittext = findViewById(R.id.message_edittext);
        messageEdittext.setOnClickListener(this);
        inputAreaLinearlayout = findViewById(R.id.input_area_linearlayout);
        sendMessageImageview = findViewById(R.id.send_message_imageview);
        sendMessageImageview.setOnClickListener(this);
        messageInputLinearlayout = findViewById(R.id.message_input_linearlayout);
        voiceRecordLinearlayout = findViewById(R.id.voice_record_linearlayout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.back_imageview:
                //销毁当前Activity
                finish();
                break;
            case R.id.character_info_imageview:
                startActivity(new Intent(ChatActivity.this,CharacterInfoActivity.class));
                break;
            case R.id.micro_phone_imageview:
                voiceRecordLinearlayout.setVisibility(View.VISIBLE);
                break;
            case R.id.send_message_imageview:
                // TODO: 2018/9/17 发送消息逻辑
                sendMessage();
                break;
            case R.id.message_edittext:
                voiceRecordLinearlayout.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 发送消息逻辑
     */
    private void sendMessage() {

    }
}

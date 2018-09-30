package com.tql.huaweiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tql.huaweiapp.R;
import com.tql.huaweiapp.adapter.ChatMessageAdapter;
import com.tql.huaweiapp.utils.CommonUtils;

import java.util.ArrayList;

import static com.tql.huaweiapp.utils.GetAnswer.GetAnswers;
import static com.tql.huaweiapp.utils.GetAnswer.PrettyPrint;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

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
    private LinearLayout record;
    private RecyclerView messageListRecyclerview;
    private ArrayList<Integer> avatars;
    private ArrayList<Integer> from;
    private ArrayList<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
        setContentView(R.layout.activity_chat);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        CommonUtils.addActivity(this);
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
        record = findViewById(R.id.record_linearlayout);
        record.setOnClickListener(this);
        record.setOnLongClickListener(this);
        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        messageListRecyclerview = findViewById(R.id.message_list_recyclerview);

        initChatList();
    }

    /**
     * 初始化聊天消息记录
     */
    private void initChatList() {
        avatars = new ArrayList<>();
        messages = new ArrayList<>();
        from = new ArrayList<>();
        for (String line : CommonUtils.getMessagesFromLocal(this, "1")) {
            if (Integer.valueOf(line.split("::")[0]).equals(ChatMessageAdapter.MY_MESSAGE)) {
                from.add(ChatMessageAdapter.MY_MESSAGE);
                avatars.add(R.mipmap.default_avatar);
            } else {
                from.add(ChatMessageAdapter.YOUR_MESSAGE);
                avatars.add(R.mipmap.default_character_avatar);
            }
            messages.add(line.split("::")[1]);
        }
        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(avatars, messages, from);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messageListRecyclerview.setHasFixedSize(false);
        messageListRecyclerview.setLayoutManager(layoutManager);
        messageListRecyclerview.setAdapter(chatMessageAdapter);
        messageListRecyclerview.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
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
                startActivity(new Intent(ChatActivity.this, CharacterInfoActivity.class));
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
            case R.id.record_linearlayout:
                Toast.makeText(this, "请长按麦克风按钮进行录音！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 发送消息逻辑
     */
    private void sendMessage() {
        String msg = messageEdittext.getText().toString();
        if (msg.isEmpty()) toast("内容不能为空！");
        else {
            avatars.add(R.mipmap.default_avatar);
            messages.add(msg);
            from.add(ChatMessageAdapter.MY_MESSAGE);
            CommonUtils.saveMessageToLocal(this, ChatMessageAdapter.MY_MESSAGE, msg, "1");
            try {
                String response = GetAnswers(msg);
                avatars.add(R.mipmap.default_character_avatar);
                String answer = getAnswer(PrettyPrint(response));
                messages.add(answer);
                from.add(ChatMessageAdapter.YOUR_MESSAGE);
                CommonUtils.saveMessageToLocal(this, ChatMessageAdapter.YOUR_MESSAGE, answer, "1");
            } catch (Exception e) {
                System.out.println(e);
            }

            messageListRecyclerview.setAdapter(new ChatMessageAdapter(avatars, messages, from));
            messageListRecyclerview.scrollToPosition(avatars.size() - 1);
            messageEdittext.setText("");
        }
    }

    /**
     * 获取回答
     *
     * @param s
     * @return
     */
    private String getAnswer(String s) {
        JSONObject data = JSON.parseObject(JSON.parseObject(s).getJSONArray("answers").getString(0));
        System.out.println(data);
        System.out.println("+++++++++"+data.getString("answer"));
        return data.getString("answer");
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.record_linearlayout:
                // TODO: 2018/9/17 开始录音

                break;
            default:
                break;
        }
        return true;
    }
}

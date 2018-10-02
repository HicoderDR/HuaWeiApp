package com.tql.huaweiapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tql.huaweiapp.R;
import com.tql.huaweiapp.adapter.ChatMessageAdapter;
import com.tql.huaweiapp.utils.CommonUtils;
import com.tql.huaweiapp.utils.ServerUtils;

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
    private String bot_id = "0";//正在聊天的对象id
    private String name = "null";//正在聊天的对象de ming zi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getTheme(this));
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        bot_id = intent.getStringExtra("bot_id");
        name = intent.getStringExtra("name");

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

        characterNameTextview.setText(name);

        initChatList();
    }

    /**
     * 初始化聊天消息记录
     */
    private void initChatList() {
        avatars = new ArrayList<>();
        messages = new ArrayList<>();
        from = new ArrayList<>();
        for (String line : CommonUtils.getMessagesFromLocal(this, bot_id)) {
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
//                startActivity(new Intent(ChatActivity.this, CharacterInfoActivity.class));
                menuClickEvent(v);
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
            CommonUtils.saveMessageToLocal(this, ChatMessageAdapter.MY_MESSAGE, msg, bot_id);
            try {
                String response = GetAnswers(msg);
                avatars.add(R.mipmap.default_character_avatar);
                String answer = getAnswer(PrettyPrint(response));
                messages.add(answer);
                from.add(ChatMessageAdapter.YOUR_MESSAGE);
                CommonUtils.saveMessageToLocal(this, ChatMessageAdapter.YOUR_MESSAGE, answer, bot_id);
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
        System.out.println("+++++++++" + data.getString("answer"));
        return data.getString("answer").trim();//去掉前后空格和换行
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

    /**
     * 弹出菜单
     *
     * @param v
     */
    private void menuClickEvent(final View v) {
        PopupMenu pm = new PopupMenu(this, v);
        pm.inflate(R.menu.character_info_menu);
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // TODO: 2018/9/17 添加“喜欢”逻辑
                switch (menuItem.getItemId()) {
                    case R.id.introduction:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                        View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.character_profile_card, null);
                        builder.setView(view);
                        builder.create().show();
                        break;
                    case R.id.favorite:
                        final ProgressDialog dialog = getProgressDialog("正在收藏...");
                        ServerUtils.setFavorite(CommonUtils.getCurrentUserEmail(ChatActivity.this), bot_id, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                dialog.dismiss();
                                if (msg.what == ServerUtils.FAILED) {
                                    toast("收藏失败，请重试！");
                                } else {
                                    toast("已收藏！");
                                }
                            }
                        });
                        break;
                }
                return true;
            }
        });
        pm.show();
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

package com.tql.huaweiapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tql.huaweiapp.R;

public class ChatHistoryCard extends CardView {
    private LinearLayout root;
    private ImageView avatar;
    private TextView name;
    private TextView lastMessage;

    public ChatHistoryCard(@NonNull Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.chat_history_card, this);
        initComponent(context);
    }

    private void initComponent(Context context) {
        root = findViewById(R.id.root_linearlayout);
        avatar = findViewById(R.id.avatar_imageview);
        name = findViewById(R.id.name_textview);
        lastMessage = findViewById(R.id.message_textview);
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar.setImageBitmap(avatar);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage.setText(lastMessage);
    }
}

package com.tql.huaweiapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tql.huaweiapp.R;

public class ChatHistoryCard extends View {
    private LinearLayout root;
    private ImageView avatar;
    private TextView name;
    private TextView lastMessage;
    private View view;

    public ChatHistoryCard(@NonNull Context context) {
        super(context);

        view = LayoutInflater.from(context).inflate(R.layout.chat_history_card, null);
        initComponent();
    }

    private void initComponent() {
        root = view.findViewById(R.id.root_linearlayout);
        avatar = view.findViewById(R.id.avatar_imageview);
        name = view.findViewById(R.id.name_textview);
        lastMessage = view.findViewById(R.id.message_textview);
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

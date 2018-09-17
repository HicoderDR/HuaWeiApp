package com.tql.huaweiapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tql.huaweiapp.R;

public class MessageView extends View {
    public static final int FROM_YOU = 0;
    public static final int FROM_ME = 1;
    private ImageView avatar;
    private TextView message;

    public MessageView(Context context, int from) {
        super(context);

        int resource = R.layout.my_message;
        if (from == FROM_YOU) resource = R.layout.your_message;
        LayoutInflater.from(context).inflate(resource, null);
        initComponent();
    }

    private void initComponent() {
        avatar = findViewById(R.id.avatar_imageview);
        message = findViewById(R.id.message_textview);
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar.setImageBitmap(avatar);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }
}

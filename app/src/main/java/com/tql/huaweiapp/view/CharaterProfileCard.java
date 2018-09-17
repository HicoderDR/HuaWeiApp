package com.tql.huaweiapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.tql.huaweiapp.R;

public class CharaterProfileCard extends CardView {
    private ImageView avatar;
    private TextView name;
    private TextView information;

    public CharaterProfileCard(@NonNull Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.character_profile_card, this);
        initComponent(context);
    }

    private void initComponent(Context context) {
        avatar = findViewById(R.id.avatar_imageview);
        name = findViewById(R.id.name_textview);
        information = findViewById(R.id.infomation_textview);
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar.setImageBitmap(avatar);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setInformation(String information) {
        this.information.setText(information);
    }
}

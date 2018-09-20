package com.tql.huaweiapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.tql.huaweiapp.R;

public class WaitingProgressBar extends View{
    public WaitingProgressBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.waiting_progress_dialog,null);
    }
}

package com.tql.huaweiapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tql.huaweiapp.R;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {
    public static final int MY_MESSAGE = 1;
    public static final int YOUR_MESSAGE = 0;
    private ArrayList<Integer> avatars;
    private ArrayList<String> messages;

    public ChatMessageAdapter(ArrayList<Integer> avatars, ArrayList<String> messages) {
        this.avatars = avatars;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        //假设前提是，用户发一条消息，服务器端必然返回一条消息，即用户的消息在所有为0,2，...，2n的Item位置
        if ((position & 1) == 0) return MY_MESSAGE;
        else return YOUR_MESSAGE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == MY_MESSAGE)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_message, viewGroup, false);
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.your_message, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.avatar.setImageResource(avatars.get(position));
        viewHolder.message.setText(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar_imageview);
            message = itemView.findViewById(R.id.message_content_textview);
        }
    }
}

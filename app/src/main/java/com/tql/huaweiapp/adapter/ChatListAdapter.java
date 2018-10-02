package com.tql.huaweiapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tql.huaweiapp.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private ArrayList<Integer> avatars;
    private ArrayList<String> names;
    private ArrayList<String> lastMessages;
    private ArrayList<String> bot_ids;
    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onItemLongClickListener;

    public ChatListAdapter(ArrayList<Integer> avatars, ArrayList<String> names, ArrayList<String> lastMessages, ArrayList<String> bot_ids) {
        this.avatars = avatars;
        this.names = names;
        this.lastMessages = lastMessages;
        this.bot_ids = bot_ids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_history_card, parent, false);
        return new ViewHolder(view, onItemClickListener, onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.avatar.setImageResource(avatars.get(position));
        holder.nickname.setText(names.get(position));
        holder.lastMessage.setText(lastMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnLongClickListener longClickListener) {
        onItemLongClickListener = longClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView avatar;
        private TextView nickname;
        private TextView lastMessage;
        private OnItemClickListener onItemClickListener;
        private OnLongClickListener onItemLongClickListener;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongClickListener onItemLongClickListener) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar_imageview);
            nickname = itemView.findViewById(R.id.name_textview);
            lastMessage = itemView.findViewById(R.id.message_textview);
            onItemClickListener = itemClickListener;
            this.onItemLongClickListener = onItemLongClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onItemLongClickListener.onItemLongClick(v, getLayoutPosition());
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnLongClickListener {
        void onItemLongClick(View v, int position);
    }
}

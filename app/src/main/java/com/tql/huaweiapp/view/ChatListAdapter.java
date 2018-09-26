package com.tql.huaweiapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tql.huaweiapp.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private ArrayList<Integer> avatars;
    private ArrayList<String> names;
    private ArrayList<String> lastMessages;
    private OnItemClickListener onItemClickListener;

    public ChatListAdapter(ArrayList<Integer> avatars, ArrayList<String> names, ArrayList<String> lastMessages) {
        this.avatars = avatars;
        this.names = names;
        this.lastMessages = lastMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_history_card, parent, false);
        return new ViewHolder(view, onItemClickListener);
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

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView avatar;
        private TextView nickname;
        private TextView lastMessage;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar_imageview);
            nickname = itemView.findViewById(R.id.name_textview);
            lastMessage = itemView.findViewById(R.id.message_textview);
            onItemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}

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

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.ViewHolder> {
    private ArrayList<Integer> avatars;
    private ArrayList<String> names;
    private ArrayList<String> informations;
    private OnItemClickListener onItemClickListener;

    public CharacterListAdapter(ArrayList<Integer> avatars, ArrayList<String> names, ArrayList<String> informations) {
        this.avatars = avatars;
        this.names = names;
        this.informations = informations;
    }

    @NonNull
    @Override
    public CharacterListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.character_profile_card, viewGroup, false);
        return new ViewHolder(view,onItemClickListener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(names.get(i));
        viewHolder.avatar.setImageResource(avatars.get(i));
        viewHolder.information.setText(informations.get(i));
    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView avatar;
        private TextView name;
        private TextView information;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView,OnItemClickListener itemClickListener) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar_imageview);
            name = itemView.findViewById(R.id.name_textview);
            information = itemView.findViewById(R.id.infomation_textview);
            onItemClickListener = itemClickListener;
            avatar.setOnClickListener(this);
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

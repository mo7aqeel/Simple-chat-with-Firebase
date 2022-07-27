package com.ali.medicalchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterViewHolder> {

    ArrayList<ChatMessage> list = new ArrayList<>();
    Context context;

    public ChatAdapter(ArrayList<ChatMessage> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message, parent, false);
        return new ChatAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapterViewHolder holder, int position) {
        holder.messageUser.setText(list.get(position).getMessageUser());
        holder.messageTime.setText(list.get(position).getMessageTime());
        holder.messageText.setText(list.get(position).getMessageText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChatAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView messageUser, messageTime, messageText;

        public ChatAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            messageUser = itemView.findViewById(R.id.message_user);
            messageTime = itemView.findViewById(R.id.message_time);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }
}

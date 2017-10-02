package com.example.ahmed.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.model.Message;
import com.example.ahmed.chat.model.MyAccount;

import java.util.List;

/**
 * Created by ahmed on 02/10/17.
 */

public class ItemChatAdapter extends RecyclerView.Adapter<ItemChatAdapter.MyViewHolder> {

    private List<Message> messageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message;

        public MyViewHolder(View view) {
            super(view);
            message = (TextView) view.findViewById(R.id.tvt_msg);
        }
    }


    public ItemChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.message.setText(message.getMessageText());
        if(message.getFromId().equals(MyAccount.getId())){
            holder.message.setBackgroundResource(R.drawable.rounded_blue_button);
        }else {
            holder.message.setBackgroundResource(R.drawable.rounded_gray_button);
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    public void addItem(Message message){
        messageList.add(message);
        notifyDataSetChanged();
    }
}
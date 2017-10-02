package com.example.ahmed.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.model.Message;
import com.example.ahmed.chat.model.MyAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 02/10/17.
 */
/*
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
        return 5;
    }
    public void updateMessage(ArrayList<Message> messageList){
        this.messageList=messageList;
        notifyDataSetChanged();
    }
}*/

public class ItemChatAdapter extends RecyclerView.Adapter<ItemChatAdapter.CutomViewHolder> {
    ArrayList<Message> myarraylist;
    private RelativeLayout.LayoutParams rightRelativeLayout,leftRelativeLayout;
    // constructor to send data and set it global
    public ItemChatAdapter(){
    }
    @Override
    public ItemChatAdapter.CutomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,null);
        CutomViewHolder cutomViewHolder = new CutomViewHolder(view);
        return  cutomViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemChatAdapter.CutomViewHolder holder, int position) {

        Message message = myarraylist.get(position);
        holder.message.setText(message.getMessageText());

        if(message.getFromId().equals(MyAccount.getId())){
            holder.message.setBackgroundResource(R.drawable.rounded_blue_button);
            holder.container.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }else {
            holder.message.setBackgroundResource(R.drawable.rounded_gray_button);
            holder.container.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public int getItemCount() {
        return myarraylist.size();
    }
    class CutomViewHolder extends RecyclerView.ViewHolder{
        //inner class to link java with item in layout
        TextView message;
        RelativeLayout container;
        ImageView image;
        public CutomViewHolder(View view){
            super(view);
            message = view.findViewById(R.id.tvt_msg);
            container=view.findViewById(R.id.container);
            image=view.findViewById(R.id.image);
        }
    }
    public void addMessage(Message message){
        if(myarraylist == null)
            myarraylist = new ArrayList<>();
        myarraylist.add(message);
        notifyDataSetChanged();
    }
}
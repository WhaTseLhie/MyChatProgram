package com.example.jayvee.mychatprogram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    Context context;
    ArrayList<ChatMessage> list;
    LayoutInflater inflater;

    public ChatAdapter(Context context, ArrayList<ChatMessage> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View arg1, ViewGroup parent) {
        ChatHandler handler = null;

        if(arg1 == null) {
            if(position%2 == 0) {
                arg1 = this.inflater.inflate(R.layout.chatlayout, null);
            } else {
                arg1 = this.inflater.inflate(R.layout.chatlayoutright, null);
            }
            handler = new ChatHandler();
            handler.sender = (TextView) arg1.findViewById(R.id.textView1);
            handler.message = (TextView) arg1.findViewById(R.id.textView2);
            arg1.setTag(handler);
        } else
            handler = (ChatHandler) arg1.getTag();

        handler.sender.setText(list.get(position).getSender());
        handler.message.setText(list.get(position).getMessage());

        return arg1;
    }

    static class ChatHandler {
        TextView sender, message;
    }
}

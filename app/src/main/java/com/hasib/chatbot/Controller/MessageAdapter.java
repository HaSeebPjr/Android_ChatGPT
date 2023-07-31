package com.hasib.chatbot.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hasib.chatbot.Model.Message;
import com.hasib.chatbot.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private List<Message> messageList;
    public MessageAdapter(List<Message> messageList){
        this.messageList = messageList;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        if(message.getSender().equals(Message.MY_MSG)){
            holder.leftChat.setVisibility(View.GONE);
            holder.leftMsgTime.setVisibility(View.GONE);
            holder.rightMsgTime.setVisibility(View.VISIBLE);
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.rightChatText.setText(message.getMessage());
            holder.rightMsgTime.setText(message.getCurrentTime());
        }else {
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.leftMsgTime.setVisibility(View.VISIBLE);
            holder.rightMsgTime.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.GONE);
            holder.leftChatText.setText(message.getMessage());
            holder.leftMsgTime.setText(message.getCurrentTime());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout leftChat, rightChat;
        private TextView leftChatText, rightChatText, leftMsgTime, rightMsgTime;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            leftChat = itemView.findViewById(R.id.leftChat);
            rightChat = itemView.findViewById(R.id.rightChat);
            leftChatText = itemView.findViewById(R.id.leftChatText);
            rightChatText = itemView.findViewById(R.id.rightChatText);
            leftMsgTime = itemView.findViewById(R.id.leftMsgTime);
            rightMsgTime = itemView.findViewById(R.id.rightMsgTime);
        }
    }
}

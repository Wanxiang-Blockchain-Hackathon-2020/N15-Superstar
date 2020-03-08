package com.example.chinacompetition.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.R;
import com.example.chinacompetition.contract.CompletedContractActivity;
import com.example.chinacompetition.contract.ContractActivity;
import com.example.chinacompetition.contract.ContractDetailActivity;
import com.example.chinacompetition.contract.TemporaryAccountActivity;


import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    /**  Message list Adapter  ***/

    private final ArrayList<MessageList> messageArrayList ;
    private MessageClickListener messageClickListener;
    private Context context;

    public MessageAdapter(ArrayList<MessageList> messageArrayList, Context context) {
        this.messageArrayList = messageArrayList;
        this.context = context;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        protected TextView id ;
        protected TextView name ;
        protected Button contract;
        protected Button contractDetail;
        protected Button temporayAccount;
        protected Button Completed;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            this.id =  itemView.findViewById(R.id.text_item_list_message_id);
            this.name =  itemView.findViewById(R.id.text_item_list_message_name);
            this.contract =  itemView.findViewById(R.id.btn_item_list_message_contract);
            this.contractDetail =  itemView.findViewById(R.id.btn_item_list_message_contract_detail);
            this.temporayAccount =  itemView.findViewById(R.id.btn_item_list_message_temporary_account);
            this.Completed =  itemView.findViewById(R.id.btn_item_list_message_completed);


        }


    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {



        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_message, viewGroup, false);


        MessageViewHolder messageViewHolder = new MessageViewHolder(view);


        return messageViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, final int position) {
        messageViewHolder.id.setText(messageArrayList.get(position).getId());
        messageViewHolder.name.setText(messageArrayList.get(position).getName());
        messageViewHolder.contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContractActivity.class);
                intent.putExtra("messageId", messageArrayList.get(position).getId());
                intent.putExtra("messageName", messageArrayList.get(position).getName());
                intent.putExtra("loginedId", CommonValue.getId());
                view.getContext().startActivity(intent);
            }
        });
        messageViewHolder.contractDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContractDetailActivity.class);
                intent.putExtra("messageId", messageArrayList.get(position).getId());
                intent.putExtra("messageName", messageArrayList.get(position).getName());
                intent.putExtra("loginedId", CommonValue.getId());
                view.getContext().startActivity(intent);
            }
        });
        String loginedWho = CommonValue.getLoginedCLIENTFREELANCER();
        if(loginedWho.equals("freelancer")){
            messageViewHolder.temporayAccount.setVisibility(View.GONE);
            messageViewHolder.Completed.setVisibility(View.GONE);
        }else if(loginedWho.equals("client")){
            messageViewHolder.temporayAccount.setVisibility(View.VISIBLE);
            messageViewHolder.Completed.setVisibility(View.VISIBLE);
            messageViewHolder.temporayAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TemporaryAccountActivity.class);
                    intent.putExtra("messageId", messageArrayList.get(position).getId());
                    intent.putExtra("messageName", messageArrayList.get(position).getName());
                    intent.putExtra("loginedId", CommonValue.getId());
                    view.getContext().startActivity(intent);
                }
            });
            messageViewHolder.Completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), CompletedContractActivity.class);
                    intent.putExtra("messageId", messageArrayList.get(position).getId());
                    intent.putExtra("messageName", messageArrayList.get(position).getName());
                    intent.putExtra("loginedId", CommonValue.getId());
                    view.getContext().startActivity(intent);
                }
            });
        }



        // 클릭이벤트
        if(messageClickListener != null){
            final int pos = position;
            messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageClickListener.onItemClicked(pos);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


    public interface MessageClickListener {

        // 클릭이벤트
        void onItemClicked(int position);
    }

    public void setMessageClickListener(MessageClickListener messageClickListener) {
        this.messageClickListener = messageClickListener;
    }
}


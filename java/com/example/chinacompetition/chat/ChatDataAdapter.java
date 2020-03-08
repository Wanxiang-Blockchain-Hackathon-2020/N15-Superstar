package com.example.chinacompetition.chat;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chinacompetition.R;

import java.util.ArrayList;

import javax.sql.DataSource;

public class ChatDataAdapter  extends RecyclerView.Adapter<ChatDataAdapter.ItemViewHolder> {

    private ArrayList<ChatItem> chatItems;
    private OnItemClickListener mListener = null;
    boolean check;

    Context context;


    //테마데이터 어댑터 생성자
    public ChatDataAdapter(Context context, ArrayList<ChatItem> chatItems) {

        this.context = context;
        this.chatItems = chatItems;

    }

    //
    @Override
    public ChatDataAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        ChatDataAdapter.ItemViewHolder viewHolder = new ChatDataAdapter.ItemViewHolder(view); // 뷰객체를 파라미터로 받아 뷰 홀더객체를 생성

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatDataAdapter.ItemViewHolder holder, int position) {


        //상대방이 나에게 말한거면
        if(!chatItems.get(position).getId().equals("me")) {
            // Glide.with(context).load(Uri.parse(chatItems.get(position).getThumbnail())).into(holder.thumbnailView);
            holder.idView.setText(chatItems.get(position).getId());
            holder.contentView.setText(chatItems.get(position).getContent());


            //내가 상대방에게 말한거면
        }else{

            //상대방의 대화 아이템을 가린다.
            holder.contentView.setVisibility(View.INVISIBLE);
            holder.idView.setVisibility(View.INVISIBLE);


            //내 대화 아이템을 보이게 한다.

            holder.onesContent.setVisibility(View.VISIBLE);

            //holder.idView.setText(chatItems.get(position).getId());
            holder.onesContent.setText(chatItems.get(position).getContent());

        }

    }


    @Override
    public int getItemCount() {//어댑터에서 관리하는 리스트에 저장된 아이템의 갯수를 확인 = 리스트의 크기를 확인
        return chatItems.size();
    }


    public void addItem(ChatItem item) {
        chatItems.add(item);
    }

    public void setItems(ArrayList<ChatItem> Item) {
        this.chatItems = Item;
    }

    public ChatItem getItem(int position) {
        return chatItems.get(position);
    }

    public void setItem(int position, ChatItem item) {
        chatItems.set(position, item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    //테마 아이템뷰 정의 부분
    public class ItemViewHolder extends RecyclerView.ViewHolder {


        private ImageView thumbnailView, onesThumbnail;
        private TextView idView, contentView, onesContent;


        public ItemViewHolder(final View itemView) {
            super(itemView);

       /*     v = itemView.findViewById(R.id.videoView);
            this.videoThumbnail =  itemView.findViewById(R.id.videoThumbnail);
            this.titleView= itemView.findViewById(R.id.titleView);
            this.explainView= itemView.findViewById(R.id.explainView);
            this.videoView = itemView.findViewById(R.id.videoView);*/

            //상대방의 대화내용과 상대방의 정보를 담을 아이템

            idView = itemView.findViewById(R.id.idView);
            contentView = itemView.findViewById(R.id.contentView);

            //내 정보와 내 대화내용을 담을 아이템

            onesContent = itemView.findViewById(R.id.onesContent);



        }
    }

    // 커스텀 리스너 인터페이스
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

}
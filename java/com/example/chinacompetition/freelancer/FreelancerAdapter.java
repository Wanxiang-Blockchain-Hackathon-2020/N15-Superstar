package com.example.chinacompetition.freelancer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chinacompetition.R;

import java.util.ArrayList;

public class FreelancerAdapter extends RecyclerView.Adapter<FreelancerAdapter.FreelancerViewHolder> {
    /**  프리랜서 목록 Adapter  ***/

    private final ArrayList<FreelancerList> freelancerArrayList ;
    private FreelancerClickListener freelancerClickListener;
    private Context context;

    public FreelancerAdapter(ArrayList<FreelancerList> freelancerArrayList, Context context) {
        this.freelancerArrayList = freelancerArrayList;
        this.context = context;
    }

    public class FreelancerViewHolder extends RecyclerView.ViewHolder {

        protected TextView title ;
        protected TextView name ;
        protected ImageView image ;
        protected TextView rate ;
        protected TextView hiredNumber ;


        public FreelancerViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title =  itemView.findViewById(R.id.text_item_list_freelancer_title);
            this.name =  itemView.findViewById(R.id.text_item_list_freelancer_name);
            this.image =  itemView.findViewById(R.id.image_item_list_freelancer_image);
            this.rate =  itemView.findViewById(R.id.text_item_list_freelancer_rate);
            this.hiredNumber =  itemView.findViewById(R.id.text_item_list_freelancer_numberOfhired);

        }


    }


    @NonNull
    @Override
    public FreelancerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {



        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_freelancer, viewGroup, false);


        FreelancerViewHolder freelancerViewHolder = new FreelancerViewHolder(view);


        return freelancerViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull FreelancerViewHolder freelancerViewHolder, int position) {
        freelancerViewHolder.title.setText(freelancerArrayList.get(position).getTitle());
        freelancerViewHolder.name.setText(freelancerArrayList.get(position).getName());
        //Glide을 이용해서 이미지뷰에 url에 있는 이미지를 세팅해줌
        Glide.with(context).load(freelancerArrayList.get(position).getImage()).into(freelancerViewHolder.image);
        freelancerViewHolder.rate.setText(freelancerArrayList.get(position).getRate());
        freelancerViewHolder.hiredNumber.setText("고용회수:"+freelancerArrayList.get(position).getHiredNumber()+"회");

        // 클릭이벤트
        if(freelancerClickListener != null){
            final int pos = position;
            freelancerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    freelancerClickListener.onItemClicked(pos);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return freelancerArrayList.size();
    }


    public interface FreelancerClickListener {

        // 클릭이벤트
        void onItemClicked(int position);
    }

    public void setFreelancerClickListener(FreelancerClickListener freelancerClickListener) {
        this.freelancerClickListener = freelancerClickListener;
    }
}


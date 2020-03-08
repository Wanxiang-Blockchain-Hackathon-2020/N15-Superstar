package com.example.chinacompetition.PostJobDir;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chinacompetition.R;
import com.example.chinacompetition.RecyclerViewClickListener;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class JobsListAdapter extends RecyclerView.Adapter<JobsListAdapter.ViewHolder> implements RecyclerViewClickListener {

    public ArrayList<JobsListData> items = new ArrayList<>();

    RecyclerViewClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_jobs_list2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobsListData item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(RecyclerViewClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder viewHolder, View view, int position) {
        if(listener != null){
            listener.onItemClick(viewHolder, view, position);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvSubject, tvCategory, tvTerm, tvCost ,tvStage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvTerm = itemView.findViewById(R.id.tvTerm);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvStage = itemView.findViewById(R.id.tvStage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }


        public void setItem(JobsListData item){
            tvSubject.setText(item.getSubject());
            tvCategory.setText(item.getCategory());
            tvTerm.setText(item.getTerm());
//            tvStage.setText(item.getStage());

            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            tvCost.setText(decimalFormat.format(Integer.parseInt(item.getCost()))+" 원");
        }
    }

    public void addItem(JobsListData item){
        items.add(item);
    }

    public ArrayList<JobsListData> getItems() {
        return items;
    }

    public void setItems(ArrayList<JobsListData> items) {
        this.items = items;
    }

    public JobsListData getItem(int position){
        return items.get(position);
    }
}

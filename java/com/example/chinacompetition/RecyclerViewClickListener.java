package com.example.chinacompetition;

import android.view.View;

import com.example.chinacompetition.PostJobDir.JobsListAdapter;


public interface RecyclerViewClickListener {

    void onItemClick(JobsListAdapter.ViewHolder viewHolder, View view, int position);
}

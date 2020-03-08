package com.example.chinacompetition.PostJobDir;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RecyclerViewClickListener;
import com.example.chinacompetition.RetrofitService;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JobsListActivity extends AppCompatActivity {

//    JobsListAdapter adapter = new JobsListAdapter();
//
//    private String TAG = "JobsListActivity";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_jobs_list);
//
//        Log.i(TAG, "JobsListActivity");
//
//        // 테스트를 위해 임시로 만들어 놓은 버튼
//        Button button = findViewById(R.id.btnPost);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), PostJobActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        RecyclerView recyclerView = findViewById(R.id.jobslist);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//
//        adapter.setOnItemClickListener(new RecyclerViewClickListener() {
//            @Override
//            public void onItemClick(JobsListAdapter.ViewHolder viewHolder, View view, int position) {
//                Log.i(TAG, "onItemClick: "+position);
//
//                JobsListData item = adapter.getItem(position);
//                HashMap hashMap = item.makeMap();
//                Intent intent = new Intent(getApplicationContext(), JobDetailActivity.class);
//                intent.putExtra("hashmap", hashMap);
//
//                startActivity(intent);
//            }
//        });
//
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        loadPosts("all");
//    }
//
//    private void loadPosts(String category){
//        adapter.items.clear();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(CommonValue.getServerURL())
//                .build();
//
//        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
//        Call<ResponseBody> call = retrofitService.loadJobs(category);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.i(TAG, "onResponse: "+response);
//
//                if (response.isSuccessful()){
//                    try {
//                        String result = response.body().string();
//                        if(!result.equals("err")){
//                            JSONArray jsonArray = new JSONArray(result);
//
//                            for(int i = 0 ; i<jsonArray.length() ; i++){
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                                JobsListData item = new JobsListData(jsonObject.getString("num"), jsonObject.getString("id"), jsonObject.getString("subject"),
//                                        jsonObject.getString("category"), jsonObject.getString("term"), jsonObject.getString("cost"), jsonObject.getString("content"));
//
//                                adapter.addItem(item);
//                                adapter.notifyDataSetChanged();
//                            }
//
//                        }else{
//                            Toast.makeText(getApplicationContext(), "서버에 에러가 있습니다.", Toast.LENGTH_SHORT).show();
//                            Log.i(TAG, "unSuccessful");
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    Toast.makeText(getApplicationContext(), "응답 받기 실패", Toast.LENGTH_SHORT).show();
//                    Log.i(TAG, "unSuccessful");
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "보내기 실패", Toast.LENGTH_SHORT).show();
//                Log.i(TAG, "onFailure: "+t);
//            }
//        });
//    }
}

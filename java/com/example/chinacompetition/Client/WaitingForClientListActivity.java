package com.example.chinacompetition.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.PostJobDir.JobDetailActivity;
import com.example.chinacompetition.PostJobDir.JobsListAdapter;
import com.example.chinacompetition.PostJobDir.JobsListData;
import com.example.chinacompetition.PostJobDir.PostJobActivity;
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

public class WaitingForClientListActivity extends AppCompatActivity {

    /******** 접속한 유저의 작업의뢰내역 목록 ***********/
    JobsListAdapter adapter = new JobsListAdapter();
    LinearLayout linear_client_bar ;
    private String TAG = "WaitingForClientListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_client_list);


    }
}

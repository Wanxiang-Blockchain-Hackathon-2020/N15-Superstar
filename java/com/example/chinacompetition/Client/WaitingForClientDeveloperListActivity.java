package com.example.chinacompetition.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.PostJobDir.JobDetailActivity;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RetrofitService;
import com.example.chinacompetition.freelancer.FreelancerAdapter;
import com.example.chinacompetition.freelancer.FreelancerDetailActivity;
import com.example.chinacompetition.freelancer.FreelancerList;
import com.example.chinacompetition.freelancer.WaitingForFreelancerClientDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WaitingForClientDeveloperListActivity extends AppCompatActivity implements FreelancerAdapter.FreelancerClickListener {

    /******** 의뢰한 프로젝트 신청한 프리랜서화면 ***********/
    // 등록된 프리랜서목록을 불러온다.


    // Freelancer Recylcerview
    private ArrayList<FreelancerList> freelancerArrayList; // 모임정보 값
    private FreelancerAdapter freelancerAdapter;
    private RecyclerView freelancerRecyclerView;

    private static String IP_ADDRESS = "34.64.144.139";
    private static String TAG = "프리랜서화면 ";
    private TextView mTextViewResult;
    // 프리랜서목록불러오는 변수들
    String mJsonString ;
    // 프리랜서목록불러오는 변수들끝
    Bitmap bmImg;
    ImageView image_waiting_for_client_developer_list_back;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CommonValue.getServerURL())
            .build();
    RetrofitService retrofitService = retrofit.create(RetrofitService.class);

    // Choosed Project number,id
    String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_client_developer_list);
        freelancerRecyclerView = (RecyclerView) findViewById(R.id.recycle_home_brand1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        freelancerRecyclerView.setLayoutManager(linearLayoutManager);
        freelancerArrayList = new ArrayList<>();
        freelancerRecyclerView.setHasFixedSize(true);
        freelancerAdapter = new FreelancerAdapter(freelancerArrayList,WaitingForClientDeveloperListActivity.this);
        freelancerRecyclerView.setAdapter(freelancerAdapter);
        freelancerAdapter.setFreelancerClickListener(WaitingForClientDeveloperListActivity.this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(freelancerRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        freelancerRecyclerView.addItemDecoration(dividerItemDecoration);
        Log.e("tag","freelancerlist size"+freelancerArrayList.size());
        image_waiting_for_client_developer_list_back = (ImageView) findViewById(R.id.image_waiting_for_client_developer_list_back);
        Intent intent = getIntent();
        String jobId = intent.getStringExtra("jobId");
        num = intent.getStringExtra("num");

        // back
        image_waiting_for_client_developer_list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(WaitingForClientDeveloperListActivity.this, JobDetailActivity.class);
                startActivity(intent1);
            }
        });

        selectBidderList(jobId);

        WaitingForClientDeveloperListActivity.GetAllCompanyData getAllCompanyData = new WaitingForClientDeveloperListActivity.GetAllCompanyData();
        getAllCompanyData.execute("http://" + IP_ADDRESS + "/contract/selectBidderList.php",jobId);
    }

    private void selectBidderList(String jobId){
        Call<ResponseBody> call = retrofitService.selectBidderList(jobId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse:"+response);

                try {
                    String result = response.body().string();
                    Log.e(TAG, "result:"+result);

                    if(response.isSuccessful()){
                        Log.e(TAG, "isSuccessful");
                        try {
                            JSONArray jsonArray = new JSONArray(result);

                            for (int i = 0; i < jsonArray.length() ; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.e("tag","jsonArray.length()"+jsonArray.length());
                                Log.e(TAG, "in try"+jsonArray.toString()+jsonObject.toString());
                                String title = jsonObject.getString("title");
                                String name = jsonObject.getString("name");
                                String image = jsonObject.getString("image");
                                String rate = jsonObject.getString("rate");
                                String hiredNumber = jsonObject.getString("hiredNumber");
                                String location = jsonObject.getString("location");
                                String skill = jsonObject.getString("skill");
                                String introduce = jsonObject.getString("introduce");
                                String review = jsonObject.getString("review");
                                String id = jsonObject.getString("id");
                                String phonenum = jsonObject.getString("phonenum");
                                FreelancerList freelancerList = new FreelancerList();
                                freelancerList.setTitle(title);
                                freelancerList.setName(name);
                                freelancerList.setImage(image);
                                freelancerList.setRate(rate);
                                freelancerList.setHiredNumber(hiredNumber);
                                freelancerList.setLocation(location);
                                freelancerList.setSkill(skill);
                                freelancerList.setIntroduce(introduce);
                                freelancerList.setReview(review);
                                freelancerList.setId(id);

                                Log.e("tag","freelancerlist size_json_before"+freelancerArrayList.size());
                                freelancerArrayList.add(freelancerList);
                                Log.e("tag","freelancerlist size_json_after"+freelancerArrayList.size());
                                freelancerAdapter.notifyItemInserted(0);
                            }




                        } catch (JSONException e) {

                            Log.d(TAG, "showResult : ", e);
                        }

                    }else{
                        Log.e(TAG, "unSuccessful");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure:"+t);
            }
        });

    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(getApplicationContext(), position + " 선택됨", Toast.LENGTH_SHORT).show();
        Intent intentItemList = new Intent(WaitingForClientDeveloperListActivity.this, WaitingForClientDeveloperDetailActivity.class);
        intentItemList.putExtra("freelancerPosition",position);
        intentItemList.putExtra("freelancerTitle",freelancerArrayList.get(position).getTitle());
        intentItemList.putExtra("freelancerName",freelancerArrayList.get(position).getName());
        intentItemList.putExtra("freelancerImage",freelancerArrayList.get(position).getImage());
        intentItemList.putExtra("freelancerRate",freelancerArrayList.get(position).getRate());
        intentItemList.putExtra("freelancerHiredNumber",freelancerArrayList.get(position).getHiredNumber());
        intentItemList.putExtra("freelancerLocation",freelancerArrayList.get(position).getLocation());
        intentItemList.putExtra("freelancerSkill",freelancerArrayList.get(position).getSkill());
        intentItemList.putExtra("freelancerIntroduce",freelancerArrayList.get(position).getIntroduce());
        intentItemList.putExtra("freelancerReview",freelancerArrayList.get(position).getReview());
        // Choosed Project number,id
        intentItemList.putExtra("num", num);
        intentItemList.putExtra("id", freelancerArrayList.get(position).getId());

        startActivity(intentItemList);
    }

    private class GetAllCompanyData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(WaitingForClientDeveloperListActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
//            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);
            Log.e(TAG, "response - " + result);
            if (result == null){

//                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult2();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = params[0];
            String jobId = params[1];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
            String postParameters = "jobId=" + jobId;
            Log.e(TAG,"jobId=" + jobId);
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();




                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                Log.e(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

//                bmImg = BitmapFactory.decodeStream(inputStream);


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult2() {
        Log.e(TAG, "showResult2들어옴  - " + mJsonString);
        String TAG_TITLE = "title";
        String TAG_NAME = "name";
        String TAG_IMAGE ="image";
        String TAG_RATE = "rate";
        String TAG_HIREDNUMBER = "hiredNumber";
        String TAG_LOCATION = "location";
        String TAG_SKILL = "skill";
        String TAG_INTRODUCE = "introduce";
        String TAG_REVIEW = "review";

        try {
            JSONArray jsonArray = new JSONArray(mJsonString);

            for (int i = 0; i < jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.e("tag","jsonArray.length()"+jsonArray.length());
                Log.e(TAG, "in try"+jsonArray.toString()+jsonObject.toString());
                String title = jsonObject.getString("title");
                String name = jsonObject.getString("name");
                String image = jsonObject.getString("image");
//                String rate = jsonObject.getString("rate");
                String hiredNumber = jsonObject.getString("hiredNumber");
                String location = jsonObject.getString("location");
                String skill = jsonObject.getString("skill");
                String introduce = jsonObject.getString("introduce");
//                String review = jsonObject.getString("review");
                FreelancerList freelancerList = new FreelancerList();
                freelancerList.setTitle(title);
                freelancerList.setName(name);
                freelancerList.setImage(image);
//                freelancerList.setRate(rate);
                freelancerList.setHiredNumber(hiredNumber);
                freelancerList.setLocation(location);
                freelancerList.setSkill(skill);
                freelancerList.setIntroduce(introduce);
//                freelancerList.setReview(review);
                Log.e("tag","freelancerlist size_json_before"+freelancerArrayList.size());
                freelancerArrayList.add(freelancerList);
                Log.e("tag","freelancerlist size_json_after"+freelancerArrayList.size());
                freelancerAdapter.notifyItemInserted(0);
            }




        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }



}

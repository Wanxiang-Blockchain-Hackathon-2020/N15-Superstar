package com.example.chinacompetition.PostJobDir;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chinacompetition.Bidding.BidderList;
import com.example.chinacompetition.Client.WaitingForClientDeveloperListActivity;
import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RetrofitService;
import com.example.chinacompetition.bottom_navigation.ClientActivity;
import com.example.chinacompetition.bottom_navigation.FreelancerActivity;
import com.example.chinacompetition.freelancer.FreelancerAdapter;
import com.example.chinacompetition.freelancer.FreelancerDetailActivity;
import com.example.chinacompetition.freelancer.FreelancerList;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JobDetailActivity extends AppCompatActivity {

    private String TAG = "JobDetailActivity";
    private static String IP_ADDRESS = "34.64.144.139";
    TextView tvSubject, tvCategory, tvTerm, tvCost, tvContent, tvNumBidder, tvShowBidder;
    Button btnEdit, btnBidding;

    private String num, id, subject, category, term, cost, content, bidderNum;

    private int REQUEST_CODE = 100;

    DecimalFormat decimalFormat;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CommonValue.getServerURL())
            .build();

    RetrofitService retrofitService = retrofit.create(RetrofitService.class);
    ImageView image_job_detail_back ;

    // Freelancer Recylcerview
//    private ArrayList<FreelancerList> freelancerArrayList; // 모임정보 값
//    private FreelancerAdapter freelancerAdapter;
//    private RecyclerView freelancerRecyclerView;

    // 프리랜서목록불러오는 변수들
    String mJsonString ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        tvSubject = findViewById(R.id.tvSubject);
        tvCategory = findViewById(R.id.tvCategory);
        tvTerm = findViewById(R.id.tvTerm);
        tvCost = findViewById(R.id.tvCost);
        tvContent = findViewById(R.id.tvContent);
        btnEdit = findViewById(R.id.btnEdit);
        btnBidding = findViewById(R.id.btnBidding);
        tvNumBidder = findViewById(R.id.tvNumBidder);
        tvShowBidder = findViewById(R.id.tvShowBidder);
        image_job_detail_back = (ImageView) findViewById(R.id.image_job_detail_back);
        // 인텐트로 넘겨받은 값들을 불러와서 뷰에 뿌려주기
        Intent intent = getIntent();
        HashMap hashMap = (HashMap) intent.getSerializableExtra("hashmap");

        num = (String) hashMap.get("num");
        id = (String) hashMap.get("id");
        Log.e(TAG,"(jobDetailActivity)id"+id);
        subject = (String) hashMap.get("subject");
        category = (String) hashMap.get("category");
        term = (String) hashMap.get("term");
        cost = (String) hashMap.get("cost");
        content = (String) hashMap.get("content");
        bidderNum = (String) hashMap.get("bidderNum");

        tvSubject.setText(subject);
        tvCategory.setText(category);
        tvTerm.setText(term);

        decimalFormat = new DecimalFormat("###,###");
        tvCost.setText(decimalFormat.format(Integer.parseInt(cost)));
        tvContent.setText(content);
        tvNumBidder.setText(bidderNum);
        image_job_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(JobDetailActivity.this,ClientActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });
        // recyclerview관련
//        freelancerRecyclerView = (RecyclerView) findViewById(R.id.recycle_home_brand1);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        freelancerRecyclerView.setLayoutManager(linearLayoutManager);
//        freelancerArrayList = new ArrayList<>();
//        freelancerRecyclerView.setHasFixedSize(true);
//        freelancerAdapter = new FreelancerAdapter(freelancerArrayList,JobDetailActivity.this);
//        freelancerRecyclerView.setAdapter(freelancerAdapter);
//        freelancerAdapter.setFreelancerClickListener(JobDetailActivity.this);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(freelancerRecyclerView.getContext(),
//                linearLayoutManager.getOrientation());
//        freelancerRecyclerView.addItemDecoration(dividerItemDecoration);
//        Log.e("tag","freelancerlist size"+freelancerArrayList.size());
//        GetAllCompanyData getAllCompanyData = new GetAllCompanyData();
//        getAllCompanyData.execute("http://" + IP_ADDRESS + "/user/selectFreelancer.php","");

        //TODO 투찰자 수.. 인텐트로 받아오는 과정

        if(CommonValue.getId().equals(id)){
            btnBidding.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        }else{
            btnEdit.setVisibility(View.GONE);
            btnBidding.setVisibility(View.VISIBLE);
        }

        // 수정버튼 누르면 주어진 값들 가지고 수정 액티비티로 넘어갔다가 수정 끝나면 돌아오게 하기
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEditClicked(num, id, subject, category, term, cost, content);
            }
        });

        btnBidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 투찰을 실행하는 과정으로 넘어가자.
                btnBiddingClicked();
            }
        });

        tvShowBidder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 투찰자 명단을 보여주는 액티비티로 넘어가자
                Intent intent1 = new Intent(getApplicationContext(), WaitingForClientDeveloperListActivity.class);
                intent1.putExtra("jobId", num);
                intent1.putExtra("num", num);
                startActivity(intent1);

            }
        });
    }

    private void btnBiddingClicked(){
        Log.i(TAG, "btnBiddingClicked");
        // 투찰을 위해 가격을 제시하는 다이얼로그를 띄우고 '확인'을 누르면 투찰에 대한 정보를 서버에 저장하도록 한다.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText suggestPrice = new EditText(this);

        suggestPrice.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setTitle("투찰을 위해 가격을 제시해주세요.");
        builder.setView(suggestPrice);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG, "positiveBtn");

                // 확인을 누르면 서버에 가격과 투찰자, 대상작업 등을 보낸다.
                String price = suggestPrice.getText().toString();

                // 서버에 가격 보내는 함수!
                uploadBidding(price);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG, "negativeBtn");

            }
        });

        builder.create().show();

    }

    private void uploadBidding(String price){
        Log.e(TAG, "uploadBidding: "+price);
        // 투찰에 대한 정보를 서버에 저장한다.
        Call<ResponseBody> call = retrofitService.uploadBidding(num, CommonValue.getId(), id, price);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: "+response);
                if (response.isSuccessful()){
                    try {
                        String result = response.body().string();

                        if(result.equals("ok")){
                            Toast.makeText(getApplicationContext(), "투찰에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "uploading bid success");

                            int numBidder = Integer.parseInt(tvNumBidder.getText().toString());
                            numBidder++;
                            tvNumBidder.setText(String.valueOf(numBidder));

                        }else{
                            Toast.makeText(getApplicationContext(), "투찰 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "uploading fail"+result);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "응답을 못 받았습니다.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "unSuccessful");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t);

            }
        });

    }

    private void btnEditClicked(String num, String id, String subject, String category, String term, String cost, String content){
        Log.i(TAG, "btnEditClicked");
        // 수정하기 위해 값들을 인텐트로 넘기고, activitiforresult 로 보내고 다시 올 예정.
        Intent intent = new Intent(getApplicationContext(), JobEditActivity.class);
        intent.putExtra("num", num);
        intent.putExtra("id", id);
        intent.putExtra("subject", subject);
        intent.putExtra("category", category);
        intent.putExtra("term", term);
        intent.putExtra("cost", cost);
        intent.putExtra("content", content);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                // 수정 액티비티에서 다 수정되고 다시 돌아왔을 때, 수정된 값을 인텐트로 받고
                // 이 액티비티 값을 다시 바꿔준다. 서버에는 수정 액티비티에서 통신하기 때문에 따로 통신할 필요 없음.

                subject = data.getStringExtra("subject");
                category = data.getStringExtra("category");
                term = data.getStringExtra("term");
                cost = data.getStringExtra("cost");
                content = data.getStringExtra("content");

                tvSubject.setText(subject);
                tvCategory.setText(category);
                tvTerm.setText(term);

                tvCost.setText(decimalFormat.format(Integer.parseInt(cost)));
                tvContent.setText(content);
            }
        }

    }

//    @Override
//    public void onItemClicked(int position) {
//        Toast.makeText(getApplicationContext(), position + " 선택됨", Toast.LENGTH_SHORT).show();
//        Intent intentItemList = new Intent(JobDetailActivity.this, FreelancerDetailActivity.class);
//        intentItemList.putExtra("freelancerPosition",position);
//        intentItemList.putExtra("freelancerTitle",freelancerArrayList.get(position).getTitle());
//        intentItemList.putExtra("freelancerName",freelancerArrayList.get(position).getName());
//        intentItemList.putExtra("freelancerImage",freelancerArrayList.get(position).getImage());
//        intentItemList.putExtra("freelancerRate",freelancerArrayList.get(position).getRate());
//        intentItemList.putExtra("freelancerHiredNumber",freelancerArrayList.get(position).getHiredNumber());
//        intentItemList.putExtra("freelancerLocation",freelancerArrayList.get(position).getLocation());
//        intentItemList.putExtra("freelancerSkill",freelancerArrayList.get(position).getSkill());
//        intentItemList.putExtra("freelancerIntroduce",freelancerArrayList.get(position).getIntroduce());
//        intentItemList.putExtra("freelancerReview",freelancerArrayList.get(position).getReview());
//        Log.e(TAG,"freelancerImage"+freelancerArrayList.get(position).getImage());
//        startActivity(intentItemList);
//    }
//
//    private class GetAllCompanyData extends AsyncTask<String, Void, String> {
//
//        ProgressDialog progressDialog;
//        String errorString = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = ProgressDialog.show(JobDetailActivity.this,
//                    "Please Wait", null, true, true);
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            progressDialog.dismiss();
////            mTextViewResult.setText(result);
//            Log.d(TAG, "response - " + result);
//            Log.e(TAG, "response - " + result);
//            if (result == null){
//
////                mTextViewResult.setText(errorString);
//            }
//            else {
//
//                mJsonString = result;
//                showResult2();
//            }
//        }
//
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String serverURL = params[0];
//            String postParameters = params[1];
//
//
//            try {
//
//                URL url = new URL(serverURL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//
//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.connect();
//
//
//
//
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.write(postParameters.getBytes("UTF-8"));
//                outputStream.flush();
//                outputStream.close();
//
//
//                int responseStatusCode = httpURLConnection.getResponseCode();
//                Log.d(TAG, "response code - " + responseStatusCode);
//
//                InputStream inputStream;
//                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
//                    inputStream = httpURLConnection.getInputStream();
//                }
//                else{
//                    inputStream = httpURLConnection.getErrorStream();
//                }
//
////                bmImg = BitmapFactory.decodeStream(inputStream);
//
//
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder sb = new StringBuilder();
//                String line;
//
//                while((line = bufferedReader.readLine()) != null){
//                    sb.append(line);
//                }
//
//                bufferedReader.close();
//
//                return sb.toString().trim();
//
//
//            } catch (Exception e) {
//
//                Log.d(TAG, "GetData : Error ", e);
//                errorString = e.toString();
//
//                return null;
//            }
//
//        }
//    }
//
//    private void showResult2() {
//        Log.e(TAG, "showResult2들어옴  - " + mJsonString);
//        String TAG_TITLE = "title";
//        String TAG_NAME = "name";
//        String TAG_IMAGE ="image";
//        String TAG_RATE = "rate";
//        String TAG_HIREDNUMBER = "hiredNumber";
//        String TAG_LOCATION = "location";
//        String TAG_SKILL = "skill";
//        String TAG_INTRODUCE = "introduce";
//        String TAG_REVIEW = "review";
//
//        try {
//            JSONArray jsonArray = new JSONArray(mJsonString);
//
//            for (int i = 0; i < jsonArray.length() ; i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                Log.e("tag","jsonArray.length()"+jsonArray.length());
//                Log.e(TAG, "in try"+jsonArray.toString()+jsonObject.toString());
//                String title = jsonObject.getString("title");
//                String name = jsonObject.getString("name");
//                String image = jsonObject.getString("image");
//                String rate = jsonObject.getString("rate");
//                String hiredNumber = jsonObject.getString("hiredNumber");
//                String location = jsonObject.getString("location");
//                String skill = jsonObject.getString("skill");
//                String introduce = jsonObject.getString("introduce");
//                String review = jsonObject.getString("review");
//                FreelancerList freelancerList = new FreelancerList();
//                freelancerList.setTitle(title);
//                freelancerList.setName(name);
//                freelancerList.setImage(image);
//                freelancerList.setRate(rate);
//                freelancerList.setHiredNumber(hiredNumber);
//                freelancerList.setLocation(location);
//                freelancerList.setSkill(skill);
//                freelancerList.setIntroduce(introduce);
//                freelancerList.setReview(review);
//                Log.e("tag","freelancerlist size_json_before"+freelancerArrayList.size());
//                freelancerArrayList.add(freelancerList);
//                Log.e("tag","freelancerlist size_json_after"+freelancerArrayList.size());
//                freelancerAdapter.notifyItemInserted(0);
//            }
//
//
//
//
//        } catch (JSONException e) {
//
//            Log.d(TAG, "showResult : ", e);
//        }
//    }



}

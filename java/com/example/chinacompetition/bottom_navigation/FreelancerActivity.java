package com.example.chinacompetition.bottom_navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.R;
import com.example.chinacompetition.freelancer.FreelancerAdapter;
import com.example.chinacompetition.freelancer.FreelancerDetailActivity;
import com.example.chinacompetition.freelancer.FreelancerList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FreelancerActivity extends AppCompatActivity implements FreelancerAdapter.FreelancerClickListener {

    /******** 프리랜서화면 ***********/
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // 클라이언트화면으로이동
                case R.id.navigation_client:
                    Intent intentRecommend = new Intent(FreelancerActivity.this, ClientActivity.class);
                    startActivity(intentRecommend);
                    break;
                // 가상피팅화면으로이동
                case R.id.navigation_message:
                    Intent intentFitting = new Intent(FreelancerActivity.this, MessageActivity.class);
                    startActivity(intentFitting);
                    break;
                // 프리랜서화면으로 이동
                case R.id.navigation_freelancer:
                    return true;
                // 내프로필화면으로이동
                case R.id.navigation_profile:
                    Intent intentMyProfile = new Intent(FreelancerActivity.this, ProfileActivity.class);
                    startActivity(intentMyProfile);
                    break;
                // 장바구니화면으로이동
                case R.id.navigation_category:
                    Intent intentCart = new Intent(FreelancerActivity.this, CategoryActivity.class);
                    startActivity(intentCart);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Log.e("TAG","FreelancerActivity(프리랜서화면 들어옴) onCreate");
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // 이화면에 들어왔을때 초기아이템설정
        navView.setSelectedItemId(R.id.navigation_freelancer);



        freelancerRecyclerView = (RecyclerView) findViewById(R.id.recycle_home_brand1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        freelancerRecyclerView.setLayoutManager(linearLayoutManager);
        freelancerArrayList = new ArrayList<>();
        freelancerRecyclerView.setHasFixedSize(true);
        freelancerAdapter = new FreelancerAdapter(freelancerArrayList,FreelancerActivity.this);
        freelancerRecyclerView.setAdapter(freelancerAdapter);
        freelancerAdapter.setFreelancerClickListener(FreelancerActivity.this);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(freelancerRecyclerView.getContext(),
//                linearLayoutManager.getOrientation());
//        freelancerRecyclerView.addItemDecoration(dividerItemDecoration);
        Log.e("tag","freelancerlist size"+freelancerArrayList.size());
        GetAllCompanyData getAllCompanyData = new GetAllCompanyData();
        getAllCompanyData.execute("http://" + IP_ADDRESS + "/user/selectFreelancer.php","");
    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(getApplicationContext(), position + " 선택됨", Toast.LENGTH_SHORT).show();
        Intent intentItemList = new Intent(FreelancerActivity.this, FreelancerDetailActivity.class);
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
        Log.e(TAG,"freelancerImage"+freelancerArrayList.get(position).getImage());
        startActivity(intentItemList);
    }

    private class GetAllCompanyData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(FreelancerActivity.this,
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

            String serverURL = params[0];
            String postParameters = params[1];


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
                String rate = jsonObject.getString("rate");
                String hiredNumber = jsonObject.getString("hiredNumber");
                String location = jsonObject.getString("location");
                String skill = jsonObject.getString("skill");
                String introduce = jsonObject.getString("introduce");
                String review = jsonObject.getString("review");
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
                Log.e("tag","freelancerlist size_json_before"+freelancerArrayList.size());
                freelancerArrayList.add(freelancerList);
                Log.e("tag","freelancerlist size_json_after"+freelancerArrayList.size());
                freelancerAdapter.notifyItemInserted(0);
            }
          

            
           
        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }


    private void showResult(){

        String TAG_JSON="result";
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
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                Log.e("tag","jsonArray.length()"+jsonArray.length());
                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString(TAG_TITLE);
                String name = item.getString(TAG_NAME);
                String image = item.getString(TAG_IMAGE);
                String rate = item.getString(TAG_RATE);
                String hiredNumber = item.getString(TAG_HIREDNUMBER);
          
//                Uri uriImage = Uri.parse(image);
                Log.e("tag","FreelancerActivity image"+image);
                Log.e("tag","FreelancerActivity title"+title);
                Log.e("tag","FreelancerActivity rate"+rate);
                Log.e("tag","FreelancerActivity name"+name);
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
            Log.e(TAG, "showResult : ", e);
        }

    }
}

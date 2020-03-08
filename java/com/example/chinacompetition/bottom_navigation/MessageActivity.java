package com.example.chinacompetition.bottom_navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.R;
import com.example.chinacompetition.chat.ChatActivity;
import com.example.chinacompetition.chat.MessageAdapter;
import com.example.chinacompetition.chat.MessageList;
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

public class MessageActivity extends AppCompatActivity implements MessageAdapter.MessageClickListener {

    /******** Chatting list Scene***********/

    // Freelancer Recylcerview
    private ArrayList<MessageList> messageArrayList; // 모임정보 값
    private MessageAdapter messageAdapter;
    private RecyclerView messageRecyclerView;

    private static String IP_ADDRESS = "34.64.144.139";
    private static String TAG = "Chatting list Scene";
    // 프리랜서목록불러오는 변수들
    String mJsonString;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // move client activity
                case R.id.navigation_client:
                    Intent intentRecommend = new Intent(MessageActivity.this, ClientActivity.class);
                    startActivity(intentRecommend);
                    break;
                // move message activity
                case R.id.navigation_message:
                    return true;
                // move freelancer activity
                case R.id.navigation_freelancer:
                    Intent intentHome = new Intent(MessageActivity.this, FreelancerActivity.class);
                    startActivity(intentHome);
                    break;
                // move freelancer profile
                case R.id.navigation_profile:
                    Intent intentMyProfile = new Intent(MessageActivity.this, ProfileActivity.class);
                    startActivity(intentMyProfile);
                    break;
                // 장바구니화면으로이동
                case R.id.navigation_category:
                    Intent intentCart = new Intent(MessageActivity.this, CategoryActivity.class);
                    startActivity(intentCart);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // 이화면에 들어왔을때 초기아이템설정
        navView.setSelectedItemId(R.id.navigation_message);
        Log.e("TAG", "MessageActivity onCreate");


        messageRecyclerView = (RecyclerView) findViewById(R.id.recycle_message);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageArrayList = new ArrayList<>();
        messageRecyclerView.setHasFixedSize(true);
        messageAdapter = new MessageAdapter(messageArrayList, MessageActivity.this);
        messageRecyclerView.setAdapter(messageAdapter);
        messageAdapter.setMessageClickListener(MessageActivity.this);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(messageRecyclerView.getContext(),
//                linearLayoutManager.getOrientation());
//        messageRecyclerView.addItemDecoration(dividerItemDecoration);
        Log.e("tag", "messagelist size" + messageArrayList.size());
        String id = CommonValue.getId();
        Log.e("tag", "id" + id);
        GetAllChattingList getAllChattingList = new GetAllChattingList();
        getAllChattingList.execute("http://" + IP_ADDRESS + "/chat/selectMemberList.php", id);

        //1번방 이
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

                intent.putExtra("room", "1"); /*송신*/


                startActivity(intent);


            }
        });


        //1번방 이
        Button button2 = findViewById(R.id.button3);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

                intent.putExtra("room", "2"); /*송신*/


                startActivity(intent);


            }
        });

        
        
       
       
    }

    @Override
    public void onItemClicked(int position){
        Toast.makeText(getApplicationContext(), position + " 선택됨", Toast.LENGTH_SHORT).show();
        Intent intentItemList = new Intent(MessageActivity.this, ChatActivity.class);
        intentItemList.putExtra("messagePosition", position);
        intentItemList.putExtra("messageId", messageArrayList.get(position).getId());
        intentItemList.putExtra("messageName", messageArrayList.get(position).getName());
        intentItemList.putExtra("loginedId", CommonValue.getId());
        Log.e(TAG, "messageId" + messageArrayList.get(position).getId());
        startActivity(intentItemList);
    }

    private class GetAllChattingList extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MessageActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
//            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);
            Log.e(TAG, "response - " + result);
            if (result == null) {

//                mTextViewResult.setText(errorString);
            } else {

                mJsonString = result;
                showResult2();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String id = params[1];
            String postParameters = "id="+id;


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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

//                bmImg = BitmapFactory.decodeStream(inputStream);


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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
        try {
            JSONArray jsonArray = new JSONArray(mJsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.e("tag", "jsonArray.length()" + jsonArray.length());
                Log.e(TAG, "in try" + jsonArray.toString() + jsonObject.toString());
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");

                MessageList messageList = new MessageList();
                messageList.setId(id);
                messageList.setName(name);

                Log.e("tag", "messagelist size_json_before" + messageArrayList.size());
                messageArrayList.add(messageList);
                Log.e("tag", "messagelist size_json_after" + messageArrayList.size());
                messageAdapter.notifyItemInserted(0);
            }


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }

}

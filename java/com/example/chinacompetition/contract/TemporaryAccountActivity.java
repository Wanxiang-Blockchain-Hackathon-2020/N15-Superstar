package com.example.chinacompetition.contract;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.MyToken;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RetrofitService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TemporaryAccountActivity extends AppCompatActivity {

    String blockchainURL = MyToken.getBcURL();
    String TAG = "TemporaryAccountActivity";
    ImageView image_temporaray_account_make_account;
    ImageView image_temporaray_account_maked_account;
    LinearLayout linear_temporaray_account_make_account;
    LinearLayout linear_temporaray_account_maked_account;
    String employee;
    EditText et_main_cash2;
    String clientId = CommonValue.getId();
//    public void bt_queryById(View view){
//        queryById("a");
//    }
    String cash;
    Button btn_money_sendMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_account);
        image_temporaray_account_make_account = (ImageView) findViewById(R.id.image_temporaray_account_make_account);
        image_temporaray_account_maked_account = (ImageView) findViewById(R.id.image_temporaray_account_maked_account);
        linear_temporaray_account_make_account = (LinearLayout) findViewById(R.id.linear_temporaray_account_make_account);
        linear_temporaray_account_maked_account = (LinearLayout) findViewById(R.id.linear_temporaray_account_maked_account);
        et_main_cash2 = (EditText) findViewById(R.id.et_main_cash2);
        btn_money_sendMoney = (Button) findViewById(R.id.btn_money_sendMoney);
        Intent intent = getIntent();
        employee = intent.getStringExtra("messageId");

//        queryById(clientId);
        String temp = et_main_cash2.getText().toString();
        if(temp.equals(employee)){
            // have account
            image_temporaray_account_maked_account.setVisibility(View.GONE);
            linear_temporaray_account_maked_account.setVisibility(View.GONE);

            image_temporaray_account_make_account.setVisibility(View.VISIBLE);
            linear_temporaray_account_make_account.setVisibility(View.VISIBLE);
        }else{
            // doesn't have account
            image_temporaray_account_make_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    makeIdAndCash(clientId+employee,"0");
                    image_temporaray_account_maked_account.setVisibility(View.VISIBLE);
                    linear_temporaray_account_make_account.setVisibility(View.VISIBLE);

                    image_temporaray_account_make_account.setVisibility(View.GONE);
                    linear_temporaray_account_make_account.setVisibility(View.GONE);

                }
            });

        }

        // moveCash
        image_temporaray_account_maked_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(TemporaryAccountActivity.this, MoveCashActivity.class);
                intent1.putExtra("messageId",employee);
                intent1.putExtra("clientId",clientId);
                startActivity(intent1);
            }
        });

        // sendMoney
        btn_money_sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(TemporaryAccountActivity.this,MoveCashActivity.class);
                intent1.putExtra("messageId",employee);
                intent1.putExtra("clientId",clientId);
                startActivity(intent1);
            }
        });


        Log.e(TAG," employee: "+employee);



    }


    //아이디 지갑 생성 (회원가입시 만들어줄것 / 에스크로 에서 임시 돈 보관 할때 "계약자ID"+"프리랜서ID" 로 만들어줄것. )
    private void makeIdAndCash(String makeId, String initCash){
        Log.d(TAG, "moveCash");
        Log.e(TAG, "makeIdAndCash_moveCash");

        // peers
        JSONArray peer_jsonArray = new JSONArray();
        peer_jsonArray.put("peer0.org1.example.com");
        peer_jsonArray.put("peer0.org2.example.com");

        //args
        JSONArray args_jsonArray = new JSONArray();
        args_jsonArray.put(makeId); // 보내는사람
        args_jsonArray.put(initCash); // 받는사람

        // 헤더에 토큰을 넣으려고 레트로핏을 네트워크 층에서 가로챈 다음에 수정해줘야함.
        // okhttp 클라이언트 빌더를 선언해주고 인터셉트 함수 안에서 필요한 헤더를 추가한다. 메소드나 바디는 기존 요청의 것을 사용한다.
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("authorization", "Bearer "+ MyToken.getmToken())
                        .header("content-type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(blockchainURL)
                .client(client)
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = retrofitService.exeChaincode("makeIdAndCash", peer_jsonArray.toString(), args_jsonArray.toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null){
                    Log.d(TAG, "onResponse: "+response);
                    Log.e(TAG, "onResponse_makeIdAndCash_moveCash: "+response);
                }else {
                    Log.d(TAG, "response null");
                    Log.e(TAG, "response_makeIdAndCash_moveCash null");
                }
                try {
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    if(response.isSuccessful()){
                        Log.e(TAG, "result is successful_makeIdAndCash_moveCash: "+result);
                        Log.d(TAG, "result is successful: "+result);
                        Toast.makeText(getApplicationContext(), "임시계좌발급완료.", Toast.LENGTH_SHORT).show();

                    }else{
                        Log.d(TAG, "unsuccessful_makeIdAndCash_moveCash: "+result);
                        Log.e(TAG, "unsuccessful_makeIdAndCash_moveCash: "+result);
                        Toast.makeText(getApplicationContext(), "서버와 통신이 좋지 않아요.", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
                Log.e(TAG, "onFailure_makeIdAndCash_moveCash: "+t);
            }
        });
    }


    //queryById
    private void queryById(String id){
        Log.d(TAG, "queryById");
        String mReturn = null;

        // peers
        JSONArray peer_jsonArray = new JSONArray();
        peer_jsonArray.put("peer0.org1.example.com");
        peer_jsonArray.put("peer0.org2.example.com");

        //args
        JSONArray args_jsonArray = new JSONArray();
        args_jsonArray.put(id); // 보내는사람

        // 헤더에 토큰을 넣으려고 레트로핏을 네트워크 층에서 가로챈 다음에 수정해줘야함.
        // okhttp 클라이언트 빌더를 선언해주고 인터셉트 함수 안에서 필요한 헤더를 추가한다. 메소드나 바디는 기존 요청의 것을 사용한다.
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("authorization", "Bearer "+ MyToken.getmToken())
                        .header("content-type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(blockchainURL)
                .client(client)
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = retrofitService.exeChaincode("queryById", peer_jsonArray.toString(), args_jsonArray.toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null){
                    Log.d(TAG, "onResponse: "+response);
                    Log.e(TAG, "onResponse: "+response);
                }else {
                    Log.d(TAG, "response null");
                    Log.e(TAG, "response null");
                }
                try {
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    if(response.isSuccessful()){
                        Log.d(TAG, "result is successful: "+result);
                        Log.e(TAG, "result is successful: "+result);
                        Toast.makeText(getApplicationContext(), "조회완료.", Toast.LENGTH_SHORT).show();
                        //파싱
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            JSONObject record = jsonArray.getJSONObject(0).getJSONObject("Record");
                            cash = record.getString("cash");
                            et_main_cash2.setText(cash);
                        } catch (Exception e) {
                            Log.e("errJson",e.getMessage());
                        }
                    }else{
                        Log.d(TAG, "unsuccessful: "+result);
                        Log.e(TAG, "unsuccessful: "+result);
                        Toast.makeText(getApplicationContext(), "서버와 통신이 좋지 않아요.", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
                Log.e(TAG, "onFailure: "+t);
            }
        });

    }//
}

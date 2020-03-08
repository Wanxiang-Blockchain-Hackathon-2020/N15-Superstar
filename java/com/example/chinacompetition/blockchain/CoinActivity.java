package com.example.chinacompetition.blockchain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class CoinActivity extends AppCompatActivity {

    String blockchainURL = MyToken.getBcURL();
    String TAG = "CoinActivity";

    String ID;
    TextView text_coin_mymoney;
    TextView text_coin_mymoney2;
    Button btn_coin_withdrawal;
    Button btn_coin_deposit;
    public void bt_Deposit(View view) {
        Deposit(ID,"10");
    }

//    public void bt_Withdrawal(View view) {
//        Withdrawal(ID,"10");
//    }

    public void bt_moveCash(View view){
        moveCash("b",ID,"10");
    }

    public void bt_queryById(View view){
        queryById(ID);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);
        ID = CommonValue.getId();
        Log.e(TAG,"coin_ID"+ID);
        text_coin_mymoney = (TextView) findViewById(R.id.text_coin_mymoney);
        text_coin_mymoney2 = (TextView) findViewById(R.id.text_coin_mymoney2);
        btn_coin_withdrawal = (Button) findViewById(R.id.btn_coin_withdrawal);
        btn_coin_deposit = (Button) findViewById(R.id.btn_coin_deposit);
        queryById(ID);

        // go to withdrawal
        btn_coin_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoinActivity.this, MoneyWithDrawalActivity.class);
                startActivity(intent);
            }
        });

        // go to deposit
        btn_coin_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoinActivity.this,CoinDepositActivity.class);
                startActivity(intent);
            }
        });

    }

    // 유저간 돈 송금
    private void moveCash(String fromId, String toId, String cash){
        Log.d(TAG, "moveCash");

        // peers
        JSONArray peer_jsonArray = new JSONArray();
        peer_jsonArray.put("peer0.org1.example.com");
        peer_jsonArray.put("peer0.org2.example.com");

        //args
        JSONArray args_jsonArray = new JSONArray();
        args_jsonArray.put(fromId); // 보내는사람
        args_jsonArray.put(toId); // 받는사람
        args_jsonArray.put(cash); // 송금액
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
        Call<ResponseBody> call = retrofitService.exeChaincode("moveCash", peer_jsonArray.toString(), args_jsonArray.toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null){
                    Log.d(TAG, "onResponse: "+response);
                }else {
                    Log.d(TAG, "response null");
                }
                try {
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    if(response.isSuccessful()){
                        Log.d(TAG, "result is successful: "+result);
                        Toast.makeText(getApplicationContext(), "송금완료.", Toast.LENGTH_SHORT).show();

                    }else{
                        Log.d(TAG, "unsuccessful: "+result);
                        Toast.makeText(getApplicationContext(), "서버와 통신이 좋지 않아요.", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }//

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
                }else {
                    Log.d(TAG, "response null");
                }
                try {
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    if(response.isSuccessful()){
                        Log.d(TAG, "result is successful: "+result);
                        Toast.makeText(getApplicationContext(), "조회완료.", Toast.LENGTH_SHORT).show();
                        //파싱
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            JSONObject record = jsonArray.getJSONObject(0).getJSONObject("Record");
                            String cash = record.getString("cash");
                            text_coin_mymoney.setText(cash);
                            text_coin_mymoney2.setText(cash);
                        } catch (Exception e) {
                            Log.e("errJson",e.getMessage());
                        }
                    }else{
                        Log.d(TAG, "unsuccessful: "+result);
                        Toast.makeText(getApplicationContext(), "서버와 통신이 좋지 않아요.", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });

    }//

    //make Deposit
    private void Deposit(String id, String cash){
        Log.d(TAG, "makeContract");

        // peers
        JSONArray peer_jsonArray = new JSONArray();
        peer_jsonArray.put("peer0.org1.example.com");
        peer_jsonArray.put("peer0.org2.example.com");

        //args
        JSONArray args_jsonArray = new JSONArray();
        args_jsonArray.put(id);
        args_jsonArray.put(cash);

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
        Call<ResponseBody> call = retrofitService.exeChaincode("Deposit", peer_jsonArray.toString(), args_jsonArray.toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null){
                    Log.d(TAG, "onResponse: "+response);
                }else {
                    Log.d(TAG, "response null");
                }
                try {
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    if(response.isSuccessful()){
                        Log.d(TAG, "result is successful: "+result);
                        Toast.makeText(getApplicationContext(), "송금완료.", Toast.LENGTH_SHORT).show();

                    }else{
                        Log.d(TAG, "unsuccessful: "+result);
                        Toast.makeText(getApplicationContext(), "서버와 통신이 좋지 않아요.", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }//

    //make Withdrawal
    private void Withdrawal(String id, String cash){
        Log.d(TAG, "Withdrawal");

        // peers
        JSONArray peer_jsonArray = new JSONArray();
        peer_jsonArray.put("peer0.org1.example.com");
        peer_jsonArray.put("peer0.org2.example.com");

        //args
        JSONArray args_jsonArray = new JSONArray();
        args_jsonArray.put(id);
        args_jsonArray.put(cash);

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
        Call<ResponseBody> call = retrofitService.exeChaincode("Withdrawal", peer_jsonArray.toString(), args_jsonArray.toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null){
                    Log.d(TAG, "onResponse: "+response);
                }else {
                    Log.d(TAG, "response null");
                }
                try {
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    if(response.isSuccessful()){
                        Log.d(TAG, "result is successful: "+result);
                        Toast.makeText(getApplicationContext(), "송금완료.", Toast.LENGTH_SHORT).show();

                    }else{
                        Log.d(TAG, "unsuccessful: "+result);
                        Toast.makeText(getApplicationContext(), "서버와 통신이 좋지 않아요.", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }//
}

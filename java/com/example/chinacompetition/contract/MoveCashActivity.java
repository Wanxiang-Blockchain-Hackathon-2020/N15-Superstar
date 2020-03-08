package com.example.chinacompetition.contract;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.MyToken;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RetrofitService;
import com.example.chinacompetition.blockchain.MoneyActivity;
import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MoveCashActivity extends AppCompatActivity {

    String blockchainURL = MyToken.getBcURL();
    String TAG = "MoveCashActivity";
    String ID ;
    EditText edit_move_cash_input ;
    ImageView image_move_cash_back ;
    public String cash;
    String employee,employer;
    SharedPreferences shared_temporay_save ; 
    public void bt_Deposit(View view) {
        cash = edit_move_cash_input.getText().toString();
        Log.e(TAG,"employer+employee1"+employer+employee);
        Log.e(TAG,"cash1"+cash);
        Deposit(employer+employee,cash);
        shared_temporay_save = getSharedPreferences("temporay_save",Activity.MODE_PRIVATE);
        SharedPreferences.Editor temporaySave = shared_temporay_save.edit();
        temporaySave.putString("id",employer+employee);
        temporaySave.putString("cash",cash);
        temporaySave.commit();
        Log.e(TAG,"employer+employee2"+employer+employee);
        Log.e(TAG,"cash2"+cash);
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_cash);
        ID = CommonValue.getId();
        edit_move_cash_input = (EditText) findViewById(R.id.edit_move_cash_input);
        image_move_cash_back = (ImageView) findViewById(R.id.image_move_cash_back);
        Log.e(TAG,"MoneyCharge"+ID);

        Intent intent = getIntent();
        employee = intent.getStringExtra("messageId");
        employer = intent.getStringExtra("clientId");



        image_move_cash_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoveCashActivity.this, MoneyActivity.class);
                startActivity(intent);
            }
        });
    }

    //make Deposit
    private void Deposit(String id, String cash){
        Log.d(TAG, "Deposit_makeContract");
        Log.e(TAG, "Deposit_makeContract");
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
                    Log.d(TAG, "Deposit_onResponse: "+response);
                    Log.e(TAG, "Deposit_onResponse: "+response);
                }else {
                    Log.d(TAG, "Deposit_response null");
                    Log.e(TAG, "Deposit_response null");
                }
                try {
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    if(response.isSuccessful()){
                        Log.d(TAG, "Deposit_result is successful: "+result);
                        Log.e(TAG, "Deposit_result is successful: "+result);
                        Toast.makeText(getApplicationContext(), "송금완료.", Toast.LENGTH_SHORT).show();

                    }else{
                        Log.d(TAG, "Deposit_unsuccessful: "+result);
                        Log.e(TAG, "Deposit_unsuccessful: "+result);
                        Toast.makeText(getApplicationContext(), "서버와 통신이 좋지 않아요.", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Deposit_onFailure: "+t);
                Log.e(TAG, "Deposit_onFailure: "+t);
            }
        });
    }//






}

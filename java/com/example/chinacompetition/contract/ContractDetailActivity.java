package com.example.chinacompetition.contract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RetrofitService;
import com.example.chinacompetition.bottom_navigation.MessageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContractDetailActivity extends AppCompatActivity {

    Button getDocu, openDocu;
    TextView textView;

    private String TAG = "getDocument";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CommonValue.getServerURL())
            .build();

    RetrofitService retrofitService = retrofit.create(RetrofitService.class);

    String employer,employee;
    // back
    ImageView image_contract_detail_back ;

    String num, date, postNum, employerAgree, employeeAgree, docLocation;
    Button btn_contract_detail_agree;
    // 생체 인식을 사용하기 위한 핸들러, 실행
    private Handler handler = new Handler();
    private Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);

        // back
        image_contract_detail_back = (ImageView) findViewById(R.id.image_contract_detail_back);
        image_contract_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContractDetailActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        });
        btn_contract_detail_agree = (Button) findViewById(R.id.btn_contract_detail_agree);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getDocu = findViewById(R.id.getDocument);
        textView = findViewById(R.id.textView);
        openDocu = findViewById(R.id.openDocu);


        Intent intent = getIntent();
        employee = intent.getStringExtra("messageId");
        employer =intent.getStringExtra("loginedId");


        Log.e(TAG," employer: "+employer);
        Log.e(TAG," employee: "+employee);


        btn_contract_detail_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "btn_contract_detail_agree clicked");
                showBiometricPrompt();
            }
        });
        getDocu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadFromUrl(docLocation);
            }
        });

        openDocu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileUrl = getExternalFilesDir(null) + File.separator + "document.docx";
                openDocument(fileUrl);
            }
        });

        selectContract();

    }

    private void selectContract(){
        Log.e(TAG, "selectContract:"+employer+"/"+employee);
        Call<ResponseBody> call = retrofitService.selectContract(employer, employee);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: "+response);

                try {
                    String result = response.body().string();
                    Log.e(TAG, "result : "+result);


                    if(response.isSuccessful()){
                        Log.e(TAG, "isSuccessful");

                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        num = jsonObject.getString("num");
                        date = jsonObject.getString("date");
                        postNum =  jsonObject.getString("postNum");
                        employerAgree =  jsonObject.getString("employerAgree");
                        employeeAgree = jsonObject.getString("employeeAgree");
                        docLocation =  jsonObject.getString("docLocation");

                    }else{
                        Log.e(TAG, "unSuccessful");

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure : "+t);
            }
        });

    }

    // 파일 여는 메소드
    private void openDocument(String fileUrl){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(fileUrl);

        intent.setDataAndType(Uri.fromFile(file), "application/msword");
        startActivity(intent);
    }

    // 파일 다운로드하는 메소드
    private void downloadFromUrl(String fileUrl){
        Log.e(TAG, "downloadFromUrl Clicked");

        // 파일 다운로드하는 레트로핏 서비스. 입력값으로 파일 URL을 받음
        Call<ResponseBody> call = retrofitService.downloadFileFromUrl(fileUrl);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.e(TAG, "isSuccessful: "+response);

                    new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                            Log.e(TAG, "writing was successful? "+writtenToDisk);

                            return null;
                        }
                    }.execute();


                }else{
                    Log.e(TAG, "unSuccessful: "+response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure"+t);
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            Log.e(TAG, "writeResponseBodyToDisk");


            // 공 파일을 하나 만든다. 외부저장소 위치 + 파일 구분자(/) + 아무 이름!

            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "document.docx");

            Log.e(TAG, "Location : "+getExternalFilesDir(null)+File.separator);

            // 파일 전송을 위한 인 아웃풋 스트림 열기
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                // 원본 크기
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;        // 다운 얼마나 됐는지 확인하기 위한 변수

                // responseBody 에 인풋스트림 열고 위에서 만들어 놓은 공파일에 아웃풋스트림 열기
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                // 인풋스트림으로 파일을 read 변수에 다 담고
                while (true) {
                    Log.e(TAG, "In Looper");
                    int read = inputStream.read(fileReader);

                    // 더 이상 읽을 내용이 없으면 반복문 탈출
                    if (read == -1) {
                        break;
                    }

                    // 그렇지 않으면, 계속 파일을 리더에 담아서 써준다.
                    outputStream.write(fileReader, 0, read);
                    // 스트림 상태를 나타내주기 위함.
                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                //버퍼 비워주기
                outputStream.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showBiometricPrompt(){
        Log.e(TAG, "showBiometricPrompt");

        // 본인인 다이얼로그에 띄울 정보들을 선언.. 지문인식이 안 될때 2차 수단을 이용하게 함.
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("본인인증")
                .setDeviceCredentialAllowed(true)
                .build();
        // 본인인증 다이얼로그 실행! 각 결과에 따라 값을 리턴
        BiometricPrompt biometricPrompt = new BiometricPrompt(ContractDetailActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e(TAG, "onAuthenticationError:" +errString);
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.e(TAG, "onAuthenticationSucceeded: "+result);
                afterAuth("agree");
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.e(TAG, "onAuthenticationFailed");
            }
        });
        biometricPrompt.authenticate(promptInfo);
    }
    // 지문인증 한 뒤! 서버에 저장하는 부분.
    private void afterAuth(String opinion){

        Call<ResponseBody> call = retrofitService.updateContractAgree(num, CommonValue.getId(), opinion);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d(TAG, "onResponse: "+response);
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    if(response.isSuccessful()){
                        if(result.equals("ok")){
                            Toast.makeText(getApplicationContext(), "동의하셨습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "result is not ok: "+result);
                        }
                    }else{
                        Log.d(TAG, "unsuccessful: "+result);
                        Toast.makeText(getApplicationContext(), "서버와 통신이 좋지 않아요.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //  통신 실패
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });
    }

}

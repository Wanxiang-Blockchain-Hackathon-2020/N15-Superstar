package com.example.chinacompetition.contract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinacompetition.Client.ClientProjectListActivity;
import com.example.chinacompetition.Client.ClientProjectListContractActivity;
import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.NewRealPathFromUri;
import com.example.chinacompetition.PostJobDir.JobsListAdapter;
import com.example.chinacompetition.PostJobDir.JobsListData;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RetrofitService;
import com.example.chinacompetition.bottom_navigation.FreelancerActivity;
import com.example.chinacompetition.bottom_navigation.MessageActivity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContractActivity extends AppCompatActivity {

    /******** contract *****/
    private static String TAG = "Contract";
    LinearLayout linear_contract_image;
    LinearLayout linear_contract_document;
    String employee,employer,file;

    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            + "/test"; //파일 위치
    JobsListAdapter adapter = new JobsListAdapter();
    String timeStamp;
    File temp;
    TextView pathView01;
    TextView pathView03;
    TextView pathView02;
    ImageView image_contract_back ;
    Button btn_contract_bring;
    Button btn_contract_cancel;
    Button btn_contract_register;
    Button btn_contract_detail_agree;

    private String num, id, subject, category, term, cost, content, bidderNum;


    String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        Log.e("TAG","ContractActivity");
        linear_contract_image = (LinearLayout) findViewById(R.id.linear_contract_image);
        // move pdf,hwf file
        linear_contract_document = (LinearLayout) findViewById(R.id.linear_contract_document);
//        file = new File(path);

        // cancel,register
        btn_contract_cancel = (Button) findViewById(R.id.btn_contract_cancel);
        btn_contract_register = (Button) findViewById(R.id.btn_contract_register);
        btn_contract_detail_agree = (Button) findViewById(R.id.btn_contract_detail_agree);

        RecyclerView recyclerView = findViewById(R.id.recycle_contract);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



        // bring my project
        btn_contract_bring = (Button) findViewById(R.id.btn_contract_bring);
        btn_contract_bring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContractActivity.this, ClientProjectListContractActivity.class);
                startActivityForResult(intent,100);

            }
        });

        // Are you Agree?
        btn_contract_detail_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //
        Intent intent = getIntent();
        employee = intent.getStringExtra("messageId");
        employer =intent.getStringExtra("loginedId");
        file =intent.getStringExtra("messageName");

        Log.e(TAG," employer: "+employer);
        Log.e(TAG," employee: "+employee);
        Log.e(TAG," num: "+num);


        linear_contract_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // back
                image_contract_back = (ImageView) findViewById(R.id.image_contract_back);
                image_contract_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ContractActivity.this, MessageActivity.class);
                        startActivity(intent);
                    }
                });

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
//                        + "/Download/");
                intent.setType("application/*");

//                intent.setDataAndType(uri, "application/*");
//                intent.setDataAndType(uri, "application/msword");

                startActivityForResult(intent, 1);


            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1 :

                    linear_contract_document.setVisibility(View.GONE);
                    linear_contract_image.setVisibility(View.VISIBLE);


                    Log.e("TAG","ContractActivity_RESULT_OK,requestCode == 1");
                    Uri fileUri = data.getData();
                     filePath = NewRealPathFromUri.getPath(getApplicationContext(),fileUri);

                    Log.e("TAG","fileUri"+fileUri);
                    Log.e("TAG","filePath"+filePath);

                    // register
                    btn_contract_register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            uploadFileToServer(filePath);
                        }
                    });


                    break;
                case 100 :

                    adapter.items.clear();

                    Log.e("TAG","ContractActivity_RESULT_OK,requestCode == 100");
                    // 인텐트로 넘겨받은 값들을 불러와서 뷰에 뿌려주기

                    HashMap hashMap = (HashMap) data.getSerializableExtra("hashmap");

                    num=data.getStringExtra("num");
                    id=data.getStringExtra("id");
                    subject=data.getStringExtra("subject");
                    category=data.getStringExtra("category");
                    term=data.getStringExtra("term");
                    cost=data.getStringExtra("cost");
                    content=data.getStringExtra("content");
                    bidderNum=data.getStringExtra("bidderNum");

//                    num = (String) hashMap.get("num");
//                    id = (String) hashMap.get("id");
//                    Log.e(TAG,"(jobDetailActivity)id"+id);
//                    subject = (String) hashMap.get("subject");
//                    category = (String) hashMap.get("category");
//                    term = (String) hashMap.get("term");
//                    cost = (String) hashMap.get("cost");
//                    content = (String) hashMap.get("content");
//                    bidderNum = (String) hashMap.get("bidderNum");
                    Log.e("TAG","num"+num+"num"+id+"num"+subject);
                    JobsListData item = new JobsListData(num, id, subject,
                            category,  term, cost,  content,
                            bidderNum,id);

                    adapter.addItem(item);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

    }



    // 서버에 이미지보내기
    private void uploadFileToServer(String filePath){
        Log.d(TAG, "uploadFileToServer");
        Log.e(TAG, "uploadFileToServer");


        // 레트로핏을 사용한다는 말임. URL은 연결할 서버 URL이고, Gson을 이용해 Json 값을 변환한다는 뜻.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonValue.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 레트로핏 서비스를 레트로핏 객체에 넣어줌.
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = null;

        // 앨범에서 가져온 이미지의 주소로 파일을 하나 생성함.
        File file = new File(filePath);
        Log.e(TAG,"uri.getPath(): "+filePath);
        // 요청하는 내용에 멀티파트 폼 데이터라는 것을 명시하고, 파일을 같이 넣어줌.
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // 멀티파트로 된 업로드파일을 하나 만들고, 이름을 files (이건 서버에서 불러올 이름이 됨), 이미지 주소와 요청내용을 달아줌.
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("file", filePath, requestBody);
        Log.e(TAG,"upload_uploadFile: "+uploadFile);
        // 콜 객체를 만들어서 레트로핏 서비스에 만들어 놓은 함수를 넣는다.
        call = retrofitService.uploadFile(uploadFile, employer, employee, num);
//        Log.e(TAG,"retrofitService.uploadFile: "+listTitle+""+listName+""+listSkill+""+listIntroduce+""+listLocation+""+CommonValue.getId());
        Log.e(TAG,"retrofitService.uploadFile: "+uploadFile);
        Log.e(TAG,"retrofitService.uploadFile: "+employer);
        Log.e(TAG,"retrofitService.uploadFile: "+employee);
        Log.e(TAG,"retrofitService.uploadFile: "+num);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: "+response);
                Log.e(TAG, "onResponse: "+response);

                try {
                    String result = response.body().string();

                    if(response.isSuccessful()){
                        Log.d(TAG, "isSuceessful: "+result);
                        Log.e(TAG, "isSuceessful: "+result);

                        Toast.makeText(ContractActivity.this,"The file was uploaded", Toast.LENGTH_SHORT);

                    }else{
                        Log.d(TAG, "isn't Suceessful: "+result);
                        Log.e(TAG, "isn't Suceessful: "+result);
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
    }

}

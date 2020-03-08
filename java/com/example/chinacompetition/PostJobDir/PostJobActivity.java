package com.example.chinacompetition.PostJobDir;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RetrofitService;
import com.example.chinacompetition.bottom_navigation.ClientActivity;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostJobActivity extends AppCompatActivity {

    private String TAG = "PostJobActivity";

    EditText etSubject, etCategory, etCost, etContent;
    RadioGroup rgDate;
    RadioButton rbDate, rbNone;
    ImageView ivCalendar;
    Spinner spinnerCategory;
    Button btnNext;

    private String selectedSpinner;
    private int REQUEST_CODE = 100;

    private String startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postjob);

        etSubject = findViewById(R.id.etSubject);
        etCategory = findViewById(R.id.etCategory);
        etCost = findViewById(R.id.etCost);
        etContent = findViewById(R.id.etContent);

        rgDate = findViewById(R.id.rgDate);
        rbDate = findViewById(R.id.rb1);
        rbNone = findViewById(R.id.rb2);
        ivCalendar = findViewById(R.id.ivCalendar);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnNext = findViewById(R.id.btnNext);

        // 달력이나, 스피너나, 버튼 같은 것의 기본 기능을 설정하는 메소드
        initFunc();

    }

    private void initFunc(){
        Log.i(TAG, "initFunc");
        Log.e(TAG, "initFunc");
        // 달력이나, 스피너나, 버튼 같은 것의 기본 기능을 설정하는 메소드


        // 달력에서 날짜를 가져오기 위한 클릭리스너, 달력이 액티비티로 되어 있기 때문에, 인텐트 형식으로 값을 받아온다.
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // 어레이 어뎁터를 만든다. 카테고리에 들어갈 값들을 선언해 놓은 R.array.category에서 값을 가져온다.
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.jobCategory, android.R.layout.simple_spinner_item);
        // 어뎁터를 어떤 형식으로 보여줄지 선택한다. layout 옵션이 있는데 크게 다른 점은 없다.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        //스피너 클릭 리스너
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedSpinner = spinnerCategory.getSelectedItem().toString();
                Log.i(TAG, "onItemSelected : "+position+" 번째 아이템 - "+selectedSpinner);
                Log.e(TAG, "onItemSelected : "+position+" 번째 아이템 - "+selectedSpinner);

                // '기타'가 눌리면 내용을 적을 수 있는 에딭텍스트가 보였다 안보였다 함.
                if(selectedSpinner.equals("기타")){
                    etCategory.setVisibility(View.VISIBLE);
                }else{
                    etCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다음 버튼 눌렀을 때 데이터베이스에 저장되는 통신이 실행됨.
                nextBtnClicked();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult:"+data);
        Log.e(TAG, "onActivityResult:"+data);

        // 달력에서 받아온 날짜를 처리해주자.
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Log.i(TAG, "RESULT_OK");
                Log.e(TAG, "RESULT_OK");
                startDate = data.getStringExtra("startDate");
                endDate = data.getStringExtra("endDate");

                rbDate.setText(startDate+" ~ "+endDate);
                rbDate.setChecked(true);
            }
        }
    }

    private void nextBtnClicked(){
        Log.i(TAG, "next button is clicked");
        Log.e(TAG, "next button is clicked");

        // 화면에서 입력된 값들을 불러옴
        String subject = etSubject.getText().toString();

        String category = selectedSpinner;
        if(selectedSpinner.equals("기타")){
            category = etCategory.getText().toString();
        }

        String term = null;
        if(rbDate.isChecked()){
            term = startDate+" ~ "+endDate;
        }else if(rbNone.isChecked()){
            term = "논의 필요";
        }

        String cost = etCost.getText().toString();
        String content = etContent.getText().toString();

        // 미입력된 값 있는지 확인.. 현재는 테스트용으로 조건문에 안 넣음. 나중에는 넣어야함.
        if(subject.equals("") || category.equals("") || term.equals("") || cost.equals("") || content.equals("") ){
            Log.i(TAG, "빈칸이 있다");
            Log.e(TAG, "빈칸이 있다");
            Toast.makeText(getApplicationContext(), "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
        }else{

        }

        // 레트로핏 객체 생성 및 콜 보내기
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonValue.getServerURL())
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = retrofitService.postJob(CommonValue.getId(), subject, category, term, cost, content);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "onResponse: "+response);
                Log.e(TAG, "onResponse: "+response);
                if (response.isSuccessful()){
                    try {
                        String result = response.body().string();

                        if(result.equals("ok")){
                            Toast.makeText(getApplicationContext(), "등록 성공했습니다.", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "post success");
                            Log.e(TAG, "post success");
                            // Client화면으로 이동
                            Intent intent = new Intent(PostJobActivity.this, ClientActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(getApplicationContext(), "등록 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "post fail");
                            Log.e(TAG, "post fail");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "응답을 못 받았습니다.", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "unSuccessful");
                    Log.e(TAG, "unSuccessful");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: "+t);
                Log.e(TAG, "onFailure: "+t);
            }
        });
    }
}

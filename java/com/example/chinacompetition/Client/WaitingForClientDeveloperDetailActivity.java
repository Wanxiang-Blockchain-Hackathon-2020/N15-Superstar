package com.example.chinacompetition.Client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.PostJobDir.PostJobActivity;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RetrofitService;
import com.example.chinacompetition.bottom_navigation.ClientActivity;
import com.example.chinacompetition.bottom_navigation.FreelancerActivity;
import com.example.chinacompetition.freelancer.FreelancerDetailActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WaitingForClientDeveloperDetailActivity extends AppCompatActivity {

    /********* 의뢰한 프로젝트 요청한 프리랜서상세화면 ***********/

    ImageView image_freelancer_detail_back ; // 뒤로가기

    ImageView image_freelancer_detail_image;
    TextView text_freelancer_detail_name;
    TextView text_freelancer_detail_introduce;
    TextView text_freelancer_detail_skill;
    TextView text_freelancer_detail_location;
    Button btn_freelancer_detail_accept;
    Button btn_freelancer_detail_decline;
    TextView text_freelancer_detail_free_item_name; // 구입하려는 상품이름
    LinearLayout linear_freelancer_detail_button ; // 구매하기 Linear
    LinearLayout linear_freelancer_detail_goods ; // 상품정보 Linear
    TextView text_freelancer_detail_delivery ; // 상품 배송/교환관련
    TextView text_freelancer_detail_prcie ; // 가격
    ImageView image_freelancer_detail_item_cart ; // 카트이미지



    private static String IP_ADDRESS = "54.180.81.219";
    private static String TAG = "프리랜서상세화면";
    String mJsonString ;
    String mJsonString_cart ;
    private TextView mTextViewResult;

    // 유저가 선택한 아이템상세화면
    int freePosition;
    String freeItemTitle,freeItemImage;

    String category,size_m,size_l,size_xl,color,information,gender,contentexchange,itemgood ;

    SharedPreferences shared_maintainLogin ; // 로그인 정보 유지 Shard
    // 로그인정보 담은변수
    String freeTitle,freeName,freeImage,freeRate,freeHiredNumber,freeLocation,freeSkill,freeIntroduce,freeReview;

    // TemporaryCart Recylcerview
//    private ArrayList<TemporaryCartList> temporaryListArrayList; // 모임정보 값
//    private TemporaryCartAdapter temporaryCartAdapter;
//    private RecyclerView temporaryRecyclerView;

    // Choosed Project number,id
    String num,id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_client_developer_detail);
        Log.e(TAG, "프리랜서상세화면(BrandItemDetailActivity)으로 들어옴 onCreate");
        image_freelancer_detail_back = (ImageView) findViewById(R.id.image_freelancer_detail_back);
        image_freelancer_detail_image = (ImageView) findViewById(R.id.image_freelancer_detail_image);
        text_freelancer_detail_name = (TextView) findViewById(R.id.text_freelancer_detail_name);
        text_freelancer_detail_introduce = (TextView) findViewById(R.id.text_freelancer_detail_introduce);
        text_freelancer_detail_skill = (TextView) findViewById(R.id.text_freelancer_detail_skill);
        text_freelancer_detail_location = (TextView) findViewById(R.id.text_freelancer_detail_location);
        text_freelancer_detail_prcie = (TextView) findViewById(R.id.text_freelancer_detail_prcie);
        btn_freelancer_detail_accept = (Button) findViewById(R.id.btn_freelancer_detail_accept);
        btn_freelancer_detail_decline = (Button) findViewById(R.id.btn_freelancer_detail_decline);

        linear_freelancer_detail_button = (LinearLayout) findViewById(R.id.linear_freelancer_detail_button);
        mTextViewResult = (TextView) findViewById(R.id.text_freelancer_detail_result);
        text_freelancer_detail_delivery = (TextView) findViewById(R.id.text_freelancer_detail_delivery);

        final Intent intent = getIntent();
        num = intent.getStringExtra("num");
        id = intent.getStringExtra("id");
        Log.e(TAG,"(waiting_for_client )id"+id);
        // 로그인정보불러오기
        loadMaintainUser();



        freePosition = intent.getIntExtra("freelancerPosition",0);
        freeTitle = intent.getStringExtra("freelancerTitle");
        freeName = intent.getStringExtra("freelancerName");
        freeImage = intent.getStringExtra("freelancerImage");
        freeRate = intent.getStringExtra("freelancerRate");
        freeHiredNumber = intent.getStringExtra("freelancerHiredNumber");
        freeLocation = intent.getStringExtra("freelancerLocation");
        freeSkill = intent.getStringExtra("freelancerSkill");
        freeIntroduce = intent.getStringExtra("freelancerIntroduce");
        freeReview = intent.getStringExtra("freelancerReview");

        text_freelancer_detail_name.setText(freeName);
        text_freelancer_detail_introduce.setText(freeIntroduce);
        text_freelancer_detail_skill.setText(freeSkill);
        text_freelancer_detail_location.setText(freeLocation);



        Glide.with(this).load("http://54.180.81.219/home" + freeItemImage).into(image_freelancer_detail_image);




        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost_freelancer_detail);
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content_freelancer_detail_detail);
        ts1.setIndicator("프로필");
        tabHost1.addTab(ts1);

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content_freelancer_detail_delivery);
        ts2.setIndicator("진행했던프로젝트");
        tabHost1.addTab(ts2);


        // 임시추가 recyclerview 관련
//        temporaryRecyclerView = (RecyclerView) findViewById(R.id.recycle_freelancer_detail);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        temporaryRecyclerView.setLayoutManager(linearLayoutManager);
//        temporaryListArrayList = new ArrayList<>();
//        temporaryRecyclerView.setHasFixedSize(true);
//        temporaryCartAdapter = new TemporaryCartAdapter(temporaryListArrayList, BrandItemDetailActivity.this);
//        temporaryRecyclerView.setAdapter(temporaryCartAdapter);
////        temporaryCartAdapter.setHomeClickListener(HomeActivity.this);
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(temporaryRecyclerView.getContext(),
//                linearLayoutManager.getOrientation());
//        temporaryRecyclerView.addItemDecoration(dividerItemDecoration);


        btn_freelancer_detail_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert_confirm = new androidx.appcompat.app.AlertDialog.Builder(WaitingForClientDeveloperDetailActivity.this);
                alert_confirm.setMessage("수락하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                setBiddingState("accept");

                                Toast.makeText(WaitingForClientDeveloperDetailActivity.this,"채팅을 진행해주세요..", Toast.LENGTH_SHORT);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                setBiddingState("refuse");
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });



        // 뒤로가기누르면 선택한 브랜드의 의류목록화면으로 이동
        image_freelancer_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(WaitingForClientDeveloperDetailActivity.this, FreelancerActivity.class);
                startActivity(intent1);
            }
        });
        Log.e("tag", "onCreate부분");


    }

    private void setBiddingState(final String state){
        // 레트로핏 객체 생성 및 콜 보내기
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonValue.getServerURL())
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = retrofitService.postBiddingState(id, num, state);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "onResponse: "+response);
                Log.e(TAG, "onResponse: "+response);
                if (response.isSuccessful()){
                    try {
                        String result = response.body().string();
                        Log.e(TAG, "result: "+result);
                        if(result.equals("ok")){

                            if(state.equals("accept")){
                                Toast.makeText(getApplicationContext(), "수락했습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "거절했습니다.", Toast.LENGTH_SHORT).show();
                            }

                            Log.i(TAG, "post success");
                            Log.e(TAG, "post success");

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






    private void initMaintainUser(){
        // 로그인 유지 shard 저장
        shared_maintainLogin = (SharedPreferences) getSharedPreferences("maintain_login", Activity.MODE_PRIVATE);
    }

    // 로그인 정보불러오기
    private void loadMaintainUser(){
        initMaintainUser();
//        loginedClassification = shared_maintainLogin.getString("login_classification",null);
//        loginedRegisterId = shared_maintainLogin.getString("login_register_id",null);
//        loginedRegisterCompanyName = shared_maintainLogin.getString("login_register_company_name",null);
//        loginedEmail = shared_maintainLogin.getString("login_email",null);
//        loginedName = shared_maintainLogin.getString("login_name",null);
//        loginedPhone = shared_maintainLogin.getString("login_phone",null);
//        loginedPostNumber = shared_maintainLogin.getString("login_postcode_number",null);
//        loginedPostBasic = shared_maintainLogin.getString("login_postcode_basic",null);
//        loginedPostDetail = shared_maintainLogin.getString("login_postcode_detail",null);


    }
}

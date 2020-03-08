package com.example.chinacompetition.freelancer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chinacompetition.PostJobDir.JobDetailActivity;
import com.example.chinacompetition.R;
import com.example.chinacompetition.bottom_navigation.FreelancerActivity;

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

public class FreelancerDetailActivity extends AppCompatActivity {

    /********* 프리랜서상세화면 ***********/

    ImageView image_freelancer_detail_back ; // 뒤로가기

    ImageView image_freelancer_detail_image;
    TextView text_freelancer_detail_name;
    TextView text_freelancer_detail_introduce;
    TextView text_freelancer_detail_skill;
    TextView text_freelancer_detail_portfolio;
    TextView text_freelancer_detail_location;
    Button btn_freelancer_detail_hire; // 고용하기
    TextView text_freelancer_detail_free_item_name; // 구입하려는 상품이름
    LinearLayout linear_freelancer_detail_button ; // 구매하기 Linear
    LinearLayout linear_freelancer_detail_goods ; // 상품정보 Linear
    TextView text_freelancer_detail_delivery ; // 상품 배송/교환관련
    ImageView image_freelancer_detail_item_cart ; // 카트이미지



    private static String IP_ADDRESS = "54.180.81.219";
    private static String TAG = "프리랜서상세화면";
    String mJsonString ;
    String mJsonString_cart ;
    private TextView mTextViewResult;

    // 유저가 선택한 아이템상세화면
    int freePosition;

    String category,size_m,size_l,size_xl,color,information,gender,contentexchange,itemgood ;

    SharedPreferences shared_maintainLogin ; // 로그인 정보 유지 Shard
    // 로그인정보 담은변수
    String freeTitle,freeName,freeImage,freeRate,freeHiredNumber,freeLocation,freeSkill,freeIntroduce,freeReview;

    // TemporaryCart Recylcerview
//    private ArrayList<TemporaryCartList> temporaryListArrayList; // 모임정보 값
//    private TemporaryCartAdapter temporaryCartAdapter;
//    private RecyclerView temporaryRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_detail);
        Log.e(TAG, "프리랜서상세화면(BrandItemDetailActivity)으로 들어옴 onCreate");
        image_freelancer_detail_back = (ImageView) findViewById(R.id.image_freelancer_detail_back);
        image_freelancer_detail_image = (ImageView) findViewById(R.id.image_freelancer_detail_image);
        text_freelancer_detail_name = (TextView) findViewById(R.id.text_freelancer_detail_name);
        text_freelancer_detail_introduce = (TextView) findViewById(R.id.text_freelancer_detail_introduce);
        text_freelancer_detail_skill = (TextView) findViewById(R.id.text_freelancer_detail_skill);
        text_freelancer_detail_location = (TextView) findViewById(R.id.text_freelancer_detail_location);

        btn_freelancer_detail_hire = (Button) findViewById(R.id.btn_freelancer_detail_hire);
        linear_freelancer_detail_button = (LinearLayout) findViewById(R.id.linear_freelancer_detail_button);
        mTextViewResult = (TextView) findViewById(R.id.text_freelancer_detail_result);
        text_freelancer_detail_delivery = (TextView) findViewById(R.id.text_freelancer_detail_delivery);

        final Intent intent = getIntent();

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


        Log.e(TAG,"freeItemImage"+freeImage);
        Glide.with(this).load(freeImage).into(image_freelancer_detail_image);


        
        
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


        // 뒤로가기누르면 선택한 브랜드의 의류목록화면으로 이동
        image_freelancer_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(FreelancerDetailActivity.this, FreelancerActivity.class);
                startActivity(intent1);
            }
        });

        // 구입하기버튼누르면 상품선택할 수 있는 창이뜸
        btn_freelancer_detail_hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert_confirm = new androidx.appcompat.app.AlertDialog.Builder(FreelancerDetailActivity.this);
                alert_confirm.setMessage("고용 요청하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                Toast.makeText(FreelancerDetailActivity.this,"고용 요청하셨습니다.", Toast.LENGTH_SHORT);

                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();

            }
        });


  

    


        Log.e("tag", "onCreate부분");


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

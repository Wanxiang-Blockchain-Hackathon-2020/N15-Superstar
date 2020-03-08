package com.example.chinacompetition.bottom_navigation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chinacompetition.Client.ClientProjectListActivity;
import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.LoginActivity;
import com.example.chinacompetition.PostJobDir.PostJobActivity;
import com.example.chinacompetition.R;
import com.example.chinacompetition.blockchain.CoinActivity;
import com.example.chinacompetition.blockchain.MoneyActivity;
import com.example.chinacompetition.freelancer.FreelancerWritingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.chinacompetition.CommonValue.loginedCLIENTFREELANCER;

public class ProfileActivity extends AppCompatActivity {

    /******** 프로필화면 ***********/
    TextView text_profile_logout ; // 로그아웃
    TextView btn_profile_withdrawal ; // 회원탈퇴
    TextView text_profile_name ; // 이름
    TextView text_profile_dibs_list; // 찜목록
    TextView text_profile_invite_friend; // 친구초대
    TextView text_profile_review_writing; // 앱리뷰쓰기
    TextView text_profile_license; // 오픈소스 라이센스
    TextView text_profile_customer_center; // 고객센터
    TextView text_profile_client_full_view; // 클라이언트 전체보기
    LinearLayout linear_profile_dibs_list; // 찜목록
    LinearLayout linear_profile_invite_friend; // 친구초대
    LinearLayout linear_profile_review_writing; // 앱리뷰쓰기
    LinearLayout linear_profile_license; // 오픈소스 라이센스
    LinearLayout linear_profile_customer_center; // 고객센터
    LinearLayout linear_profile_service_edit; // 서비스관리/편집(마켓전용)
    LinearLayout linear_profile_register; // 서비스등록(마켓전용)
    LinearLayout linear_profile_profit; // 수익관리
    LinearLayout linear_profile_advertisement; // 광고신청

    LinearLayout linear_profile_export_selling; //판매중인 상품
    LinearLayout linear_profile_export_send; //보낸견적
    LinearLayout linear_profile_client_request; // 나의 견적요청
    LinearLayout linear_profile_client_purchasing; // 구매중인주문

    LinearLayout linear_profile_client_credit_list;  // 결제/환불내역
    LinearLayout linear_profile_client_coupon; // 쿠폰/프로모션
    LinearLayout linear_profile_client_cash; // 캐시 충전
    LinearLayout linear_profile_register_export; // 전문가등록
    LinearLayout linear_profile_coin; // coin
    LinearLayout linear_profile_wallet; // wallet

    Button btn_profile_exportToClient; // 전문가-> 손님
    Button btn_profile_clientToExport; // 손님 -> 전문가
    Button btn_profile_wallet; // wallet

    SharedPreferences shared_maintainLogin ; // 로그인 정보 유지 Shard

//    TextView text_profile_ordered_inquiry ; // 사업자용 주문내역 조회
    TextView text_profile_admin ; // admin화면으로 이동
    // 로그인정보 담은변수
    String loginedClassification,loginedRegisterId,loginedRegisterCompanyName,loginedEmail
            ,loginedName,loginedPhone,loginedPostNumber,loginedPostBasic,loginedPostDetail;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // 클라이언트화면으로이동
                case R.id.navigation_client:
                    Intent intentRecommend = new Intent(ProfileActivity.this, ClientActivity.class);
                    startActivity(intentRecommend);
                    break;
                // 가상피팅화면으로이동
                case R.id.navigation_message:
                    Intent intentFitting = new Intent(ProfileActivity.this, MessageActivity.class);
                    startActivity(intentFitting);
                    break;
                // 프리랜서화면으로 이동
                case R.id.navigation_freelancer:
                    Intent intentHome = new Intent(ProfileActivity.this, FreelancerActivity.class);
                    startActivity(intentHome);
                // 내프로필화면으로이동
                case R.id.navigation_profile:
                    return true;

                // 장바구니화면으로이동
                case R.id.navigation_category:
                    Intent intentCart = new Intent(ProfileActivity.this, CategoryActivity.class);
                    startActivity(intentCart);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("tag","프로필화면 onCreate");
        setContentView(R.layout.activity_profile);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // 이화면에 들어왔을때 초기아이템설정
        navView.setSelectedItemId(R.id.navigation_profile);
        text_profile_name = (TextView) findViewById(R.id.text_profile_name);
        text_profile_logout = (TextView) findViewById(R.id.text_profile_logout);
        btn_profile_withdrawal = (TextView) findViewById(R.id.btn_profile_withdrawal);
        text_profile_dibs_list = (TextView) findViewById(R.id.text_profile_dibs_list);
        text_profile_invite_friend = (TextView) findViewById(R.id.text_profile_invite_friend);
        text_profile_review_writing = (TextView) findViewById(R.id.text_profile_review_writing);
        text_profile_license = (TextView) findViewById(R.id.text_profile_license);
        text_profile_customer_center = (TextView) findViewById(R.id.text_profile_customer_center);
        btn_profile_withdrawal = (TextView) findViewById(R.id.btn_profile_withdrawal);
        btn_profile_withdrawal = (TextView) findViewById(R.id.btn_profile_withdrawal);
        linear_profile_dibs_list = (LinearLayout) findViewById(R.id.linear_profile_dibs_list);
        linear_profile_invite_friend = (LinearLayout) findViewById(R.id.linear_profile_invite_friend);
        linear_profile_review_writing = (LinearLayout) findViewById(R.id.linear_profile_review_writing);
        linear_profile_license = (LinearLayout) findViewById(R.id.linear_profile_license);
        linear_profile_customer_center = (LinearLayout) findViewById(R.id.linear_profile_customer_center);
        linear_profile_service_edit = (LinearLayout) findViewById(R.id.linear_profile_service_edit);
        linear_profile_register = (LinearLayout) findViewById(R.id.linear_profile_register);
        linear_profile_profit = (LinearLayout) findViewById(R.id.linear_profile_profit);
        linear_profile_advertisement = (LinearLayout) findViewById(R.id.linear_profile_advertisement);
        linear_profile_export_selling = (LinearLayout) findViewById(R.id.linear_profile_export_selling);
        linear_profile_export_send = (LinearLayout) findViewById(R.id.linear_profile_export_send);
        linear_profile_client_request = (LinearLayout) findViewById(R.id.linear_profile_client_request);
        linear_profile_client_purchasing = (LinearLayout) findViewById(R.id.linear_profile_client_purchasing);
        linear_profile_client_credit_list = (LinearLayout) findViewById(R.id.linear_profile_client_credit_list);
        linear_profile_client_coupon = (LinearLayout) findViewById(R.id.linear_profile_client_coupon);
        linear_profile_client_cash = (LinearLayout) findViewById(R.id.linear_profile_client_cash);
        linear_profile_register_export = (LinearLayout) findViewById(R.id.linear_profile_register_export);
        linear_profile_coin = (LinearLayout) findViewById(R.id.linear_profile_coin);
        linear_profile_wallet = (LinearLayout) findViewById(R.id.linear_profile_wallet);
        btn_profile_exportToClient = (Button) findViewById(R.id.btn_profile_exportToClient);
        btn_profile_clientToExport = (Button) findViewById(R.id.btn_profile_clientToExport);
        text_profile_client_full_view = (TextView) findViewById(R.id.text_profile_client_full_view);

        btn_profile_wallet = (Button) findViewById(R.id.btn_profile_wallet);
        String beforeButton = CommonValue.getLoginedCLIENTFREELANCER();
        Log.e("tag","beforeButton"+beforeButton);
        // 전문가 -> 손님
        btn_profile_exportToClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_profile_client_request.setVisibility(View.VISIBLE);
                linear_profile_client_purchasing.setVisibility(View.VISIBLE);
                linear_profile_export_selling.setVisibility(View.GONE);
                linear_profile_export_send.setVisibility(View.GONE);
                btn_profile_exportToClient.setVisibility(View.GONE);
                btn_profile_clientToExport.setVisibility(View.VISIBLE);
                CommonValue.setLoginedCLIENTFREELANCER("client");
                loginedCLIENTFREELANCER = "client";

                Log.e("tag","loginedexportToClient"+loginedCLIENTFREELANCER);
            }
        });
        // 손님 -> 전문가
        btn_profile_clientToExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_profile_client_request.setVisibility(View.GONE);
                linear_profile_client_purchasing.setVisibility(View.GONE);
                linear_profile_export_selling.setVisibility(View.VISIBLE);
                linear_profile_export_send.setVisibility(View.VISIBLE);
                btn_profile_exportToClient.setVisibility(View.VISIBLE);
                btn_profile_clientToExport.setVisibility(View.GONE);
                loginedCLIENTFREELANCER = "freelancer";
                CommonValue.setLoginedCLIENTFREELANCER("freelancer");
                Log.e("tag","loginedclientToExport"+loginedCLIENTFREELANCER);
            }
        });


        // move to coin activity
        linear_profile_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CoinActivity.class);
                startActivity(intent);
            }
        });
        // move to money activity
        linear_profile_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MoneyActivity.class);
                startActivity(intent);
            }
        });

        // go to wallet
        btn_profile_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MoneyActivity.class);
                startActivity(intent);
            }
        });

        // 클라이언트 견적요청하기
        linear_profile_client_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PostJobActivity.class);
                startActivity(intent);
            }
        });

        // 전문가등록
        linear_profile_register_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, FreelancerWritingActivity.class);
                startActivity(intent);
            }
        });
        // 찜목록
        linear_profile_dibs_list.setOnClickListener(new View.OnClickListener() {
             @Override
                public void onClick(View view) {

                }
             });
        // 친구초대
        linear_profile_invite_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 앱리뷰쓰기
        linear_profile_review_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 오픈소스 라이센스
        linear_profile_license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 고객센터
        linear_profile_customer_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 서비스관리/편집(마켓전용)
        linear_profile_service_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 서비스등록(마켓전용)
        linear_profile_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 수익관리
        linear_profile_profit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 광고신청
        linear_profile_advertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        text_profile_admin = (TextView) findViewById(R.id.text_profile_admin);
        // 로그인정보유지
        loadMaintainUser();

        // 내(클라이언트) 프로젝트목록 전체보기
        text_profile_client_full_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(ProfileActivity.this, ClientProjectListActivity.class);
                    startActivity(intent);
            }
        });


        // 접속한 유저가 회사 혹은 개인여부에 따라 관리자화면으로 이동할 수 있는게 보이거나 안보임
//        if(loginedClassification.equals("회사")){
//            text_profile_admin.setVisibility(View.VISIBLE);
//            linear_profile_admin.setVisibility(View.VISIBLE);
//        }else if(loginedClassification.equals("개인")){
//            text_profile_admin.setVisibility(View.GONE);
//            linear_profile_register_company.setVisibility(View.GONE);
//            text_profile_register_company.setVisibility(View.GONE);
//        }



        text_profile_name.setText(loginedName);

        // 회원탈퇴
        btn_profile_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag","프로필화면 > 로그인화면으로 이동");
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        // 로그아웃하기
        text_profile_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag","프로필화면 > 이메일로그인화면으로 이동");
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initMaintainUser(){
        // 로그인 유지 shard 저장
        shared_maintainLogin = getSharedPreferences("maintain_login", Activity.MODE_PRIVATE);
    }

    // 로그인 정보불러오기
    private void loadMaintainUser(){
        initMaintainUser();
        loginedClassification = shared_maintainLogin.getString("login_classification",null);
        loginedRegisterId = shared_maintainLogin.getString("login_register_id",null);
        loginedRegisterCompanyName = shared_maintainLogin.getString("login_register_company_name",null);
        loginedEmail = shared_maintainLogin.getString("login_email",null);
        loginedName = shared_maintainLogin.getString("login_name",null);
        loginedPhone = shared_maintainLogin.getString("login_phone",null);
        loginedPostNumber = shared_maintainLogin.getString("login_postcode_number",null);
        loginedPostBasic = shared_maintainLogin.getString("login_postcode_basic",null);
        loginedPostDetail = shared_maintainLogin.getString("login_postcode_detail",null);


    }
}

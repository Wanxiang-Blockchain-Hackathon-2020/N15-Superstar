package com.example.chinacompetition.bottom_navigation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chinacompetition.R;
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

public class CategoryActivity extends AppCompatActivity {

    /******** 카테고리화면 ***********/


    private CheckBox check_cart_all;

    private static String IP_ADDRESS = "54.180.81.219";
    private static String TAG = "장바구니화면";
    String mJsonString ;
    private TextView mTextViewResult;

    // recyclerview
    private RecyclerView cartRecyclerView ;
//    private CartAdapter cartAdapter;
//    private ArrayList<CartList> cartListArrayList ;
    LinearLayout linear_cart_check_count ;
    private TextView text_cart_check_count ;
    SharedPreferences shared_maintainLogin ; // 로그인 정보 유지 Shard
    // 로그인정보 담은변수
    String loginedClassification,loginedRegisterId,loginedRegisterCompanyName,loginedEmail
            ,loginedName,loginedPhone,loginedPostNumber,loginedPostBasic,loginedPostDetail;

    String totalMoney;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // 클라이언트화면으로이동
                case R.id.navigation_client:
                    Intent intentRecommend = new Intent(CategoryActivity.this, ClientActivity.class);
                    startActivity(intentRecommend);
                    break;
                // 가상피팅화면으로이동
                case R.id.navigation_message:
                    Intent intentFitting = new Intent(CategoryActivity.this, MessageActivity.class);
                    startActivity(intentFitting);
                    break;
                // 프리랜서화면으로 이동
                case R.id.navigation_freelancer:
                    Intent intentHome = new Intent(CategoryActivity.this, FreelancerActivity.class);
                    startActivity(intentHome);
                    break;
                // 내프로필화면으로이동
                case R.id.navigation_profile:
                    Intent intentMyProfile = new Intent(CategoryActivity.this, ProfileActivity.class);
                    startActivity(intentMyProfile);
                    break;
                // 장바구니화면으로이동
                case R.id.navigation_category:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Log.e("TAG","CategoryActivity(장바구니화면 들어옴) onCreate");
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // 이화면에 들어왔을때 초기아이템설정
        navView.setSelectedItemId(R.id.navigation_category);

        // 로그인정보불러오기
        loadMaintainUser();
        text_cart_check_count = (TextView) findViewById(R.id.text_cart_check_count);
        linear_cart_check_count = (LinearLayout) findViewById(R.id.linear_cart_check_count);
        check_cart_all = (CheckBox) findViewById(R.id.check_cart_all);
        mTextViewResult = (TextView) findViewById(R.id.text_cart_result);

        


        cartRecyclerView = (RecyclerView) findViewById(R.id.recylce_cart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cartRecyclerView.setLayoutManager(linearLayoutManager);
//        cartListArrayList = new ArrayList<>();
//        cartAdapter = new CartAdapter(cartListArrayList, CategoryActivity.this);
//        cartRecyclerView.setAdapter(cartAdapter);
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        cartRecyclerView.setHasFixedSize(false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(cartRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        cartRecyclerView.addItemDecoration(dividerItemDecoration);


        Log.e("TAG","loginedEmail"+loginedEmail);
        Log.e("TAG","loginedName"+loginedName);
//        Log.e("tag","cartListArrayList size"+cartListArrayList.size());
        GetCartInformation getCartInformation = new GetCartInformation();
        getCartInformation.execute("http://" + IP_ADDRESS + "/home/virtualclothes/frontend/clothes_cart_list.php",loginedEmail,loginedName);
        Log.e("TAG","loginedEmail"+loginedEmail);
        Log.e("TAG","loginedName"+loginedName);
//        GetAllPrice getAllPrice = new GetAllPrice();
//        getAllPrice.execute("http://" + IP_ADDRESS + "/home/virtualclothes/frontend/clothes_cart_list_price.php",loginedEmail,loginedName);


    }


    private class GetCartInformation extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(CategoryActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);
            Log.e(TAG, "response - " + result);
            if (result == null){
                Log.e(TAG, "response - " + result);
                mTextViewResult.setText(errorString);
                return;
            }else {
                    Log.e(TAG, "response - " + result);
                    mJsonString = result;
                    showResult();
            }






        }


        @Override
        protected String doInBackground(String... params) {

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = params[0];
            
            String loginedEmail = (String)params[1];
            String loginedName = (String)params[2];

            Log.e("TAG","loginedEmail"+loginedEmail);
            Log.e("TAG","loginedName"+loginedName);
            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
            String postParameters = "loginedEmail=" + loginedEmail + "&loginedName=" + loginedName;
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
                // HTTP 200 OK는 요청이 성공했음을 나타내는 성공 응답 상태 코드입니다. 기본값에서 200 응답은 캐시에 저장할 수 있습니다.
                Log.e(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

//                bmImg = BitmapFactory.decodeStream(inputStream);


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                Log.e(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){

        String TAG_JSON="cart_list_result";
        String TAG_CART_LIST_USERID = "cart_user_id";
        String TAG_CART_LIST_USERNAME = "cart_user_name";
        String TAG_CART_LIST_COLOR = "cart_color";
        String TAG_CART_LIST_PRICETOTAL ="cart_price_total";
        String TAG_CART_LIST_PRICE ="cart_price";
        String TAG_CART_LIST_INFORMATION ="cart_information";
        String TAG_CART_LIST_IMAGE ="cart_path_image";
        String TAG_CART_LIST_DELIVERY_FEE ="cart_delivery_fee";
        String TAG_CART_LIST_CLOTHES_NAME ="cart_clothes_name";
        String TAG_CART_LIST_SIZE ="cart_size";
        String TAG_CART_LIST_SIZE_TOTAL ="cart_size_total";
        String TAG_CART_LIST_NUMBER_OF_PRODUCT ="cart_number_of_product";
        String TAG_CART_LIST_CLOTHES_NUMBER ="cart_clothes_number";
        String TAG_CART_LIST_COMPANY_NAME ="cart_clothes_company_name";
        String TAG_CART_LIST_CART_POINT ="cart_point";
        String TAG_CART_LIST_CART_NO ="cart_no";
        String TAG_CART_LIST_CART_DATE ="cart_date";


        String TAG_JSON1="total_cart_price";
        String TAG_SUM = "cart_money";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                Log.e("tag","jsonArray.length()"+jsonArray.length());
                JSONObject item = jsonArray.getJSONObject(i);

                String cart_userid = item.getString(TAG_CART_LIST_USERID);
                String cart_username = item.getString(TAG_CART_LIST_USERNAME);
                String cart_clothes_color = item.getString(TAG_CART_LIST_COLOR);
                String cart_pricetotal = item.getString(TAG_CART_LIST_PRICETOTAL);
                String cart_price = item.getString(TAG_CART_LIST_PRICE);
                String cart_information = item.getString(TAG_CART_LIST_INFORMATION);
                String cart_image = item.getString(TAG_CART_LIST_IMAGE);
                String cart_delivery_fee = item.getString(TAG_CART_LIST_DELIVERY_FEE);
                String cart_clothes_name = item.getString(TAG_CART_LIST_CLOTHES_NAME);
                String cart_size = item.getString(TAG_CART_LIST_SIZE);
                String cart_size_total = item.getString(TAG_CART_LIST_SIZE_TOTAL);
                String cart_numberofproduct = item.getString(TAG_CART_LIST_NUMBER_OF_PRODUCT);
                String cart_clothes_number = item.getString(TAG_CART_LIST_CLOTHES_NUMBER);
                String cart_company_name = item.getString(TAG_CART_LIST_COMPANY_NAME);
                String cart_point = item.getString(TAG_CART_LIST_CART_POINT);
                String cart_no = item.getString(TAG_CART_LIST_CART_NO);
                String cart_date = item.getString(TAG_CART_LIST_CART_DATE);

//                Uri uriImage = Uri.parse(image);
                Log.e("tag","CategoryActivity cart_userid"+cart_userid);
                Log.e("tag","CategoryActivity cart_username"+cart_username);
                Log.e("tag","CategoryActivity cart_clothes_color"+cart_clothes_color);

            }

            jsonObject = new JSONObject(mJsonString);
            jsonArray = jsonObject.getJSONArray(TAG_JSON1);

            Log.e("tag","jsonArray1.length()"+jsonArray.length());
            JSONObject item = jsonArray.getJSONObject(0);
            Log.e(TAG,"TAG_SUM json(전)"+TAG_SUM);
            totalMoney = item.getString(TAG_SUM);
            Log.e(TAG,"totalMoney"+totalMoney);
            Log.e(TAG,"TAG_SUM json(후)"+TAG_SUM);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
            Log.e(TAG, "showResult : ", e);
        }





    }

    private class GetAllPrice extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(CategoryActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            progressDialog.dismiss();
//            mTextViewResult.setText(result);
//            Log.d(TAG, "response - " + result);
//            Log.e(TAG, "response - " + result);
//            if (result == null){
//
//                mTextViewResult.setText(errorString);
//            }else if(result.equals("장바구니 목록이 비어있습니다")){
//                Log.e(TAG,"장바구니 목록이 비어있습니다."+result);
//                linear_cart_check_count.setVisibility(View.VISIBLE);
//                text_cart_check_count.setText(View.VISIBLE);
//            }else {
//
//                mJsonString = result;
//                showResult_price();
//
//            }



        }


        @Override
        protected String doInBackground(String... params) {

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = params[0];

            String loginedEmail = (String)params[1];
            String loginedName = (String)params[2];

            Log.e("TAG","loginedEmail"+loginedEmail);
            Log.e("TAG","loginedName"+loginedName);
            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
            String postParameters = "loginedEmail=" + loginedEmail + "&loginedName=" + loginedName;
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
                // HTTP 200 OK는 요청이 성공했음을 나타내는 성공 응답 상태 코드입니다. 기본값에서 200 응답은 캐시에 저장할 수 있습니다.
                Log.e(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

//                bmImg = BitmapFactory.decodeStream(inputStream);


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                Log.e(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


//    private void showResult_price(){
//
//        String TAG_JSON="cart_price";
//        String TAG_CART_PRICE = "cart_total_price";
//
//
//
//        try {
//            JSONObject jsonObject = new JSONObject(mJsonString);
//            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
//
//            for(int i=0;i<jsonArray.length();i++){
//                Log.e("tag","jsonArray.length()"+jsonArray.length());
//                JSONObject item = jsonArray.getJSONObject(i);
//
//                String cart_price = item.getString(TAG_CART_PRICE);
//                Log.e("tag","CategoryActivity cart_userid"+cart_price);
//
//
//
//
//            }
//
//            text_cart_total_price.setText(TAG_CART_PRICE);
//        } catch (JSONException e) {
//
//            Log.d(TAG, "showResult : ", e);
//            Log.e(TAG, "showResult : ", e);
//        }
//
//    }

    private  void initMaintainUser(){
        // 로그인 유지 shard 저장
        shared_maintainLogin = (SharedPreferences) getSharedPreferences("maintain_login", Activity.MODE_PRIVATE);
    }

    // 로그인 정보불러오기
    private  void loadMaintainUser(){
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

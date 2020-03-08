package com.example.chinacompetition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinacompetition.bottom_navigation.FreelancerActivity;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "34.64.144.139";
    private static String TAG = "회원가입";
    // 블록체인주소
    private static String blockchainURL = MyToken.getBcURL();

    EditText edit_sign_up_email; // 이메일
    EditText edit_signup_business_pw; // 비밀번호
    EditText edit_signup_business_pw_re; // 비밀번호확인
    EditText edit_signup_business_name; // 이름
    EditText edit_sign_up_phone; // 핸드폰

    Button btn_sign_up_phone; // 핸드폰인증
    Button btn_sign_up_check_email; // 이메일중복확인
    CheckBox check_sign_up_information1; // 1번약관
    CheckBox check_sign_up_information2; // 2번약관
    CheckBox check_sign_up_information3; // 3번약관
    CheckBox check_sign_up_information4; // 4번약관
    CheckBox check_sign_up_information_all; // 전체동의
    CheckBox check_sign_up_information1_detail; // 1번약관 화살표
    CheckBox check_sign_up_information2_detail; // 2번약관 화살표
    CheckBox check_sign_up_information3_detail; // 3번약관 화살표
    CheckBox check_sign_up_information4_detail; // 4번약관 화살표


    Button btn_sign_up_add; // 가입하기
    Button btn_sign_up_cancel; // 취소
    TextView text_sign_up_company_number_result; // 회사번호결과값
    TextView text_sign_up_company_name_result; // 회사이름결과값
    TextView text_sign_up_company_email_result; // 이메일 결과값
    TextView text_sign_up_company_pw_result; // 비밀번호 결과값
    TextView text_sign_up_company_pw_re_result; // 비밀번호 결과값
    TextView text_sign_up_company_phone_result; // 핸드폰 결과값
    TextView text_sign_up_name_result; // 회사이름 결과값

    private TextView mTextViewResult;

    TextView text_sign_up_detail1; // 1번약관 상세내용
    TextView text_sign_up_detail2; // 1번약관 상세내용
    TextView text_sign_up_detail3; // 1번약관 상세내용
    TextView text_sign_up_detail4; // 1번약관 상세내용

    String resultBlockChain;
    // 이메일,이름
    public String  SignUpEmail,SignUpName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.e("tag", "사업자 회원가입화면 onCreate");

        edit_sign_up_email = (EditText) findViewById(R.id.edit_sign_up_email);
        edit_signup_business_pw = (EditText) findViewById(R.id.edit_signup_business_pw);
        edit_signup_business_pw_re = (EditText) findViewById(R.id.edit_signup_business_pw_re);
        edit_signup_business_name = (EditText) findViewById(R.id.edit_signup_business_name);
        edit_sign_up_phone = (EditText) findViewById(R.id.edit_sign_up_phone);
        btn_sign_up_phone = (Button) findViewById(R.id.btn_sign_up_phone);
        btn_sign_up_check_email = (Button) findViewById(R.id.btn_sign_up_check_email);
        check_sign_up_information1 = (CheckBox) findViewById(R.id.check_sign_up_information1);
        check_sign_up_information2 = (CheckBox) findViewById(R.id.check_sign_up_information2);
        check_sign_up_information3 = (CheckBox) findViewById(R.id.check_sign_up_information3);
        check_sign_up_information4 = (CheckBox) findViewById(R.id.check_sign_up_information4);
        check_sign_up_information_all = (CheckBox) findViewById(R.id.check_sign_up_information_all);

        check_sign_up_information1_detail = (CheckBox) findViewById(R.id.check_sign_up_information1_detail);
        check_sign_up_information2_detail = (CheckBox) findViewById(R.id.check_sign_up_information2_detail);
        check_sign_up_information3_detail = (CheckBox) findViewById(R.id.check_sign_up_information3_detail);
        check_sign_up_information4_detail = (CheckBox) findViewById(R.id.check_sign_up_information4_detail);


        btn_sign_up_add = (Button) findViewById(R.id.btn_sign_up_add);
        btn_sign_up_cancel = (Button) findViewById(R.id.btn_sign_up_cancel);

        text_sign_up_company_number_result = (TextView) findViewById(R.id.text_sign_up_company_number_result);
        text_sign_up_company_name_result = (TextView) findViewById(R.id.text_sign_up_company_name_result);
        text_sign_up_company_email_result = (TextView) findViewById(R.id.text_sign_up_company_email_result);
        text_sign_up_company_pw_result = (TextView) findViewById(R.id.text_sign_up_company_pw_result);
        text_sign_up_company_pw_re_result = (TextView) findViewById(R.id.text_sign_up_company_pw_re_result);
        text_sign_up_company_phone_result = (TextView) findViewById(R.id.text_sign_up_company_phone_result);
        text_sign_up_name_result = (TextView) findViewById(R.id.text_sign_up_name_result);

        mTextViewResult = (TextView) findViewById(R.id.textView_main_result);

        text_sign_up_detail1 = (TextView) findViewById(R.id.text_sign_up_detail1);
        text_sign_up_detail2 = (TextView) findViewById(R.id.text_sign_up_detail2);
        text_sign_up_detail3 = (TextView) findViewById(R.id.text_sign_up_detail3);
        text_sign_up_detail4 = (TextView) findViewById(R.id.text_sign_up_detail4);

        // 체크박스 전체선택 혹은 전체해제

        check_sign_up_information_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (check_sign_up_information_all.isChecked()) {
                    check_sign_up_information1.setChecked(true);
                    check_sign_up_information2.setChecked(true);
                    check_sign_up_information3.setChecked(true);
                    check_sign_up_information4.setChecked(true);
                } else {
                    check_sign_up_information1.setChecked(false);
                    check_sign_up_information2.setChecked(false);
                    check_sign_up_information3.setChecked(false);
                    check_sign_up_information4.setChecked(false);
                }

            }
        });





        // 이메일중복확인
        btn_sign_up_check_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String SignUpEmail = edit_sign_up_email.getText().toString();

                CheckEmailId checkEmailId = new CheckEmailId();
                checkEmailId.execute("http://" + IP_ADDRESS + "/user/checkemail.php", SignUpEmail);
                Log.e("tag", "이메일 중복확인");
            }
        });

        // 사업자 회원가입
        btn_sign_up_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (check_sign_up_information1.isChecked() && check_sign_up_information2.isChecked() &&
                        check_sign_up_information3.isChecked() && check_sign_up_information4.isChecked()) {

                    text_sign_up_company_number_result.setVisibility(View.GONE);
                    text_sign_up_company_name_result.setVisibility(View.GONE);
                    text_sign_up_company_email_result.setVisibility(View.GONE);
                    text_sign_up_company_pw_result.setVisibility(View.GONE);
                    text_sign_up_company_pw_re_result.setVisibility(View.GONE);
                    text_sign_up_company_phone_result.setVisibility(View.GONE);
                    text_sign_up_name_result.setVisibility(View.GONE);

                    
                    SignUpEmail = edit_sign_up_email.getText().toString();
                    String SignUpPw = edit_signup_business_pw.getText().toString();
                    String SignUpPwRE = edit_signup_business_pw_re.getText().toString();
                    SignUpName = edit_signup_business_name.getText().toString();
                    String SignUpPhone = edit_sign_up_phone.getText().toString();

                    InsertBusinessMember insertBusinessMember = new InsertBusinessMember();
                    insertBusinessMember.execute("http://" + IP_ADDRESS + "/user/signup.php",
                            SignUpEmail, SignUpPw, SignUpPwRE, SignUpName, SignUpPhone);
                    Log.e("tag", "보내는값" + SignUpEmail + SignUpPw + SignUpName);
                    Log.e("tag", "사업자 회원가입화면 > 홈 화면으로 이동");

                } else if (check_sign_up_information_all.isChecked()) {
                    text_sign_up_company_number_result.setVisibility(View.GONE);
                    text_sign_up_company_name_result.setVisibility(View.GONE);
                    text_sign_up_company_email_result.setVisibility(View.GONE);
                    text_sign_up_company_pw_result.setVisibility(View.GONE);
                    text_sign_up_company_pw_re_result.setVisibility(View.GONE);
                    text_sign_up_company_phone_result.setVisibility(View.GONE);
                    text_sign_up_name_result.setVisibility(View.GONE);


                    SignUpEmail = edit_sign_up_email.getText().toString();
                    String SignUpPw = edit_signup_business_pw.getText().toString();
                    String SignUpPwRE = edit_signup_business_pw_re.getText().toString();
                    SignUpName = edit_signup_business_name.getText().toString();
                    String SignUpPhone = edit_sign_up_phone.getText().toString();


                    InsertBusinessMember insertBusinessMember = new InsertBusinessMember();
                    insertBusinessMember.execute("http://" + IP_ADDRESS + "/user/signup.php",
                            SignUpEmail, SignUpPw, SignUpPwRE, SignUpName, SignUpPhone);

                    Log.e("tag", "보내는값" + SignUpEmail + SignUpPw + SignUpName);
                    Log.e("tag", "사업자 회원가입화면 > 홈 화면으로 이동");

                }else{
                    text_sign_up_company_email_result.setVisibility(View.GONE);
                    text_sign_up_company_pw_result.setVisibility(View.GONE);
                    text_sign_up_company_pw_re_result.setVisibility(View.GONE);
                    text_sign_up_company_phone_result.setVisibility(View.GONE);
                    text_sign_up_name_result.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "체크박스 동의를 해주세요!.", Toast.LENGTH_LONG).show();
                }


            }
        });

        btn_sign_up_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("tag", "사업자 회원가입화면 > 회원가입 화면으로 이동");
                Intent intent = new Intent(SignUpActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // 1번약관화살표 (화살표누르면 약관내용보이고,다시누르면 사라짐)
        check_sign_up_information1_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_sign_up_information1_detail.isChecked()) {
                    text_sign_up_detail1.setVisibility(View.VISIBLE);
                } else {
                    text_sign_up_detail1.setVisibility(View.GONE);
                }
            }
        });

        // 2번약관화살표 (화살표누르면 약관내용보이고,다시누르면 사라짐)
        check_sign_up_information2_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_sign_up_information2_detail.isChecked()) {
                    text_sign_up_detail2.setVisibility(View.VISIBLE);
                } else {
                    text_sign_up_detail2.setVisibility(View.GONE);
                }
            }
        });

        // 3번약관화살표 (화살표누르면 약관내용보이고,다시누르면 사라짐)
        check_sign_up_information3_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_sign_up_information3_detail.isChecked()) {
                    text_sign_up_detail3.setVisibility(View.VISIBLE);
                } else {
                    text_sign_up_detail3.setVisibility(View.GONE);
                }
            }
        });

        // 4번약관화살표 (화살표누르면 약관내용보이고,다시누르면 사라짐)
        check_sign_up_information4_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_sign_up_information4_detail.isChecked()) {
                    text_sign_up_detail4.setVisibility(View.VISIBLE);
                } else {
                    text_sign_up_detail4.setVisibility(View.GONE);
                }
            }
        });
    }

   


    class InsertBusinessMember extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            String Org1 = "Org1";
            Log.e(TAG, "POST response  - " + result);
//            SignUpBlockChain signUpBlockChain = new  SignUpBlockChain();
            Log.e(TAG,"SignUpName"+SignUpName);
//            signUpBlockChain.execute("http://" + blockchainURL + "users", SignUpEmail
//                    ,Org1);


            loginRequest(SignUpEmail);
            Toast.makeText(getApplicationContext(), "회원가입이되었습니다.", Toast.LENGTH_LONG).show();
            mTextViewResult.setText(result);
            Intent intent2 = new Intent(SignUpActivity.this, FreelancerActivity.class);
            startActivity(intent2);



            if (result.equals("이메일을 입력하세요.")) {
                text_sign_up_company_email_result.setVisibility(View.VISIBLE);
                text_sign_up_company_email_result.setText(result);
            } else if (result.equals("비밀번호를 입력하세요.")) {
                text_sign_up_company_pw_result.setVisibility(View.VISIBLE);
                text_sign_up_company_pw_result.setText(result);
            } else if (result.equals("비밀번호확인을 입력하세요.")) {
                text_sign_up_company_pw_re_result.setVisibility(View.VISIBLE);
                text_sign_up_company_pw_re_result.setText(result);
            } else if (result.equals("비밀번호가 일치하지않습니다.")) {
                text_sign_up_company_pw_result.setVisibility(View.VISIBLE);
                text_sign_up_company_pw_result.setText(result);
            } else if (result.equals("이름을 입력하세요.")) {
                text_sign_up_name_result.setVisibility(View.VISIBLE);
                text_sign_up_name_result.setText(result);
            } else if (result.equals("회원가입이되었습니다.")) {



                Toast.makeText(getApplicationContext(), "회원가입이되었습니다.", Toast.LENGTH_LONG).show();
                mTextViewResult.setText(result);
                Intent intent = new Intent(SignUpActivity.this, FreelancerActivity.class);
                startActivity(intent);
            } else {

            }


        }

        @Override
        protected String doInBackground(String... params) {


            String SignUpEmail = (String) params[1];
            String SignUpPw = (String) params[2];
            String SignUpPwRE = (String) params[3];
            String SignUpName = (String) params[4];
            String SignUpPhone = (String) params[5];


            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
            String postParameters = "&SignUpEmail=" + SignUpEmail + "&SignUpPw=" + SignUpPw + "&SignUpPwRE=" + SignUpPwRE + "&SignUpName=" + SignUpName + "&SignUpPhone=" + SignUpPhone ;
            Log.e("TAG","SignUpEmail"+SignUpEmail);
            Log.e("TAG","SignUpPw"+SignUpPw);
            Log.e("TAG","SignUpPhone"+SignUpPhone);

            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.
                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.
                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된
                // 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.
                outputStream.flush();
                outputStream.close();

                // 3. 응답을 읽습니다.
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    // 에러 발생
                    inputStream = httpURLConnection.getErrorStream();
                }

                // 4. StringBuilder를 사용하여 수신되는 데이터를 저장합니다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();

                // 5. 저장된 데이터를 스트링으로 변환하여 리턴합니다.
                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }

    class CheckEmailId extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "POST response  - " + result);
            Log.e(TAG, "이메일 아이디 존재여부  - " + result);
            if (result.equals("사용가능한 이메일입니다.")) {
                Log.e(TAG, "이메일  존재여부 : 사용가능한 이메일입니다.  - " + result);
                text_sign_up_company_email_result.setVisibility(View.VISIBLE);
                text_sign_up_company_email_result.setText(result);
            } else if (result.equals("이미 가입한 이메일입니다.")) {
                Log.e(TAG, "이메일  존재여부 : 사용불가능한 이메일입니다. - " + result);
                text_sign_up_company_email_result.setVisibility(View.VISIBLE);
                text_sign_up_company_email_result.setText(result);
            } else if (result.equals("잘못된 이메일 형식입니다.")) {
                Log.e(TAG, "이메일  존재여부 : 잘못된 이메일 형식입니다. - " + result);
                text_sign_up_company_email_result.setVisibility(View.VISIBLE);
                text_sign_up_company_email_result.setText(result);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String SignUpEmail = (String) params[1];


            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
            String postParameters = "&SignUpEmail=" + SignUpEmail;


            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.
                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.
                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된
                // 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.
                outputStream.flush();
                outputStream.close();

                // 3. 응답을 읽습니다.
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    // 에러 발생
                    inputStream = httpURLConnection.getErrorStream();
                }

                // 4. StringBuilder를 사용하여 수신되는 데이터를 저장합니다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();

                // 5. 저장된 데이터를 스트링으로 변환하여 리턴합니다.
                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "EmailData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }

    class SignUpBlockChain extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
//            text_email_login_result.setText(result);
            Log.d(TAG, "POST response block -  " + result);
            Log.e(TAG, "POST response block  - " + result);

            resultBlockChain = result;



//            Toast.makeText(getApplicationContext(), "로그인이되었습니다.", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(LoginActivity.this,FreelancerActivity.class);
//            startActivity(intent);

        }

        @Override
        protected String doInBackground(String... params) {

            String username = (String)params[1];
            String orgName = (String)params[2];


            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String)params[0];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
            String postParameters = "username=" + username + "&orgName=" + orgName;
            Log.e(TAG,"SignUpName_doInBack"+username);

            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.
                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.
                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된
                // 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.
                outputStream.flush();
                outputStream.close();

                // 3. 응답을 읽습니다.
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);
                Log.e(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    // 에러 발생
                    inputStream = httpURLConnection.getErrorStream();
                }

                // 4. StringBuilder를 사용하여 수신되는 데이터를 저장합니다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();

                // 5. 저장된 데이터를 스트링으로 변환하여 리턴합니다.
                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                Log.e(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }

    private void make(){
        //JSON만들기

    }


    //아이디 지갑 생성 (회원가입시 만들어줄것 / 에스크로 에서 임시 돈 보관 할때 "계약자ID"+"프리랜서ID" 로 만들어줄것. )
    private void makeIdAndCash(String makeId, String initCash){
        Log.d(TAG, "moveCash");

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
                    Log.e(TAG, "onResponse: "+response);
                }else {
                    Log.d(TAG, "response null");
                    Log.e(TAG, "response null");
                }
                try {
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    Log.e(TAG, "result : "+ result);
                    if(response.isSuccessful()){
                        Log.d(TAG, "result is successful: "+result);
                        Log.e(TAG, "result is successful: "+result);
                        Toast.makeText(getApplicationContext(), "송금완료.", Toast.LENGTH_SHORT).show();

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
            }
        });
    }



    // 블록체인 네트워크에 접속해서 로그인하는 것. 이후 토큰을 받아서 보관할 예정이다.
    private void loginRequest(String id){
        Log.d(TAG, "loginRequest");
        Log.e(TAG, "loginRequest");
        HashMap hashMap = new HashMap();
        hashMap.put("username", id);
        hashMap.put("orgName", "Org1");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(blockchainURL)
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = retrofitService.loginRequest(hashMap);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: "+response);
                try {
                    // 서버에서 보낸 값을 스트링에 담음.
                    String result = response.body().string();
                    if(response.isSuccessful()){
                        Log.d(TAG, "result is successful: "+result);
                        Log.e(TAG, "result is successful: "+result);

                        JSONObject jsonObject = new JSONObject(result);
                        MyToken.setmToken(jsonObject.getString("token"));
                        Log.d(TAG, "token: "+ MyToken.getmToken());
                        Log.e(TAG, "token: "+ MyToken.getmToken());

                        makeIdAndCash(SignUpEmail,"0");

                    }else{
                        Log.d(TAG, "unsuccessful: "+result);
                        Log.e(TAG, "unsuccessful: "+result);
                        Toast.makeText(getApplicationContext(), "서버와 통신이 좋지 않아요.", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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
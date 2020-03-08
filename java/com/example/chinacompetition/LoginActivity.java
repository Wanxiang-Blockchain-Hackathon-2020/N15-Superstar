package com.example.chinacompetition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.chinacompetition.freelancer.FreelancerWritingActivity;

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
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    /****** 이메일 로그인화면 *******/

    Button btn_email_login_login ; // 로그인 버튼
    EditText edit_email_login_id ; // 아이디 입력칸
    EditText edit_email_login_pw ; // 비밀번호 입력칸
    TextView text_email_login_signup ; // 회원가입
    TextView text_email_login_pw_result ; // 비밀번호 로그인 결과값
    TextView text_email_login_id_result ; // 아이디 로그인 결과값
    CheckBox check_login_auto_login ; // 자동로그인
    private static String IP_ADDRESS = "34.64.144.139";
    String blockchainURL = MyToken.getBcURL();
    private static String TAG = "이메일로그인";
    SharedPreferences shared_maintainLogin ; // 로그인 정보 유지 Shard
    String login_id;
    private String mJsonString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_email_login_login = (Button) findViewById(R.id.btn_email_login_login);
        edit_email_login_id = (EditText) findViewById(R.id.edit_email_login_id);
        edit_email_login_pw = (EditText) findViewById(R.id.edit_email_login_pw);
        text_email_login_signup = (TextView) findViewById(R.id.text_email_login_signup);
        text_email_login_pw_result = (TextView) findViewById(R.id.text_email_login_pw_result);
        text_email_login_id_result = (TextView) findViewById(R.id.text_email_login_id_result);
        check_login_auto_login = (CheckBox) findViewById(R.id.check_login_auto_login);
        // 로그인버튼누르면 홈화면으로 이동

        text_email_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        btn_email_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    text_email_login_id_result.setVisibility(View.GONE);
                    text_email_login_pw_result.setVisibility(View.GONE);

                    login_id = edit_email_login_id.getText().toString();
                    String login_pw = edit_email_login_pw.getText().toString();

                    Log.e("tag","로그인화면 > 홈화면으로 이동");
                    LoginCheck loginCheck = new LoginCheck();
                    loginCheck.execute("http://" + IP_ADDRESS + "/user/login.php", login_id
                            ,login_pw);




                // 자동로그인 체크여부
                // 체크되었을때
//                if(check_login_auto_login.isChecked()){
//                    text_email_login_id_result.setVisibility(View.GONE);
//                    text_email_login_pw_result.setVisibility(View.GONE);
//
//                    String login_id = edit_email_login_id.getText().toString();
//                    String login_pw = edit_email_login_pw.getText().toString();
//
//                    Log.e("tag","로그인화면 > 홈화면으로 이동");
//                    LoginCheck loginCheck = new LoginCheck();
//                    loginCheck.execute("http://" + IP_ADDRESS + "/home/virtualclothes/backend/login_ok.php", login_id
//                            ,login_pw);
//
//
//                    // 안되었을때
//                }else{
//                    text_email_login_id_result.setVisibility(View.GONE);
//                    text_email_login_pw_result.setVisibility(View.GONE);
//
//                    String login_id = edit_email_login_id.getText().toString();
//                    String login_pw = edit_email_login_pw.getText().toString();
//
//                    Log.e("tag","로그인화면 > 홈화면으로 이동");
//                    LoginCheck loginCheck = new LoginCheck();
//                    loginCheck.execute("http://" + IP_ADDRESS + "/home/virtualclothes/backend/login_ok.php", login_id
//                            ,login_pw);
//                }


//                Intent intent = new Intent(LoginActivity.this,FreelancerActivity.class);
//                startActivity(intent);
            }
        });
    }

    class LoginCheck extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
//            text_email_login_result.setText(result);
            Log.d(TAG, "POST response  - " + result);
            Log.e(TAG, "POST response  - " + result);



            if(result.equals("로그인이되었습니다")){

//                Log.e(TAG, "로그인이되었습니다 (intent코드 읽기전) - " + result);
//                Intent intent = new Intent(LoginActivity.this,FreelancerActivity.class);
//                startActivity(intent);
//                Log.e(TAG, "로그인이되었습니다 (intent코드 읽은후)  - " + result);
            }else if(result.equals("아이디혹은비밀번호가틀렸습니다")){
                Log.e(TAG, "아이디혹은비밀번호가틀렸습니다  - " + result);
                text_email_login_id_result.setVisibility(View.VISIBLE);
                text_email_login_id_result.setText(result);
            }else if(result.equals("아이디를 입력하세요.")){
                Log.e(TAG, "아이디를 입력하세요.  - " + result);
                text_email_login_id_result.setVisibility(View.VISIBLE);
                text_email_login_id_result.setText(result);
            }else if(result.equals("비밀번호를 입력하세요.")){
                Log.e(TAG, "비밀번호를 입력하세요.  - " + result);
                text_email_login_pw_result.setVisibility(View.VISIBLE);
                text_email_login_pw_result.setText(result);
            }else{
                mJsonString = result;
                Log.e(TAG, "로그인이되었습니다 (mJsonString) - " + mJsonString);
//                showResult();


                    showResult2();
                  loginRequest(login_id);
//                String NUM = "num";
//                String ID = "id";
//                String NAME = "name";
//                String PHONENUM = "phonenum";
//                String TITLE = "title";
//                String SKILL = "skill";
//                String INTRODUCTION = "introduction";
//                String LOCATION = "location";
//                String IMAGES = "images";
//                Log.e(TAG, "id" +ID+"name"+NAME);
//                shared_maintainLogin = getSharedPreferences("maintain_login", Activity.MODE_PRIVATE);
//                SharedPreferences.Editor maintainLogin = shared_maintainLogin.edit();
//                maintainLogin.putString("num",NUM);
//                maintainLogin.putString("id",ID);
//                maintainLogin.putString("name",NAME);
//                maintainLogin.putString("phonenum",PHONENUM);
//                maintainLogin.putString("title",TITLE);
//                maintainLogin.putString("skill",SKILL);
//                maintainLogin.putString("introduction",INTRODUCTION);
//                maintainLogin.putString("location",LOCATION);
//                maintainLogin.putString("images",IMAGES);
//                maintainLogin.commit();

                Log.e(TAG, "로그인이되었습니다 (intent코드 읽기전) - " + result);
                Intent intent = new Intent(LoginActivity.this, FreelancerActivity.class);
                Toast.makeText(LoginActivity.this,"로그인이되었습니다.", Toast.LENGTH_SHORT);
                startActivity(intent);
                Log.e(TAG, "로그인이되었습니다 (intent코드 읽은후)  - " + result);
            }



//            Toast.makeText(getApplicationContext(), "로그인이되었습니다.", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(LoginActivity.this,FreelancerActivity.class);
//            startActivity(intent);

        }

        @Override
        protected String doInBackground(String... params) {

            String login_id = (String)params[1];
            String login_pw = (String)params[2];


            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String)params[0];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
            String postParameters = "login_id=" + login_id + "&login_pw=" + login_pw;


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

                return new String("Error: " + e.getMessage());
            }
        }
    }



    private void showResult2() {
        Log.e(TAG, "showResult2들어옴  - " + mJsonString);
//        String TAG_JSON="";
//        String NUM = "num";
//        String ID = "id";
//        String NAME = "name";
//        String PHONENUM = "phonenum";
//        String TITLE = "title";
//        String SKILL = "skill";
//        String INTRODUCTION = "introduction";
//        String LOCATION = "location";
//        String IMAGES = "images";

        try {
            JSONArray jsonArray = new JSONArray(mJsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            Log.e(TAG, "in try"+jsonArray.toString()+jsonObject.toString());
            String num = jsonObject.getString("num");
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            CommonValue.setId(id);
            CommonValue.setName(name);
            Log.e(TAG, "Login(id,name)"+id+name);
//            String phonenum = jsonObject.getString("phonenum");
//            String title = jsonObject.getString("title");
//            String skill = jsonObject.getString("skill");
//            String introduction = jsonObject.getString("introduction");
//            String location = jsonObject.getString("location");
//            String images = jsonObject.getString("images");
            Log.e(TAG, "id" +id+"name"+name);
//            shared_maintainLogin = getSharedPreferences("maintain_login", Activity.MODE_PRIVATE);
//            SharedPreferences.Editor maintainLogin = shared_maintainLogin.edit();
//            maintainLogin.putString("num",num);
//            maintainLogin.putString("id",id);
//            maintainLogin.putString("name",name);
//            maintainLogin.putString("phonenum",phonenum);
//            maintainLogin.putString("title",title);
//            maintainLogin.putString("skill",skill);
//            maintainLogin.putString("introduction",introduction);
//            maintainLogin.putString("location",location);
//            maintainLogin.putString("images",images);
//            maintainLogin.commit();
        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }

    private void showResult(){
        Log.e(TAG, "showResult들어옴  - " + mJsonString);
        String TAG_JSON="";
        String NUM = "num";
        String ID = "id";
        String NAME = "name";
        String PHONENUM = "phonenum";
        String TITLE = "title";
        String SKILL = "skill";
        String INTRODUCTION = "introduction";
        String LOCATION = "location";
        String IMAGES = "images";
      
        
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                Log.e(TAG, "showResult들어옴 for문에 들어가니?  - " + mJsonString);
                JSONObject item = jsonArray.getJSONObject(i);

                String num = item.getString(NUM);
                String id = item.getString(ID);
                String name = item.getString(NAME);
                String phonenum = item.getString(PHONENUM);
                String title = item.getString(TITLE);
                String skill = item.getString(SKILL);
                String introduction= item.getString(INTRODUCTION);
                String location = item.getString(LOCATION);
                String images = item.getString(IMAGES);
                Log.e(TAG, "id" +ID+"name"+NAME);
                shared_maintainLogin = getSharedPreferences("maintain_login", Activity.MODE_PRIVATE);
                SharedPreferences.Editor maintainLogin = shared_maintainLogin.edit();
                maintainLogin.putString("num",num);
                maintainLogin.putString("id",id);
                maintainLogin.putString("name",name);
                maintainLogin.putString("phonenum",phonenum);
                maintainLogin.putString("title",title);
                maintainLogin.putString("skill",skill);
                maintainLogin.putString("introduction",introduction);
                maintainLogin.putString("location",location);
                maintainLogin.putString("images",images);
                maintainLogin.commit();
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    // 블록체인 네트워크에 접속해서 로그인하는 것. 이후 토큰을 받아서 보관할 예정이다.
    private void loginRequest(String id){
        Log.d(TAG, "loginRequest");
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

                        JSONObject jsonObject = new JSONObject(result);
                        MyToken.setmToken(jsonObject.getString("token"));
                        Log.d(TAG, "token: "+ MyToken.getmToken());
                    }else{
                        Log.d(TAG, "unsuccessful: "+result);
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
            }
        });
    }

}

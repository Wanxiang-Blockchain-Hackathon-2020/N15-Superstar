package com.example.chinacompetition.freelancer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chinacompetition.CheckDoc.AddImageRCAdapter;
import com.example.chinacompetition.CommonValue;
import com.example.chinacompetition.R;
import com.example.chinacompetition.RealPathFromURI;
import com.example.chinacompetition.RetrofitService;
import com.example.chinacompetition.bottom_navigation.FreelancerActivity;
import com.example.chinacompetition.bottom_navigation.ProfileActivity;
import com.example.chinacompetition.camera.ImageResizeUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.Part;

public class FreelancerWritingActivity extends AppCompatActivity {

    /****** 프리랜서등록화면 *******/
    private static String IP_ADDRESS = "http://34.64.144.139/";
    private static String tag = "프리랜서등록화면";
    // 등록을 하지않았다면, 최초에 1번등록할 수 있다.
    // 프리랜서를 등록한뒤에 수정은 가능하다.
    // 프리랜서가 이미 등록되어있으면 등록할 수 없다.

    EditText edit_freelancer_writing_title; // 프리랜서제목
    TextView text_freelancer_writing_name; // 프리랜서이름
    EditText edit_freelancer_writing_skill;// 프리랜서기술
    EditText edit_freelancer_writing_introduce; // 프리랜서자기소개
    EditText edit_freelancer_writing_location; // 프리랜서위치
    ImageButton imageBtn_freelancer_writing_camera; // 카메라버튼
    ImageButton imageBtn_freelancer_writing_album; // 앨범버튼
    ImageView image_freelancer_writing_picture; // 이미지
    Button btn_freelancer_writing_post; // 등록하기
    Button btn_freelancer_writing_cancel; // 취소

    // 카메라 관련변수
    private static final String TAG = "blackjin";

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private Boolean isCamera = false;
    private File tempFile;
    private Uri savingUri = Uri.parse("file:///" + Environment.getExternalStorageDirectory()); // crop후 저장할 Uri

    SharedPreferences shared_maintainLogin; // 로그인 정보 유지 Shard
    // 로그인정보 담은변수
    String num, id, name, phonenum, title, skill, introduction, location, images;

    // 앨범,카메라에서 가져온 이미지 서버로 전송하는 변수
    TextView messageText;

    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String upLoadServerUri = null;
    /**********  File Path *************/
    private String uploadFilePath = "storage/emulated/0/blackJin/";//경로를 모르겠으면, 갤러리 어플리케이션 가서 메뉴->상세 정보
    private String uploadFileName = "blackJin_175346_6424269135152691202.jpg"; //전송하고자하는 파일 이름
    // 기존예제는 두개로 나눠놨는데, 굳이 두개로 나눌필요없어서 하나의 변수로 퉁침
    private String uploadFilePathName = "";

    // inner 클래스에 사용하면서 final로 사용하라고해서 그냥 전역변수로 선언
    String listTitle, listName, listSkill, listIntroduce, listLocation;

    AddImageRCAdapter adapter = new AddImageRCAdapter(this);
    Uri photoUri;

    String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("tag", "프리랜서등록화면 onCreate");
        setContentView(R.layout.activity_freelancer_writing);

        text_freelancer_writing_name = (TextView) findViewById(R.id.text_freelancer_writing_name);
        edit_freelancer_writing_title = (EditText) findViewById(R.id.edit_freelancer_writing_title);
        edit_freelancer_writing_skill = (EditText) findViewById(R.id.edit_freelancer_writing_skill);
        edit_freelancer_writing_introduce = (EditText) findViewById(R.id.edit_freelancer_writing_introduce);
        edit_freelancer_writing_location = (EditText) findViewById(R.id.edit_freelancer_writing_location);
        imageBtn_freelancer_writing_camera = (ImageButton) findViewById(R.id.imageBtn_freelancer_writing_camera);
        imageBtn_freelancer_writing_album = (ImageButton) findViewById(R.id.imageBtn_freelancer_writing_album);
        image_freelancer_writing_picture = (ImageView) findViewById(R.id.image_freelancer_writing_picture);
        btn_freelancer_writing_post = (Button) findViewById(R.id.btn_freelancer_writing_post);
        btn_freelancer_writing_cancel = (Button) findViewById(R.id.btn_freelancer_writing_cancel);


        // 로그인한 유저정보불러오기
        loadMaintainUser();

        text_freelancer_writing_name.setText(CommonValue.getName());



        messageText = (TextView) findViewById(R.id.messageText);
        messageText.setText("Uploading file path :- 'storage/emulated/0/" + uploadFileName + "'");

        // 카메라 권한 library
        tedPermission();

        findViewById(R.id.imageBtn_freelancer_writing_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if (isPermission) goToAlbum();
                else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.imageBtn_freelancer_writing_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if (isPermission) takePhoto();
                else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();

            }
        });



        // 등록버튼을 누르면 프리랜서가 등록이된다. 그리고 홈화면으로 이동한다.
        btn_freelancer_writing_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "프리랜서등록화면 > 홈화면으로 이동");


                listTitle = edit_freelancer_writing_title.getText().toString();
                listName = text_freelancer_writing_name.getText().toString();
                listSkill = edit_freelancer_writing_skill.getText().toString();
                listIntroduce = edit_freelancer_writing_introduce.getText().toString();
                listLocation = edit_freelancer_writing_location.getText().toString();

                if (listIntroduce.length() == 0) {
                    AlertDialog.Builder builder_id = new AlertDialog.Builder(FreelancerWritingActivity.this);
                    builder_id.setMessage("프리랜서설명을 입력해주세요!");
                    builder_id.setTitle("프리랜서설명");
                    AlertDialog alertDialog = builder_id.create();
                    alertDialog.show();
                } else if (listLocation.length() == 0) {
                    AlertDialog.Builder builder_id = new AlertDialog.Builder(FreelancerWritingActivity.this);
                    builder_id.setMessage("프리랜서키워드를 입력해주세요!");
                    builder_id.setTitle("프리랜서키워드");
                    AlertDialog alertDialog = builder_id.create();
                    alertDialog.show();
                } else {

                    uploadImageToServer();
                }

            }
        });

        // 취소버튼을 누르면 프로필화면으로 이동
        btn_freelancer_writing_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "프리랜서등록화면 > 프로필화면으로 이동");
                Intent intent = new Intent(FreelancerWritingActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }



    // 서버에 이미지보내기
    private void uploadImageToServer(){
        Log.d(TAG, "uploadImageToServer");
        Log.e(TAG, "uploadImageToServer");



        // 레트로핏을 사용한다는 말임. URL은 연결할 서버 URL이고, Gson을 이용해 Json 값을 변환한다는 뜻.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonValue.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 레트로핏 서비스를 레트로핏 객체에 넣어줌.
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = null;

        // 앨범에서 가져온 이미지의 주소로 파일을 하나 생성함.
        File file = new File(imagePath);
        Log.e(TAG,"upload_imagePath: "+imagePath);
        // 요청하는 내용에 멀티파트 폼 데이터라는 것을 명시하고, 파일을 같이 넣어줌.
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // 멀티파트로 된 업로드파일을 하나 만들고, 이름을 files (이건 서버에서 불러올 이름이 됨), 이미지 주소와 요청내용을 달아줌.
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("file", imagePath, requestBody);
        Log.e(TAG,"upload_uploadFile: "+uploadFile);
        // 콜 객체를 만들어서 레트로핏 서비스에 만들어 놓은 함수를 넣는다.
        call = retrofitService.uploadImage(uploadFile,listTitle,listName,listSkill,listIntroduce,listLocation,CommonValue.getId());
        Log.e(TAG,"retrofitService.uploadImage: "+listTitle+""+listName+""+listSkill+""+listIntroduce+""+listLocation+""+CommonValue.getId());
        Log.e(TAG,"retrofitService.uploadImage: "+uploadFile);
        // 앨범에서 이미지를 지정했다면 이미지를 서버에 보내는 과정도 같이 해주고, 이미지 변경이 없다면 그냥 닉네임 변경만 진행한다. (예외처리임)
//        if(imagePath != null){
//
//        }else{
//            // 위의 콜은 사진도 변경되었을 때 실행되는 것이고, 이 콜은 닉네임만 변경되었을 때 실행되는 것.
////            call = retrofitService.uploadImage(up);
//        }

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
                        // 프리랜서 목록화면으로 이동
                        Toast.makeText(FreelancerWritingActivity.this,"프리랜서 등록이 되었습니다.", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(FreelancerWritingActivity.this,FreelancerActivity.class);
                        startActivity(intent);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {

            photoUri = data.getData();

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));
                imagePath = cursor.getString(column_index);

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
//
//            if (tempFile != null) {
//                if (tempFile.exists()) {
//
//                    if (tempFile.delete()) {
//                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
//                        tempFile = null;
//                    }
//                }
//            }
//
//            return;
//        }
//
//        switch (requestCode) {
//            case PICK_FROM_ALBUM: {
//
//                Uri photoUri = data.getData();
//                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);
//                Log.e(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);
//                cropImage(photoUri);
//
//                break;
//            }
//            case PICK_FROM_CAMERA: {
//
//                Uri photoUri = Uri.fromFile(tempFile);
//                Log.d(TAG, "takePhoto photoUri : " + photoUri);
//
//                cropImage(photoUri);
//
//                break;
//            }
//            case Crop.REQUEST_CROP: {
//                File cropFile = new File(Crop.getOutput(data).getPath());
//                setImage();
//            }
//        }
//    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void goToAlbum() {
        isCamera = false;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * 카메라에서 이미지 가져오기
     */
    private void takePhoto() {
        isCamera = true;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
            Log.d(TAG, "tempFile" + tempFile);
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            /**
             *  안드로이드 OS 누가 버전 이후부터는 file:// URI 의 노출을 금지로 FileUriExposedException 발생
             *  Uri 를 FileProvider 도 감싸 주어야 합니다.
             *
             *  참고 자료 http://programmar.tistory.com/4 , http://programmar.tistory.com/5
             */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.chinacompetition.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                Log.d(TAG, "takePhoto photoUri : " + photoUri);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }

    /**
     * Crop 기능
     */
    private void cropImage(Uri photoUri) {

        Log.d(TAG, "tempFile : " + tempFile);
        Log.e(TAG, "tempFile : " + tempFile);

        /**
         *  갤러리에서 선택한 경우에는 tempFile 이 없으므로 새로 생성해줍니다.
         */
        if (tempFile == null) {
            try {
                Log.e("tag", "cropImage_tempFile여기들어가니?");
                tempFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }

        //크롭 후 저장할 Uri
        Log.e("tag", "savingUri_before " + savingUri);
        savingUri = Uri.fromFile(tempFile);

        Log.e("tag", "savingUri_after " + savingUri);
        String imageUri = savingUri.toString();
        // file:/// 이부분이 있어서 안보내져서 substring으로 잘라서 보냄
        String resultImage = imageUri.substring(8);
        uploadFilePathName = resultImage;
        String FilePath = uploadFilePathName.substring(0, 27);
        String FileName = uploadFilePathName.substring(27);
        uploadFilePath = FilePath;
        uploadFileName = FileName;
        Log.e("tag", "FilePath " + FilePath);
        Log.e("tag", "FileName " + FileName);
        Log.e("tag", "crop된 앨범경로 " + savingUri);
        Crop.of(photoUri, savingUri).asSquare().start(this);
    }

    /**
     * 폴더 및 파일 만들기
     */
    private File createImageFile() throws IOException {

        Log.e(TAG, "createImageFile<-여기들어가니)");
        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "list_" + timeStamp + "_";

        // 이미지가 저장될 파일 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/company/");

        Log.e(TAG, "createImageFile_storageDir : " + storageDir);
        if (!storageDir.exists()) storageDir.mkdirs();
        Log.e(TAG, "createImageFile_image(여기들어가니)");
        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());
        Log.e(TAG, "createImageFile : " + image.getAbsolutePath());
        Log.e(TAG, "createImageFile_image : " + image);

        return image;
    }

    private void setImage() {



        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

//        image_freelancer_writing_picture.setImageBitmap(originalBm);
        //Glide을 이용해서 이미지뷰에 url에 있는 이미지를 세팅해줌
        Glide.with(this).load(photoUri).into(image_freelancer_writing_picture);
    }


//    /**
//     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
//     */
//    private void setImage() {
//
//        // newWidth 숫자를 작게하니 화질이 꺠짐
//        Log.e("tag", "tempFile setImage" + tempFile);
//        ImageResizeUtils.resizeFile(tempFile, tempFile, 1080, 1080, isCamera);
//        Log.e("tag", "tempFile setImage" + tempFile);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
//        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());
//
//        // Display a jpg image from the given url
//        //Glide을 이용해서 이미지뷰에 url에 있는 이미지를 세팅해줌
////        Glide.with(this).load(tempFile).into(image_freelancer_writing_picture);
//
////        imageView.setImageBitmap(originalBm);
//        image_freelancer_writing_picture.setImageBitmap(originalBm);
//        /**
//         *  tempFile 사용 후 null 처리를 해줘야 합니다.
//         *  (resultCode != RESULT_OK) 일 때 (tempFile != null)이면 해당 파일을 삭제하기 때문에
//         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
//         */
//        tempFile = null;
//
//    }

    /**
     * 권한 설정
     */
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    private void initMaintainUser() {
        // 로그인 유지 shard 저장
        shared_maintainLogin = getSharedPreferences("maintain_login", Activity.MODE_PRIVATE);
    }

    // 로그인 정보불러오기
    private void loadMaintainUser() {
        initMaintainUser();
        num = shared_maintainLogin.getString("num", null);
        id = shared_maintainLogin.getString("id", null);
        name = shared_maintainLogin.getString("name", null);
        phonenum = shared_maintainLogin.getString("phonenum", null);
        title = shared_maintainLogin.getString("title", null);
        skill = shared_maintainLogin.getString("skill", null);
        introduction = shared_maintainLogin.getString("introduction", null);
        location = shared_maintainLogin.getString("location", null);
        images = shared_maintainLogin.getString("images", null);


    }


}
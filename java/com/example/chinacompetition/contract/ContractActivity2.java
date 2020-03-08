package com.example.chinacompetition.contract;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chinacompetition.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ContractActivity2 extends AppCompatActivity {

    /******** contract *****/

    LinearLayout linear_contract_image;
    LinearLayout linear_contract_document;
    File file;

    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            + "/test"; //파일 위치

    String timeStamp;
    File temp;
    TextView pathView01;
    TextView pathView03;
    TextView pathView02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        linear_contract_image = (LinearLayout) findViewById(R.id.linear_contract_image);
        // move pdf,hwf file
        linear_contract_document = (LinearLayout) findViewById(R.id.linear_contract_document);
//        file = new File(path);

//        pathView01 = (TextView) findViewById(R.id.pathview01);
//        pathView02 = (TextView) findViewById(R.id.pathview02);
//        pathView03 = (TextView) findViewById(R.id.pathview03);
        linear_contract_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                load();

              
//                File storeDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyDocApp");
//                if (!storeDir.exists()) {
//                    if (!storeDir.mkdirs()) {
//                        Log.d("MyDocApp", "failed to create directory");
//                        return;
//                    }
//                }
//                timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                temp = new File(storeDir.getPath() + File.separator + "test_"+ timeStamp + ".docx");
//                if(temp == null){
//                    Log.d("TAG", "Error at creating .docx file, check storage permissions :");
//                    return;
//                }
//
//                pathView01.setText(temp.getAbsolutePath());
//                pathView02.setText(temp.getPath());
//                try {
//                    pathView03.setText(temp.getCanonicalPath());
//                } catch (IOException e) {
//                    Log.d("TAG", "Error at creating .docx file, check storage permissions :");
//                    return;
//                }


//                File storeDir = new File(Environment.getExternalStorageDirectory(), "MyDocApp");
//
//                Log.e("TAG","Environment.getExternalStorageDirectory()"+Environment.getExternalStorageDirectory());
//                if (!storeDir.exists()) {
//                    if (!storeDir.mkdirs()) {
//                        Log.d("MyDocApp", "failed to create directory");
//                        Log.e("MyDocApp", "failed to create directory");
//                        return;
//                    }
//                }
//
//                file = new File(storeDir.getPath());
//                Intent intent = new Intent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                if (file.getName().endsWith(".pdf")){
//                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//                }else if (file.getName().endsWith(".hwp")){
//                    intent.setDataAndType(Uri.fromFile(file), "application/hwp");
//                }else if (file.getName().endsWith(".docx")) {
//                    intent.setDataAndType(Uri.fromFile(file), "application/msword");
//                }
//                try{
//                    startActivity(intent);
//                }catch(ActivityNotFoundException e){
////                   util.showLongToast("해당파일을 실항할 수 있는 어플리케이션이 없습니다.\n파일을 열 수 없습니다.");
//                    e.printStackTrace();
//                }

            }
        });
    }


    public static void save(String result) {
        String sdPath;  //SD 카드의 경로
        String externalState = Environment.getExternalStorageState();
        if (externalState.equals(Environment.MEDIA_MOUNTED)) {
            //외부 저장 장치가 마운트 되어서 읽어올 준비가 되었을 때
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            //마운트 되지 않았을 때
            sdPath = Environment.MEDIA_UNMOUNTED;
        }
        File file = new File(sdPath + "/myDir");
        if (!file.isDirectory())
            file.mkdir(); //디렉토리 만들기
        File file1 = new File(sdPath + "/mydir/text.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file1);
            fos.write(result.getBytes());
            fos.close();

        } catch (Exception e) {
            Log.i("파일 저장 실패:", e.getMessage());
        }
    }

    //데이터 로드 메소드
    public static String load() {
        String sdPath;  //SD 카드의 경로
        String externalState = Environment.getExternalStorageState();
        if (externalState.equals(Environment.MEDIA_MOUNTED)) {
            //외부 저장 장치가 마운트 되어서 읽어올 준비가 되었을 때
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            //마운트 되지 않았을 때
            sdPath = Environment.MEDIA_UNMOUNTED;
        }
        String result = "";
        try {
            String dir = sdPath + "/myDir/text.txt";
            //파일에서 읽어오기 위한 스트림 객체
            File file = new File(dir);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            result = new String(buffer);
        } catch (Exception e) {
            Log.i("불러오기 실패", e.getMessage());
        }
        return result;
    }


}

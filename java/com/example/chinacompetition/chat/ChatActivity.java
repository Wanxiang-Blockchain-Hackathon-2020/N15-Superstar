package com.example.chinacompetition.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinacompetition.R;
import com.example.chinacompetition.bottom_navigation.MessageActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import androidx.appcompat.app.AlertDialog;


public class ChatActivity extends AppCompatActivity {

    TextView text1;
    Button button, fileBtn;
    BufferedReader bufferR;
    EditText insertView;
    BufferedWriter bufferW;

    //계약서 파일의 리스트를 보여줄 리스트뷰
    ListView listView;
    Boolean btnClick = true;

    //파일관련 변수
    ArrayList<Data> datas = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String fileName,filePath;

    //채팅화면 관련 변수
    ArrayList<ChatItem> chatItems = new ArrayList<ChatItem>();

    private RecyclerView recyclerView;
    private ChatDataAdapter chatDataAdapter;


    String sendMsg;
    String msg;

    //유저정보
    String id, thumbnail = null, content;

    //방정보
    String room;


    //ClientThread thread;


    //스레드 파라미터

    String nickName;
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

    boolean isConnected = true;


    // back
    ImageView image_chat_back ;
    ImageView image_chat2;
    ImageView image_chat2_file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        //권한 체크
        checkFunction();

        image_chat_back = (ImageView) findViewById(R.id.image_chat_back);
        // go to back
        image_chat_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        });
        //메시지 입력이 종료되면 키보드 패드를 사라지게 하기 위한 객체
        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //인텐트를 통해서 방번호를 전달 받는다.


        Intent intent = getIntent(); /*데이터 수신*/

        room = intent.getExtras().getString("room");
        room = "1";
/*String형*
        room = "1";


/*
        text1 = findViewById(R.id.text1);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);*/
        //listView = findViewById(R.id.Lv);
        button = (Button) findViewById(R.id.button);
        image_chat2 = (ImageView)findViewById(R.id.image_chat2);

        fileBtn= findViewById(R.id.fileBtn);
        double ran = Math.random() * 100;
        int num = (int) ran;


        id = "user" + num;

        //채팅을 위한 소켓 스레드 실행
      /*  thread = new ClientThread();
        thread.start();*/


       // adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);

        //listView.setAdapter(adapter);

  //      writeFile();

//        listFile();

        //채팅내용을 보여줄 리사이클러뷰 위젯을 연결
        recyclerView = findViewById(R.id.Rc);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //채팅내용을 어댑터,리사이클러뷰에 연결한다
        /*
        chatDataAdapter = new ChatDataAdapter(ChatActivity.this, chatItems);
        recyclerView.setAdapter(chatDataAdapter);
        chatDataAdapter.notifyDataSetChanged();*/


        //키보드와 editText 연결
        insertView = (EditText) findViewById(R.id.insertView);
        //  Button mButton = (Button)findViewById(R.id.button1);

        //insertView.setOnEditorActionListener(this); //mEditText와 onEditorActionListener를 연결



        //todo 채팅소켓
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {

                    String host = "34.64.144.139";
//                    15.165.57.108
                    int port = 5000;
                    //서버와 연결하는 소켓 생성..
                    socket = new Socket(host, port);
                    bufferR = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    bufferW = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

//id와 방번호를 보낸다.

                    bufferW.write(room + "," + id + "\n");
                    // bufferW.write(id + "\n");
                    bufferW.flush();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //서버와 접속이 끊길 때까지 무한반복하면서 서버의 메세지 수신
                while (isConnected) {
                    try {

                        sendMsg = bufferR.readLine();

                        //runOnUiThread()는 별도의 Thread가 main Thread에게 UI 작업을 요청하는 메소드이다
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

                                //서버로부터 전달받은 메시지에서 상대방의 아이디와 상대방이 입력한 메시지를 구분한다.
                                if (!sendMsg.equals("start")) {
                                    String id = sendMsg.substring(0, sendMsg.lastIndexOf(":"));
                                    sendMsg = sendMsg.substring(sendMsg.lastIndexOf(":") + 1);


                                    chatItems.add(new ChatItem(id, sendMsg));
                                    //chatItems.add(new ChatItem(id, thumbnail, sendMsg));
                                }
                                chatDataAdapter = new ChatDataAdapter(ChatActivity.this, chatItems);
                                recyclerView.setAdapter(chatDataAdapter);
                                chatDataAdapter.notifyDataSetChanged();

                            }
                        });
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }//while
            }//run method...
        }).start();//Thread 실행..

        image_chat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = insertView.getText().toString();

                SendMessage();

                  /*  bufferW.write(msg + "\n");

                    bufferW.flush();*/

                //thread.sendMsg();

//                chatItems.add(new ChatItem(id, thumbnail, msg));
                chatItems.add(new ChatItem("me", msg));

                chatDataAdapter = new ChatDataAdapter(ChatActivity.this, chatItems);
                recyclerView.setAdapter(chatDataAdapter);
                chatDataAdapter.notifyDataSetChanged();

                Log.i("ClientThread", "서버로 보냄.");
                insertView.setText("");
                imm.hideSoftInputFromWindow(insertView.getWindowToken(), 0);



            }
        });


//        button.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                msg = insertView.getText().toString();
//
//                SendMessage();
//
//                  /*  bufferW.write(msg + "\n");
//
//                    bufferW.flush();*/
//
//                //thread.sendMsg();
//
////                chatItems.add(new ChatItem(id, thumbnail, msg));
//                chatItems.add(new ChatItem("me", msg));
//
//                chatDataAdapter = new ChatDataAdapter(ChatActivity.this, chatItems);
//                recyclerView.setAdapter(chatDataAdapter);
//                chatDataAdapter.notifyDataSetChanged();
//
//                Log.i("ClientThread", "서버로 보냄.");
//                insertView.setText("");
//                imm.hideSoftInputFromWindow(insertView.getWindowToken(), 0);
//
//
//            }
//        });

/*

        //파일추가버튼
        fileBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnClick){
                    listView.setVisibility(View.VISIBLE);
                    btnClick=false;

                }else {
                    listView.setVisibility(View.INVISIBLE);
                    btnClick=true;

                }
            }
        });
*/




     /*   // TODO: 2020-03-06 채팅창에 업로드할 파일 클릭이벤트
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alg = new AlertDialog.Builder(ChatActivity.this);

                final int position = i;
                alg.setTitle("파일전송")
                        .setMessage("전송하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                             *//*   deletefile(titles.get(position));
                                titles.remove(position);
                                datas.remove(position);
                                adapter.notifyDataSetChanged();*//*

                                fileName =datas.get(position).getTitle();
                                filePath = datas.get(position).getPath();
                                //소켓으로 파일 전송한다.
                                SendFile();


                                //채팅창에 반영
                                chatItems.add(new ChatItem("me", fileName+"파일전송"));

                                chatDataAdapter = new ChatDataAdapter(ChatActivity.this, chatItems);
                                recyclerView.setAdapter(chatDataAdapter);
                                chatDataAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("취소",null)
                        .show();
                return true;
            }
        });*/

    }


    // public void SendMessage(View view) {
    public void SendMessage() {

        if (bufferW == null) return;   //서버와 연결되어 있지 않다면 전송불가..

        //네트워크 작업이므로 Thread 생성
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //서버로 보낼 메세지 EditText로 부터 얻어오기
                String msg = insertView.getText().toString();
                try {
                   /* runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String msg= insertView.getText().toString();
                            // TODO Auto-generated method stub
                            text_msg.setText("[SEND]" +msg);
                        }
                    });*/

                    bufferW.write(msg + "\n");
                    bufferW.flush();


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }//run method..

        }).start(); //Thread 실행..
    }




    public void checkFunction(){
        int permissioninfo = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissioninfo == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"SDCard 쓰기 권한 있음",Toast.LENGTH_SHORT).show();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this, "권한 설명",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String str = null;
        if(requestCode == 100){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                str = "SD Card 쓰기권한 승인";
            else str = "SD Card 쓰기권한 거부";
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        }
    }


    // TODO: 2020-03-05 파일 저장할 폴더 경로 설정
    public String getExternalPath(){
        String sdPath ="";
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)){
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        }else{
            sdPath  = getFilesDir() +"";

        }
        return sdPath;
    }

    // TODO: 2020-03-05 파일목록을 보여주는 메소
    public void listFile() {
        String path = getExternalPath();

        File file = new File(path + "mydiary");
        if (!file.exists()) file.mkdir();
        else{
            File[] files = new File(path + "mydiary").listFiles();

            if(files != null){
                for (File f : files){


                    titles.add(f.getName());
                    String body = loadFile(f.getName());
                    datas.add(new Data(f.getName(), body,f.getPath()));
                }

            }
            Collections.sort(titles, titleAsc);
            Collections.sort(datas, dataAsc);

            adapter.notifyDataSetChanged();
        }


    }

    // TODO: 2020-03-05 경로에 있는 파일들을 읽어서  로드
    public String loadFile(String title){
        try{
            String path = getExternalPath();
            BufferedReader br = new BufferedReader(new FileReader(path+"mydiary/"+title));
            String readStr = "";
            String str = null;
            while(((str = br.readLine()) != null)){
                readStr += str +"\n";
            }
            br.close();

            return readStr;

        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this, "File not Found", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    // TODO: 2020-03-05 파일 쓰기

    // public void writeFile(String title, String body){
    public void writeFile(){

        try{
            String path = getExternalPath();

            String filename = "20.2.6 合同书";
            String filename2 = "20.2.11 合同书";
            String filename3 = "20.3.5 合同书";
            String body = "";

            //파일만들기
            BufferedWriter bw = new BufferedWriter(new FileWriter(path + "mydiary/" + filename, false));
            bw.write(body);
            bw.close();
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(path + "mydiary/" + filename2, false));
            bw2.write(body);
            bw2.close();

            BufferedWriter bw3 = new BufferedWriter(new FileWriter(path + "mydiary/" + filename3, false));
            bw3.write(body);
            bw3.close();

            //tvCount.setText("등록된 메모 개수: " + titles.size());
            //Toast.makeText(this,"저장완료", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: 2020-03-06 소켓을 이용해서 파일을 전송하는 부분
    public void deletefile(String title){
        String path = getExternalPath();

        File file = new File(path + "mydiary");

        File[] files = new File(path + "mydiary").listFiles();

        for(File f : files){
            if(title.equals(f.getName())){
                //이 부분에서 파일을 소켓으로 전송한다.
                //f.delete();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isConnected = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        isConnected = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //파일리스트를 정렬해주는 부분
    Comparator<Data> dataAsc = new Comparator<Data>() {
        @Override
        public int compare(Data data, Data t1) {
            return data.getTitle().compareToIgnoreCase(t1.getTitle());
        }
    };

    Comparator<String> titleAsc = new Comparator<String>() {
        @Override
        public int compare(String s, String t1) {
            return s.compareToIgnoreCase(t1);
        }
    };






}
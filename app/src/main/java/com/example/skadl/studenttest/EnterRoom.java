package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by skadl on 2018-03-08.
 */

public class EnterRoom extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private EditText nickname, pinNum;
    private ImageButton char1, char2, char3, char4, char5;
    private Socket mSocket;
    private String title, stdNum, character;

    public EnterRoom() {
        title = "로그인";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_room);

        Intent getNum = getIntent();
        stdNum = getNum.getStringExtra("Student_num");

        setTitle(stdNum);

        button = (Button) findViewById(R.id.find);
        button.setOnClickListener(this);

        nickname = (EditText) findViewById(R.id.nickname);
        pinNum = (EditText) findViewById(R.id.pinnum);

        char1 = (ImageButton)findViewById(R.id.char1);
        char1.setOnClickListener(this);

        char2 = (ImageButton)findViewById(R.id.char2);
        char2.setOnClickListener(this);

        char3 = (ImageButton)findViewById(R.id.char3);
        char3.setOnClickListener(this);

        char4 = (ImageButton)findViewById(R.id.char4);
        char4.setOnClickListener(this);

        char5 = (ImageButton)findViewById(R.id.char5);
        char5.setOnClickListener(this);

        try {
            mSocket = IO.socket("http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890");
            mSocket.connect();


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {

        if (view.getId() == R.id.find) {

            //  공백 등 예외처리

            if(character == null){
                return;
            }

            String nick = nickname.getText().toString();
            String pin = pinNum.getText().toString();

            mSocket.emit("join", pin);
            mSocket.emit("user_in", pin, nick, stdNum, character);

            Intent intent = new Intent(EnterRoom.this, Blindrace.class);

            intent.putExtra("Student_num", stdNum);
            intent.putExtra("Nick_name", nick);
            intent.putExtra("Room_num", pin);
            startActivity(intent);

        }else if(view.getId() == R.id.char1){

            character = "1";

        }else if(view.getId() == R.id.char2){

            character = "2";

        }else if(view.getId() == R.id.char3){

            character = "3";

        }else if(view.getId() == R.id.char4){

            character = "4";

        }else if(view.getId() == R.id.char5){

            character = "5";

        }
    }
    //  방 번호 체크
    //  있을 경우 방 이름, 버튼 -> 입장하기

    //  없을 경우 개설된 방이 없습니다. , 버튼 -> 돌아가기
}
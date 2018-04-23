package com.example.skadl.studenttest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
    private ImageButton char1, char2, char3, char4, char5, char6, char7, char8, char9;
    private Socket mSocket;
    private String title, stdNum, character;
    private boolean check;

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

        char1 = (ImageButton) findViewById(R.id.char1);
        char1.setOnClickListener(this);

        char2 = (ImageButton) findViewById(R.id.char2);
        char2.setOnClickListener(this);

        char3 = (ImageButton) findViewById(R.id.char3);
        char3.setOnClickListener(this);

        char4 = (ImageButton) findViewById(R.id.char4);
        char4.setOnClickListener(this);

        char5 = (ImageButton) findViewById(R.id.char5);
        char5.setOnClickListener(this);

        char6 = (ImageButton) findViewById(R.id.char6);
        char6.setOnClickListener(this);

        char7 = (ImageButton) findViewById(R.id.char7);
        char7.setOnClickListener(this);

        char8 = (ImageButton) findViewById(R.id.char8);
        char8.setOnClickListener(this);

        char9 = (ImageButton) findViewById(R.id.char9);
        char9.setOnClickListener(this);

        char1.setBackgroundColor(Color.GRAY);
        char2.setBackgroundColor(Color.GRAY);
        char3.setBackgroundColor(Color.GRAY);
        char4.setBackgroundColor(Color.GRAY);
        char5.setBackgroundColor(Color.GRAY);
        char6.setBackgroundColor(Color.GRAY);
        char7.setBackgroundColor(Color.GRAY);
        char8.setBackgroundColor(Color.GRAY);
        char9.setBackgroundColor(Color.GRAY);

        try {
            mSocket = IO.socket("http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890");

            mSocket.connect();


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener errorMsg = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            EnterRoom.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), arg0[0].toString(), Toast.LENGTH_LONG).show();
                    check = false;
                }
            });
        }
    };

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.find:

                check = true;

                if (character == null) {
                    return;
                }


                String nick = nickname.getText().toString();
                String pin = pinNum.getText().toString();

                mSocket.emit("join", pin);
                mSocket.emit("user_in", pin, nick, stdNum, character);
                mSocket.on("err_msg", errorMsg);

                if(check == false){
                    return;
                }

                mSocket.emit("answer", "0", stdNum, nick);

                Intent intent = new Intent(EnterRoom.this, WaitingRoom.class);

                intent.putExtra("Student_num", stdNum);
                intent.putExtra("Nick_name", nick);
                intent.putExtra("Room_num", pin);
                intent.putExtra("char", character);
                startActivity(intent);
                break;

            case R.id.char1:
                char1.setBackgroundColor(Color.WHITE);
                char2.setBackgroundColor(Color.GRAY);
                char3.setBackgroundColor(Color.GRAY);
                char4.setBackgroundColor(Color.GRAY);
                char5.setBackgroundColor(Color.GRAY);
                char6.setBackgroundColor(Color.GRAY);
                char7.setBackgroundColor(Color.GRAY);
                char8.setBackgroundColor(Color.GRAY);
                char9.setBackgroundColor(Color.GRAY);
                character = "1";
                break;

            case R.id.char2:
                char2.setBackgroundColor(Color.WHITE);
                char1.setBackgroundColor(Color.GRAY);
                char3.setBackgroundColor(Color.GRAY);
                char4.setBackgroundColor(Color.GRAY);
                char5.setBackgroundColor(Color.GRAY);
                char6.setBackgroundColor(Color.GRAY);
                char7.setBackgroundColor(Color.GRAY);
                char8.setBackgroundColor(Color.GRAY);
                char9.setBackgroundColor(Color.GRAY);
                character = "2";
                break;

            case R.id.char3:
                char3.setBackgroundColor(Color.WHITE);
                char1.setBackgroundColor(Color.GRAY);
                char2.setBackgroundColor(Color.GRAY);
                char4.setBackgroundColor(Color.GRAY);
                char5.setBackgroundColor(Color.GRAY);
                char6.setBackgroundColor(Color.GRAY);
                char7.setBackgroundColor(Color.GRAY);
                char8.setBackgroundColor(Color.GRAY);
                char9.setBackgroundColor(Color.GRAY);
                character = "3";
                break;

            case R.id.char4:
                char4.setBackgroundColor(Color.WHITE);
                char1.setBackgroundColor(Color.GRAY);
                char2.setBackgroundColor(Color.GRAY);
                char3.setBackgroundColor(Color.GRAY);
                char5.setBackgroundColor(Color.GRAY);
                char6.setBackgroundColor(Color.GRAY);
                char7.setBackgroundColor(Color.GRAY);
                char8.setBackgroundColor(Color.GRAY);
                char9.setBackgroundColor(Color.GRAY);
                character = "4";
                break;

            case R.id.char5:
                char5.setBackgroundColor(Color.WHITE);
                char1.setBackgroundColor(Color.GRAY);
                char2.setBackgroundColor(Color.GRAY);
                char3.setBackgroundColor(Color.GRAY);
                char4.setBackgroundColor(Color.GRAY);
                char6.setBackgroundColor(Color.GRAY);
                char7.setBackgroundColor(Color.GRAY);
                char8.setBackgroundColor(Color.GRAY);
                char9.setBackgroundColor(Color.GRAY);
                character = "5";
                break;

            case R.id.char6:
                char6.setBackgroundColor(Color.WHITE);
                char1.setBackgroundColor(Color.GRAY);
                char2.setBackgroundColor(Color.GRAY);
                char3.setBackgroundColor(Color.GRAY);
                char4.setBackgroundColor(Color.GRAY);
                char5.setBackgroundColor(Color.GRAY);
                char7.setBackgroundColor(Color.GRAY);
                char8.setBackgroundColor(Color.GRAY);
                char9.setBackgroundColor(Color.GRAY);
                character = "6";
                break;

            case R.id.char7:
                char7.setBackgroundColor(Color.WHITE);
                char1.setBackgroundColor(Color.GRAY);
                char2.setBackgroundColor(Color.GRAY);
                char3.setBackgroundColor(Color.GRAY);
                char4.setBackgroundColor(Color.GRAY);
                char5.setBackgroundColor(Color.GRAY);
                char6.setBackgroundColor(Color.GRAY);
                char8.setBackgroundColor(Color.GRAY);
                char9.setBackgroundColor(Color.GRAY);
                character = "7";
                break;

            case R.id.char8:
                char8.setBackgroundColor(Color.WHITE);
                char1.setBackgroundColor(Color.GRAY);
                char2.setBackgroundColor(Color.GRAY);
                char3.setBackgroundColor(Color.GRAY);
                char4.setBackgroundColor(Color.GRAY);
                char5.setBackgroundColor(Color.GRAY);
                char6.setBackgroundColor(Color.GRAY);
                char7.setBackgroundColor(Color.GRAY);
                char9.setBackgroundColor(Color.GRAY);
                character = "8";
                break;

            case R.id.char9:
                char9.setBackgroundColor(Color.WHITE);
                char1.setBackgroundColor(Color.GRAY);
                char2.setBackgroundColor(Color.GRAY);
                char3.setBackgroundColor(Color.GRAY);
                char4.setBackgroundColor(Color.GRAY);
                char5.setBackgroundColor(Color.GRAY);
                char6.setBackgroundColor(Color.GRAY);
                char7.setBackgroundColor(Color.GRAY);
                char8.setBackgroundColor(Color.GRAY);
                character = "9";
                break;
        }
    }
    //  방 번호 체크
    //  있을 경우 방 이름, 버튼 -> 입장하기

    //  없을 경우 개설된 방이 없습니다. , 버튼 -> 돌아가기
}
package com.example.skadl.studenttest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private Button      button;
    private EditText    nickname, pinNum;
    private ImageButton chara[] = new ImageButton[9];
    private Socket      mSocket;
    private String      title, stdNum, character;
    private boolean     check;
    private int id[] = new int[9];

    public EnterRoom() {
        title = "로그인";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_room);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent getNum = getIntent();
        stdNum = getNum.getStringExtra("Student_num");

        setTitle(stdNum);

        button = (Button) findViewById(R.id.find);
        button.setOnClickListener(this);

        nickname = (EditText) findViewById(R.id.nickname);
        pinNum = (EditText) findViewById(R.id.pinnum);

        for (int i = 0; i < chara.length; i++) {
            String resId = "char" + String.valueOf(i + 1);
            id[i] = getResources().getIdentifier(resId, "id", getPackageName());

            chara[i] = (ImageButton) findViewById(id[i]);
            chara[i].setOnClickListener(this);
            chara[i].setBackgroundColor(Color.BLUE);
        }

        try {
            mSocket = IO.socket("http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890");

            mSocket.connect();
            mSocket.on("android_enter_room", enter_check);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener enter_check = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            EnterRoom.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String enter_checking = arg0[1].toString();

                    if( enter_checking.equals("false")){
                        //닉네임이나 캐릭터에 중복된 정보가 입력되었을 때
                        if( stdNum.equals(arg0[2].toString()) )
                            Toast.makeText(getApplicationContext(), "중복된 캐릭터 혹은 닉네임입니다.", Toast.LENGTH_LONG).show();

                    }else{
                        // 닉네임과 캐릭터가 정상적으로 선택되었을 떄

                        //선택된 캐릭터 비활성화
                        int selected_chara_num  = Integer.parseInt(enter_checking)-1;
                        chara[selected_chara_num].setEnabled(false);


                        //올바른정보로 신청시 입장요청유저는 방입장
                       if( stdNum.equals(arg0[2].toString()) ) {
                           String nick = nickname.getText().toString();
                           String pin = pinNum.getText().toString();
                           Toast.makeText(getApplicationContext(), stdNum, Toast.LENGTH_LONG).show();
                           mSocket.emit("answer", "0", stdNum, nick);
                           Intent intent = new Intent(EnterRoom.this, WaitingRoom.class);
                           intent.putExtra("Student_num", stdNum);
                           intent.putExtra("Nick_name", nick);
                           intent.putExtra("Room_num", pin);
                           intent.putExtra("char", character);
                           startActivity(intent);
                       }
                    }
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

//                Toast.makeText(getApplicationContext(), stdNum, Toast.LENGTH_LONG).show();
//
//
//                mSocket.emit("answer", "0", stdNum, nick);
//
//
//                Intent intent = new Intent(EnterRoom.this, WaitingRoom.class);
//
//                intent.putExtra("Student_num", stdNum);
//                intent.putExtra("Nick_name", nick);
//                intent.putExtra("Room_num", pin);
//                intent.putExtra("char", character);
//                startActivity(intent);
//                break;

            case R.id.char1:

                for (int i = 0; i < chara.length; i++) {
                    chara[i].setBackgroundColor(Color.BLUE);
                }
                chara[0].setBackgroundColor(Color.RED);

                character = "1";
                break;

            case R.id.char2:

                for (int i = 0; i < chara.length; i++) {
                    chara[i].setBackgroundColor(Color.BLUE);
                }
                chara[1].setBackgroundColor(Color.RED);

                character = "2";
                break;

            case R.id.char3:

                for (int i = 0; i < chara.length; i++) {
                    chara[i].setBackgroundColor(Color.BLUE);
                }
                chara[2].setBackgroundColor(Color.RED);

                character = "3";
                break;

            case R.id.char4:

                for (int i = 0; i < chara.length; i++) {
                    chara[i].setBackgroundColor(Color.BLUE);
                }
                chara[3].setBackgroundColor(Color.RED);

                character = "4";
                break;

            case R.id.char5:

                for (int i = 0; i < chara.length; i++) {
                    chara[i].setBackgroundColor(Color.BLUE);
                }
                chara[4].setBackgroundColor(Color.RED);

                character = "5";
                break;

            case R.id.char6:

                for (int i = 0; i < chara.length; i++) {
                    chara[i].setBackgroundColor(Color.BLUE);
                }
                chara[5].setBackgroundColor(Color.RED);

                character = "6";
                break;

            case R.id.char7:

                for (int i = 0; i < chara.length; i++) {
                    chara[i].setBackgroundColor(Color.BLUE);
                }
                chara[6].setBackgroundColor(Color.RED);

                character = "7";
                break;

            case R.id.char8:

                for (int i = 0; i < chara.length; i++) {
                    chara[i].setBackgroundColor(Color.BLUE);
                }
                chara[7].setBackgroundColor(Color.RED);

                character = "8";
                break;

            case R.id.char9:

                for (int i = 0; i < chara.length; i++) {
                    chara[i].setBackgroundColor(Color.BLUE);
                }
                chara[8].setBackgroundColor(Color.RED);

                character = "9";
                break;
        }
    }
    //  방 번호 체크
    //  있을 경우 방 이름, 버튼 -> 입장하기

    //  없을 경우 개설된 방이 없습니다. , 버튼 -> 돌아가기
}
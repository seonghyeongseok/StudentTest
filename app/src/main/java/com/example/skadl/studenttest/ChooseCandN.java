package com.example.skadl.studenttest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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

public class ChooseCandN extends AppCompatActivity implements View.OnClickListener {

    public static final String ServerIP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890";

    private Socket      mSocket;

    private Button      button;
    private EditText    nickname;
    private ImageButton chara[] = new ImageButton[9];

    private String sessionNum, character, roomNum, stdName;
    private int id[] = new int[9];


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_char_nick);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent getNum = getIntent();

        sessionNum  = getNum.getStringExtra("session_num");
        stdName     = getNum.getStringExtra("student_name");
        roomNum     = getNum.getStringExtra("room_num");

        button = (Button) findViewById(R.id.find);
        button.setOnClickListener(this);

        nickname = (EditText) findViewById(R.id.nickname);

        for (int i = 0; i < chara.length; i++) {

            String resId = "char" + String.valueOf(i + 1);
            id[i] = getResources().getIdentifier(resId, "id", getPackageName());

            chara[i] = (ImageButton) findViewById(id[i]);
            chara[i].setOnClickListener(this);
            chara[i].setBackgroundColor(Color.BLUE);

        }

        try {

            mSocket = IO.socket(ServerIP);
            mSocket.on("android_enter_room", enter_check);
            mSocket.connect();

        } catch (URISyntaxException e) {

            e.printStackTrace();

        }
    }

    private Emitter.Listener enter_check = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            ChooseCandN.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String enter_checking = arg0[1].toString();

                    if (enter_checking.equals("false")) {

                        //닉네임이나 캐릭터에 중복된 정보가 입력되었을 때
                        if (sessionNum.equals(arg0[2].toString()))

                            Toast.makeText(getApplicationContext(), "중복된 캐릭터 혹은 닉네임입니다.", Toast.LENGTH_LONG).show();

                    } else {
                        // 닉네임과 캐릭터가 정상적으로 선택되었을 떄

                        //선택된 캐릭터 비활성화
                        int selected_chara_num = Integer.parseInt(enter_checking) - 1;
                        chara[selected_chara_num].setEnabled(false);

                        //올바른정보로 신청시 입장요청유저는 방입장
                        if (sessionNum.equals(arg0[2].toString())) {

                            String nick = nickname.getText().toString();

                            Intent intent = new Intent(ChooseCandN.this, Blindrace.class);
                            intent.putExtra("session_num", sessionNum);
                            intent.putExtra("student_name", stdName);
                            intent.putExtra("Nick_name", nick);
                            intent.putExtra("room_num", roomNum);
                            intent.putExtra("char", character);

                            startActivity(intent);
                        }
                    }
                }
            });
        }
    };

    public void onClick(View view) {

        for(int i = 0 ; i < chara.length ; i++){

            if(id[i] == view.getId()){
                chara[i].setBackgroundColor(Color.RED);
                character = String.valueOf(i+1);
            }else if(R.id.find == view.getId()){

                //button.setText(character + nickname.getText().toString());

                if (character == null) {

                    Toast.makeText(getApplicationContext(), "캐릭터 쵸이스 쿠다사이", Toast.LENGTH_SHORT).show();
                    return;

                }

                String nick = nickname.getText().toString();

                mSocket.emit("join", roomNum);
                mSocket.emit("user_in", roomNum, nick, sessionNum, character);

                break;
            }else{
                chara[i].setBackgroundColor(Color.BLUE);
            }

        }
    }
}
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
import java.util.Arrays;

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
    private String character_info;
    private String[] choosedcharacters = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_char_nick);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

            Intent getNum = getIntent();

            sessionNum  = getNum.getStringExtra("session_num");
            roomNum     = getNum.getStringExtra("room_num");
            character_info = getNum.getStringExtra("character_Info");



            if(character_info.length() > 2)
                choosedcharacters = character_info.split(",");
            else if(character_info.length() == 1 ) {
                choosedcharacters = new String[1];
            choosedcharacters[0] = character_info;
        }


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
            chara[i].setBackgroundColor(Color.parseColor("#00000000"));

        }

        if(choosedcharacters != null) {
            for (int i = 0; i < choosedcharacters.length; i++) {
                int choosedNumber;
                choosedNumber = Integer.parseInt(choosedcharacters[i]);

                chara[choosedNumber - 1].setEnabled(false);
                chara[choosedNumber - 1].setBackgroundResource(R.drawable.selected);

            }
        }

        try {

            mSocket = IO.socket(ServerIP);
            mSocket.on("android_enter_room", enter_check);
            mSocket.on("enable_character",enable_character);
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
                            finish();
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener enable_character = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            ChooseCandN.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String enable_num = arg0[0].toString();
                        int selected_chara_num = Integer.parseInt(enable_num) - 1;
                        chara[selected_chara_num].setEnabled(true);

                }
            });
        }
    };

    public void onClick(View view) {

        for(int i = 0 ; i < chara.length ; i++){

            if(id[i] == view.getId()){
                chara[i].setBackgroundColor(Color.parseColor("#64FFDA"));
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
                chara[i].setBackgroundColor(Color.parseColor("#00000000"));
            }
        }
    }
}
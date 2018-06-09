package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import java.net.URISyntaxException;

/**
 * Created by skadl on 2018-04-30.
 */

public class InputRoomNum extends AppCompatActivity implements View.OnClickListener {

    private static final String ServerIP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890";

    private String      sessionNum, roomNum = null, stdName;
    private Button      button;
    private EditText    pinNum;
    private Socket      mSocket;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_room);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent getNum = getIntent();
        sessionNum = getNum.getStringExtra("session_num");
        stdName = getNum.getStringExtra("student_name");

        Log.e("로긴", sessionNum);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        pinNum = (EditText) findViewById(R.id.pin_num);

        try {

            mSocket = IO.socket(ServerIP);
            mSocket.on("android_join_result", joinCheck);
            mSocket.connect();

        } catch (URISyntaxException e) {

            e.printStackTrace();

        }
    }

    private Emitter.Listener joinCheck = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            InputRoomNum.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Intent intent = null;

                    boolean check = false;
                    String checkSessionNum = null;
                    String category = null;

                    check = Boolean.valueOf(arg0[0].toString()).booleanValue();
                    checkSessionNum = arg0[1].toString();
                    category = arg0[2].toString();

                    Log.d("1", String.valueOf(check));
                    Log.d("2", arg0[1].toString());
                    Log.d("3", sessionNum);

                    if(check && checkSessionNum.equals(sessionNum)){

                        mSocket.emit("join", roomNum);
                        //  조인할때 필요하단다 - ジュンヒから。。

                        switch (category){

                            case "race":

                                intent = new Intent(InputRoomNum.this, ChooseCandN.class);
                                break;

                            case "popQuiz":

                                intent = new Intent(InputRoomNum.this, PopQuiz.class);
                                break;

                            default:

                                break;

                        }

                        intent.putExtra("session_num", sessionNum);
                        intent.putExtra("student_name", stdName);
                        intent.putExtra("room_num", roomNum);
                        startActivity(intent);

                    }
                    else if(checkSessionNum.equals(sessionNum)){

                        Toast.makeText(getApplicationContext(), "제대로입력해주십셔~", Toast.LENGTH_SHORT).show();
                        Log.d("4", String.valueOf(check));
                        Log.d("5", checkSessionNum);
                        Log.d("6", sessionNum);

                    }
                }
            });
        }
    };

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button) {

            roomNum = pinNum.getText().toString();

            if (roomNum == null)
            {

                Toast.makeText(getApplicationContext(), "핀번입력해주십셔~", Toast.LENGTH_SHORT).show();

                return;

            }

            mSocket.emit("android_join", roomNum, sessionNum);
        }

    }
}

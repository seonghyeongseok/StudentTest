package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by skadl on 2018-03-08.
 */

public class Blindrace extends AppCompatActivity implements View.OnClickListener{

    public static final String ServerIP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890";
    private Socket mSocket;
    private Button button, button2, button3, button4, submit;
    private String arg = "";
    private String stdNum, nick, roomNum, rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.race_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent getInfo = getIntent();

        stdNum = getInfo.getStringExtra("Student_num");
        nick = getInfo.getStringExtra("Nick_name");
        roomNum = getInfo.getStringExtra("Room_num");
        rank = getInfo.getStringExtra("rank");

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(this);

        button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(this);

        button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(this);

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);

       try {
            mSocket = IO.socket("http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890");
            mSocket.on("mid_ranking", midresult);
            //  next_quiz
            mSocket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener midresult = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            Blindrace.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(Blindrace.this, MidResult.class);

                    intent.putExtra("Student_num", stdNum);
                    intent.putExtra("Nick_name", nick);
                    intent.putExtra("Room_num", roomNum);
                    intent.putExtra("rank", rank);
                    intent.putExtra("ResultInfo", arg0[0].toString());

                    startActivity(intent);

                }
            });
        }
    };

    @Override
    public void onClick(View view) {

        if(R.id.button == view.getId()){
            arg = "1";
        }
        else if(R.id.button2 == view.getId()){
            arg = "2";
        }
        else if(R.id.button3 == view.getId()){
            arg = "3";
        }
        else if(R.id.button4 == view.getId()){
            arg = "4";
        }
        else if(R.id.submit == view.getId()){

            submit.setOnClickListener(null);
            //  arg 값이 없을 경우

            //  arg 값이 있을 경우
            try {
                mSocket = IO.socket(ServerIP);
                mSocket.connect();
            } catch(URISyntaxException e) {
                e.printStackTrace();
            }

            mSocket.emit("answer", arg, stdNum, nick);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("leaveRoom",roomNum,stdNum);
    }
}

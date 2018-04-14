package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
/**
 * Created by skadl on 2018-04-14.
 */

public class WaitingRoom extends AppCompatActivity {

    private String stdNum, nick, roomNum, character;
    private Socket mSocket;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_room);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            mSocket = IO.socket("http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890");
            mSocket.on("android_game_start", gameStart);
            mSocket.connect();


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private Emitter.Listener gameStart = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            WaitingRoom.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WaitingRoom.this, Blindrace.class);
                    Intent getInfo = getIntent();

                    stdNum = getInfo.getStringExtra("Student_num");
                    nick = getInfo.getStringExtra("Nick_name");
                    roomNum = getInfo.getStringExtra("Room_num");
                    character = getInfo.getStringExtra("char");

                    intent.putExtra("Student_num", stdNum);
                    intent.putExtra("Nick_name", nick);
                    intent.putExtra("rank", "0");
                    intent.putExtra("char", character);
                    startActivity(intent);
                }
            });
        }
    };
}

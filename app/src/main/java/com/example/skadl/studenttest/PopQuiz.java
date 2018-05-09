package com.example.skadl.studenttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by skadl on 2018-05-07.
 */

public class PopQuiz extends AppCompatActivity {

    public static final String ServerIP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890";
    private Socket mSocket;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_quiz);

        try {

            mSocket = IO.socket(ServerIP);
            mSocket.on("popInfo", quizInfo);
            mSocket.connect();

        } catch (URISyntaxException e) {

            e.printStackTrace();

        }

    }

    private Emitter.Listener quizInfo = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            PopQuiz.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                }
            });
        }
    };
}
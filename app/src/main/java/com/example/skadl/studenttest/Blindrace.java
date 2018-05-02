package com.example.skadl.studenttest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    private String answerNum = "";
    private String sessionNum, nick, roomNum, rank, character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.race_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent getInfo = getIntent();

        character   = getInfo.getStringExtra("char");
        sessionNum  = getInfo.getStringExtra("session_num");
        nick        = getInfo.getStringExtra("Nick_name");
        roomNum     = getInfo.getStringExtra("Room_num");
        rank        = getInfo.getStringExtra("rank");

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

        try
        {

            mSocket = IO.socket(ServerIP);
            mSocket.on("mid_ranking", midresult);
            //  mSocket.on("", );
            //  next_quiz
            mSocket.connect();

        }
        catch (URISyntaxException e)
        {

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

                    intent.putExtra("session_num", sessionNum);
                    intent.putExtra("Nick_name", nick);
                    intent.putExtra("Room_num", roomNum);
                    intent.putExtra("rank", rank);
                    intent.putExtra("char", character);
                    intent.putExtra("ResultInfo", arg0[0].toString());

                    startActivity(intent);

                }
            });
        }
    };

    @Override
    public void onClick(View view) {

        if(R.id.button == view.getId())
        {

            answerNum = "1";
            button.setBackgroundColor(Color.WHITE);
            button2.setBackgroundColor(Color.parseColor("#3598db"));
            button3.setBackgroundColor(Color.parseColor("#f1c40f"));
            button4.setBackgroundColor(Color.parseColor("#e84c3d"));

        }
        else if(R.id.button2 == view.getId())
        {

            answerNum = "2";
            button2.setBackgroundColor(Color.WHITE);
            button.setBackgroundColor(Color.parseColor("#1bbc9b"));
            button3.setBackgroundColor(Color.parseColor("#f1c40f"));
            button4.setBackgroundColor(Color.parseColor("#e84c3d"));

        }
        else if(R.id.button3 == view.getId())
        {

            answerNum = "3";
            button3.setBackgroundColor(Color.WHITE);
            button.setBackgroundColor(Color.parseColor("#1bbc9b"));
            button2.setBackgroundColor(Color.parseColor("#3598db"));
            button4.setBackgroundColor(Color.parseColor("#e84c3d"));

        }
        else if(R.id.button4 == view.getId())
        {

            answerNum = "4";
            button4.setBackgroundColor(Color.WHITE);
            button.setBackgroundColor(Color.parseColor("#1bbc9b"));
            button2.setBackgroundColor(Color.parseColor("#3598db"));
            button3.setBackgroundColor(Color.parseColor("#f1c40f"));

        }
        else if(R.id.submit == view.getId())
        {

            submit.setOnClickListener(null);
            //  arg 값이 없을 경우

            if(answerNum == "")
            {

                Toast.makeText(getApplicationContext(), "답을 눌러쥬세용~", Toast.LENGTH_SHORT).show();
                return;

            }
            //  arg 값이 있을 경우
            try
            {

                mSocket = IO.socket(ServerIP);
                mSocket.connect();

            }
            catch(URISyntaxException e) {

                e.printStackTrace();

            }

            mSocket.emit("answer", roomNum, answerNum, sessionNum, nick);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("leaveRoom",roomNum, sessionNum);
    }
}

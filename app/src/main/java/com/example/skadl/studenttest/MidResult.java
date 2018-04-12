package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by skadl on 2018-04-03.
 */

public class MidResult extends AppCompatActivity {

    private Socket mSocket;
    public static final String ServerIP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890";
    private Button button;
    private String test, stdNum, nick, roomNum, beforeRank, afterRank, point;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mid_result);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        button = (Button)findViewById(R.id.button5);

        Intent getInfo = getIntent();

        stdNum = getInfo.getStringExtra("Student_num");
        nick = getInfo.getStringExtra("Nick_name");
        roomNum = getInfo.getStringExtra("Room_num");
        test = getInfo.getStringExtra("test");
        beforeRank = getInfo.getStringExtra("rank");

        try {
            JSONArray array = new JSONArray(test);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String temp = obj.optString("user_num");

                if (stdNum.equals(temp)) {
                    point = obj.optString("point");
                    afterRank = String.valueOf(i+1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //int aRank = Integer.parseInt(afterRank);
        //int bRank = Integer.parseInt(beforeRank);
        //int rank = Integer.parseInt(afterRank) - Integer.parseInt(beforeRank);
        //String result = point + "점, " + afterRank + "등, " + String.valueOf(rank) + "등";

        button.setText(afterRank+","+beforeRank);


        try {
            mSocket = IO.socket(ServerIP);
            mSocket.on("android_nextquiz", nextQuiz);
            mSocket.on("race_ending", raceEnding);
            mSocket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener nextQuiz = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            MidResult.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //String test = arg0[0].toString();

                    Intent intent = new Intent(MidResult.this, Blindrace.class);
                    intent.putExtra("Student_num", stdNum);
                    intent.putExtra("Nick_name", nick);
                    intent.putExtra("Room_num", roomNum);
                    intent.putExtra("Rank", afterRank);
                    startActivity(intent);

                }
            });
        }
    };

    private Emitter.Listener raceEnding = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            MidResult.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //String test = arg0[0].toString();

                    Intent intent = new Intent(MidResult.this, EnterRoom.class);
                    intent.putExtra("Student_num", stdNum);
                    startActivity(intent);

                }
            });
        }
    };
}

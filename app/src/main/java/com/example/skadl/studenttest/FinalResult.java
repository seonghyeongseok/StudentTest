package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by skadl on 2018-04-13.
 */

public class FinalResult extends AppCompatActivity{

    public static final String ServerIP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890";

    private Socket      mSocket;

    private ImageView   imageView;
    private TextView    nickName, point, resultView;
    private String      sessionNum, nick, character, result;
    private String      finalResult = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_result);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        imageView   = (ImageView)findViewById(R.id.character);

        nickName       = (TextView) findViewById(R.id.nick);
        point       = (TextView) findViewById(R.id.point);
        resultView       = (TextView) findViewById(R.id.result);

        Intent getInfo = getIntent();

        character   = getInfo.getStringExtra("char");
        nick        = getInfo.getStringExtra("Nick_name");
        sessionNum  = getInfo.getStringExtra("session_num");
        finalResult      = getInfo.getStringExtra("result");

        Log.e("result", finalResult);

        try{

            JSONArray result = new JSONArray(finalResult);

            for(int i = 0 ; i < result.length() ; i++){
                JSONObject studentResult = result.getJSONObject(i);

                String character = studentResult.optString("characterId");
                String score = studentResult.optString("score");
                Boolean pass = studentResult.optBoolean("retestState");

                imageView.setImageResource(getResources().getIdentifier(
                        "char"+character, "drawable", this.getPackageName()));

                nickName.setText(nick);
                point.setText(score);

                if(pass){
                    resultView.setText("합격");
                }else
                    resultView.setText("불 합격");
            }

        }catch (Exception e){

        }



    }
}

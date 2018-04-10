package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
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
    private String test, stdNum, nick, roomNum;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mid_result);

        button = (Button)findViewById(R.id.button5);

        Intent getInfo = getIntent();

        test = getInfo.getStringExtra("test");
        stdNum = getInfo.getStringExtra("num");
        nick = getInfo.getStringExtra("nickname");
        roomNum = getInfo.getStringExtra("roomnum");

        button.setText(test);

        try {
            mSocket = IO.socket(ServerIP);
            mSocket.on("android_nextquiz", nextQuiz);
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
                    intent.putExtra("num", stdNum);
                    intent.putExtra("nickname", nick);
                    intent.putExtra("roomnum", roomNum);
                    startActivity(intent);


                    //button.setText();
                   /* try {
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject registered = jsonArray.getJSONObject(i);


                            String registered_id = registered.getString("user_id");
                            String registered_pw = registered.getString("user_password");

                            if(registered_id.equals(ID) && registered_pw.equals(PW)){
                                Student_num = registered.getString("user_num");

                                Intent intent = new Intent(login.this, Character_select.class);
                                intent.putExtra("Student_num",Student_num);
                                startActivity(intent);
                            }

                        }
//                                textView.setText(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONEXCEPTION", "JSONEXCEPTION");
                    }
*/
                }
            });
        }
    };
}

package com.example.skadl.studenttest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private final String IP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com/mobileLogin";

    private EditText w_ID, w_PW;
    private Button login;
    private String stdName = null;
    private String sessionNum = null;
    private boolean checkLogin = false;
    private String mySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        w_ID = (EditText) findViewById(R.id.userid);
        w_PW = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        mySession = pref.getString("session_id", "");
        stdName   = pref.getString("std_name", "");

        Intent intent = new Intent(Login.this, Main.class);
        Log.e("session", mySession);

        if (mySession != null) {
            intent.putExtra("session_num", mySession);
            intent.putExtra("Student_name", stdName);

            startActivity(intent);
        }


        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.login) {

            String ID = w_ID.getText().toString();
            String PW = w_PW.getText().toString();

            Login(ID, PW);
            /*checkLogin = false;

            String returnValue = null;
            String params = null;
            String ID = w_ID.getText().toString();
            String PW = w_PW.getText().toString();
            Boolean check = false;

            params = "p_ID=" + ID + "&p_PW=" + PW;

            try {

                returnValue = new MyAsyncTask(IP).execute(params).get();
                Log.e("aaa", returnValue);

            } catch (InterruptedException e) {

                e.printStackTrace();

            } catch (ExecutionException e) {

                e.printStackTrace();

            }

            try {

                JSONObject obj = new JSONObject(returnValue);

                checkLogin = obj.optBoolean("check");

                check = obj.optBoolean("loginCheck");

                stdName = obj.optString("userName");
                sessionNum = obj.optString("sessionId");
                Log.e("로긴", sessionNum);


            } catch (Exception e) {

                e.printStackTrace();
                return;

            }
            //  check, sessionId, userName

            if (checkLogin) {

                if (!check) {
                    Toast.makeText(getBaseContext(), "이미 로그인 되어있습니당", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(Login.this, Main.class);
                intent.putExtra("session_num", sessionNum);
                intent.putExtra("Student_name", stdName);

                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("id", ID);
                editor.putString("pw", PW);
                editor.commit();
                startActivity(intent);

            } else {

                Toast.makeText(getBaseContext(), "틀렸슴다", Toast.LENGTH_SHORT).show();

            }*/
        }
    }

    private void Login(String ID, String PW) {

        checkLogin = false;

        String returnValue = null;
        String params = null;
        Boolean check = false;

        params = "p_ID=" + ID + "&p_PW=" + PW;

        try {

            returnValue = new MyAsyncTask(IP).execute(params).get();
            Log.e("aaa", returnValue);

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        try {

            JSONObject obj = new JSONObject(returnValue);

            checkLogin = obj.optBoolean("check");

            check = obj.optBoolean("loginCheck");

            stdName = obj.optString("userName");
            sessionNum = obj.optString("sessionId");
            Log.e("로긴", sessionNum);


        } catch (Exception e) {

            e.printStackTrace();
            return;

        }
        //  check, sessionId, userName

        if (checkLogin) {

            if (!check) {
                Toast.makeText(getBaseContext(), "이미 로그인 되어있습니당", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("session_id", sessionNum);
            editor.putString("std_name", stdName);

            Intent intent = new Intent(Login.this, Main.class);
            intent.putExtra("session_num", sessionNum);
            intent.putExtra("Student_name", stdName);
            Log.e("name", stdName);
            editor.commit();
            startActivity(intent);

        } else {

            Toast.makeText(getBaseContext(), "틀렸슴다", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        w_ID.setText(null);
        w_PW.setText(null);
    }
}
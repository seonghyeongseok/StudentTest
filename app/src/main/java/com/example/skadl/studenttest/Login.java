package com.example.skadl.studenttest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;

/**
 * Created by pheonix on 2018-03-02.
 */

public class Login extends AppCompatActivity {

    private EditText    w_ID, w_PW;
    private Button      login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Context context = this;

        w_ID    = (EditText) findViewById(R.id.userid);
        w_PW    = (EditText) findViewById(R.id.password);
        login   = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                final String ID = w_ID.getText().toString();
                final String PW = w_PW.getText().toString();

                try {
                    URL url = new URL("http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com/mobileLogin");
                    new AsyncTask<URL, Integer, String>() {
                        @Override
                        protected void onProgressUpdate(Integer... values) {
                            super.onProgressUpdate(values);
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        protected String doInBackground(URL... params) {

                            int i = 0;

                            String result = new String();
                            try {
                                publishProgress(i++);
                                HttpURLConnection connection = (HttpURLConnection) params[0].openConnection();
                                Log.i("http", "connected");
                                connection.setRequestMethod("POST");//POST
                                connection.setDoOutput(true);//쓰기모드 - post로 강제 설정됨
                                connection.setDoInput(true);//읽기모드
                                connection.setUseCaches(false);
                                connection.setDefaultUseCaches(false);

                                OutputStream os = connection.getOutputStream();
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                                writer.write("p_ID=" + ID + "&p_PW=" + PW);
                                writer.flush();

                                publishProgress(i++);
                                InputStream is = connection.getInputStream();
                                StringBuilder builder = new StringBuilder();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //문자열 셋팅
                                String line;

                                while ((line = reader.readLine()) != null) {
                                    builder.append(line + "\n");
                                    publishProgress(i++);
                                }
                                result = builder.toString();
                                Log.i("http", "result=" + result);
                                publishProgress(i++);

                                is.close();

                            } catch (IOException me) {
                                me.printStackTrace();
                            }
                            return result;
                        }

                        @Override
                        protected void onPostExecute(String check) {
                            super.onPostExecute(check);

                            String  stdName     = null;
                            String  stdNum      = null;
                            boolean checkLogin  = false;

                            try {
                                //JSONArray array = new JSONArray(check);

                                JSONObject obj = new JSONObject(check);

                                checkLogin  = obj.optBoolean("check");
                                stdName     = obj.optString("userName");
                                stdNum      = obj.optString("sessionId");

                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                            //  check, sessionId, userName

                            if(checkLogin){
                                Intent intent = new Intent(Login.this, Main.class);
                                intent.putExtra("Student_num", stdNum);
                                intent.putExtra("Student_name", stdName);
                                startActivity(intent);
                            }else{
                                Toast.makeText(context, "틀렸슴다", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //  아이디 패스워드 둘다 맞을때
                        //아닐때

                    }.execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "인터넷이 연결되어 있지 않습니다.~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
package com.example.skadl.studenttest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by pheonix on 2018-03-02.
 */

public class Login extends AppCompatActivity {

    private EditText w_ID, w_PW;
    private Button login, join;
    private TextView textView;
    private Socket mSocket;
    private String test = "안넘어옴";
    boolean check = true;

    private Emitter.Listener errorMsg = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            Login.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    test = (String) arg0[0];
                    check = false;
                    Toast.makeText(getApplicationContext(), test, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        w_ID = (EditText) findViewById(R.id.userid);
        w_PW = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        try {
            mSocket = IO.socket("http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890");
            mSocket.connect();
            mSocket.on("err_msg", errorMsg);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        login.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                final String ID = w_ID.getText().toString();
                final String PW = w_PW.getText().toString();

                try {
                    URL url = new URL("http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890/persons");
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
//                                writer.write("id="+ID+"&pw="+PW);
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
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);

                            String Student_num = "";

                            try {
                                JSONArray jsonArray = new JSONArray(s);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject registered = jsonArray.getJSONObject(i);

                                    String registered_id = registered.getString("user_id");
                                    String registered_pw = registered.getString("user_password");

                                        if (registered_id.equals(ID) && registered_pw.equals(PW)) {
                                            Student_num = registered.getString("user_num");

                                        if(check == false){
                                            login.setText("씨벌");

                                        }else{
                                            login.setText("슈벌");
                                        }


                                        mSocket.emit("checkedLogin", Student_num);



                                        /*Intent intent = new Intent(Login.this, Main.class);
                                        intent.putExtra("Student_num", Student_num);
                                        startActivity(intent);*/

                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("JSONEXCEPTION", "JSONEXCEPTION");
                            }
                        }

                    }.execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("CONNECTION FAIL", "CONNECTION FAIL");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("연결 실패");
                    alertDialogBuilder
                            .setMessage("네트워크 연결에 실패했습니다.")
                            .setCancelable(false)
                            .setNegativeButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 다이얼로그를 취소한다
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

                
            }
        });

    }
    public void Login_check(){

    }


}
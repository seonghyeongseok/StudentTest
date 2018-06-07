package com.example.skadl.studenttest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by skadl on 2018-05-24.
 */

public class MyAsyncTask extends AsyncTask<String, Void, String> {

    String IP;
    String returnValue;

    MyAsyncTask(String ip){
        IP = ip;
    }

    @Override
    public String doInBackground(String... params) {
        try {
            String url = IP;
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            //Log.d("output", outputInBytes.toString());
            writer.write(params[0]);
            writer.flush();

            int retCode = conn.getResponseCode();

            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = br.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            br.close();

            returnValue = response.toString();

            if(returnValue == null){
                Log.e("없어", "없어");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnValue;
    }
}
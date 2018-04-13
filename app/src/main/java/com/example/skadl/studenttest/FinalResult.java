package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

/**
 * Created by skadl on 2018-04-13.
 */

public class FinalResult extends AppCompatActivity{

    private Button button;
    private String point, rank, stdNum, nick;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_result);

        Intent getInfo = getIntent();

        nick = getInfo.getStringExtra("Nick_name");
        stdNum = getInfo.getStringExtra("Student_num");
        point = getInfo.getStringExtra("point");
        rank = getInfo.getStringExtra("rank");
        

        Log.d("rank", rank);
        button = (Button)findViewById(R.id.button6);
        button.setText(nick+"님"+point+"점"+rank+"등");
    }

}

package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by skadl on 2018-04-13.
 */

public class FinalResult extends AppCompatActivity{

    private ImageView imageView;
    private TextView text1, text2, text3;
    private String point, rank, stdNum, nick, character;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_result);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        imageView   = (ImageView)findViewById(R.id.character);

        text1       = (TextView) findViewById(R.id.point);
        text2       = (TextView) findViewById(R.id.rank);
        text3       = (TextView) findViewById(R.id.nick);

        Intent getInfo = getIntent();

        character   = getInfo.getStringExtra("char");
        nick        = getInfo.getStringExtra("Nick_name");
        stdNum      = getInfo.getStringExtra("Student_num");
        point       = getInfo.getStringExtra("point");
        rank        = getInfo.getStringExtra("rank");

        imageView.setImageResource(getResources().getIdentifier(
                "char"+character, "drawable", this.getPackageName()));
        text1.setText(Integer.parseInt(point) * 100 + "점");
        text2.setText(rank+" 등");
        text3.setText(nick+" 님");


    }
}

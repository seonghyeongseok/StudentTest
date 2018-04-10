package com.example.skadl.studenttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by skadl on 2018-03-12.
 */

public class Feedback extends AppCompatActivity implements View.OnClickListener{

    private ImageButton write;
    private Button write2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        write = (ImageButton)findViewById(R.id.writeImage);
        write2 = (Button)findViewById(R.id.writeButton);

        write.setOnClickListener(this);
        write2.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.writeButton || view.getId() == R.id.writeImage){
            //  글쓰기
            //  글쓰기를 따로 추가할까?
            //  글쓰기를 밑에 레이아웃을 추가해서 만들까?
        }
    }
}
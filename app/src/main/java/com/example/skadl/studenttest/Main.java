package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by skadl on 2018-03-08.
 */

public class Main extends AppCompatActivity implements View.OnClickListener{

    private ImageButton admission, record, feedback;
    private String      sessionNum, stdName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main);

        Intent getInfo = getIntent();

        sessionNum = getInfo.getStringExtra("session_num");
        stdName = getInfo.getStringExtra("Student_name");

        admission = (ImageButton)findViewById(R.id.admission);
        admission.setOnClickListener(this);

        record = (ImageButton)findViewById(R.id.record);
        record.setOnClickListener(this);

        feedback = (ImageButton)findViewById(R.id.feedback);
        feedback.setOnClickListener(this);

    }

    public void onClick(View view) {

        Intent intent;

        if(view.getId() == R.id.admission)
        {
            //  방 입장
            intent = new Intent(Main.this, InputRoomNum.class);
            intent.putExtra("session_num", sessionNum);
            intent.putExtra("student_name", stdName);
            startActivity(intent);

        }
        else if(view.getId() == R.id.record)
        {
            //  학습기록 조회
            intent = new Intent(Main.this, GroupMain.class);
            intent.putExtra("session_num", sessionNum);
            intent.putExtra("student_name", stdName);
            startActivity(intent);

        }
        else if(view.getId() == R.id.feedback)
        {
            //  질문하기
            intent = new Intent(Main.this, Feedback.class);
            intent.putExtra("session_num", sessionNum);
            intent.putExtra("student_name", stdName);
            startActivity(intent);

        }
    }
}


package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by skadl on 2018-03-12.
 */

public class  Retest extends AppCompatActivity implements View.OnClickListener{

    private Button      submit;
    private TextView    questName, textView;
    private String      sessionNum, stdName, quizName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retest);

        Intent getInfo = getIntent();

        //  문제 명 받아오기
        sessionNum = getInfo.getStringExtra("session_num");
        stdName = getInfo.getStringExtra("student_name");
        quizName = getInfo.getStringExtra("quiz_name");

        questName = (TextView)findViewById(R.id.questName);
        questName.setText(quizName);

        textView = (TextView)findViewById(R.id.textView6);

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    public void onClick(View view) {
        if(view.getId() == R.id.submit){

            //  재시험 정보 db 등록
            Intent intent = new Intent(Retest.this, GradeRecord.class);
            startActivity(intent);

        }
    }
}
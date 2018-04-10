package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by skadl on 2018-03-08.
 */

public class Main extends AppCompatActivity implements View.OnClickListener{

    private String Title;
    private ImageButton admission, myGroup, record, feedback;
    private String title;

    public Main(){
        title = "메인";
    }
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main);

        setTitle(title);
        admission = (ImageButton)findViewById(R.id.admission);
        admission.setOnClickListener(this);

        myGroup = (ImageButton)findViewById(R.id.myGroup);
        myGroup.setOnClickListener(this);

        record = (ImageButton)findViewById(R.id.record);
        record.setOnClickListener(this);

        feedback = (ImageButton)findViewById(R.id.feedback);
        feedback.setOnClickListener(this);
    }

    public void onClick(View view) {

        Intent intent;
        Intent getNum = getIntent();
        String stdNum = getNum.getStringExtra("Student_num");

        if(view.getId() == R.id.admission){
            //  방 입장
            intent = new Intent(Main.this, EnterRoom.class);
            intent.putExtra("Student_num", stdNum);
            startActivity(intent);

        }else if(view.getId() == R.id.myGroup){
            //  나의 그룹
            intent = new Intent(Main.this, GroupMain.class);
            intent.putExtra("Student_num", stdNum);
            startActivity(intent);

        }else if(view.getId() == R.id.record){
            //  학습기록 조회
            intent = new Intent(Main.this, GradeRecord.class);
            intent.putExtra("Student_num", stdNum);
            startActivity(intent);

        }else if(view.getId() == R.id.feedback){
            //  질문하기
            intent = new Intent(Main.this, Feedback.class);
            intent.putExtra("Student_num", stdNum);
            startActivity(intent);
        }
    }
}


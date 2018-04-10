package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by skadl on 2018-03-08.
 */

public class GradeRecord extends AppCompatActivity implements View.OnClickListener{

    private Button note, retest;

    public GradeRecord() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_record);

        //  받아온 반을 기준으로 점수 출력 및 개인 현황 표 만들기
        //  미제출, 미응시를 클릭할 경우 오답노트와 재시험으로 넘어갈 수 있게
        note = (Button)findViewById(R.id.note);
        note.setOnClickListener(this);

        retest = (Button)findViewById(R.id.retest);
        retest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        if(view.getId() == R.id.note){
            intent = new Intent(GradeRecord.this, Note.class);
            startActivity(intent);
        }else if(view.getId() == R.id.retest){
            intent = new Intent(GradeRecord.this, Retest.class);
            startActivity(intent);
        }

    }
}

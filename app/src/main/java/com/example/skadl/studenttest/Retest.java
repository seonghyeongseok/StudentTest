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
    private TextView    questName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retest);

        //  문제 명 받아오기
        String name = "레이스1";
        questName = (TextView)findViewById(R.id.questName);
        questName.setText(name);

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
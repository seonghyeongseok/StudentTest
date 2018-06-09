package com.example.skadl.studenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.concurrent.ExecutionException;

/**
 * Created by skadl on 2018-03-08.
 */

public class Main extends AppCompatActivity implements View.OnClickListener{

    private final String IP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com/mobileLogout";

    private ImageButton admission, record, feedback;
    private String      sessionNum, stdName;
    private String      returnValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main);

        Intent getInfo = getIntent();

        sessionNum = getInfo.getStringExtra("session_num");
        stdName = getInfo.getStringExtra("Student_name");

        Log.e("main", sessionNum);

        admission = (ImageButton)findViewById(R.id.admission);
        admission.setOnClickListener(this);

        record = (ImageButton)findViewById(R.id.record);
        record.setOnClickListener(this);

        feedback = (ImageButton)findViewById(R.id.feedback);
        feedback.setOnClickListener(this);

    }

    /**
     * Click 처리 함수.
     * @param view Clicked view
     */
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

    @Override
    protected void onStop() {
        super.onStop();

        Log.e("스탑", "스탑");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        String params;

        params = "sessionId=" + sessionNum;

        try {

            returnValue = new MyAsyncTask(IP).execute(params).get();
            Log.e("dd",returnValue);

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

    }
}


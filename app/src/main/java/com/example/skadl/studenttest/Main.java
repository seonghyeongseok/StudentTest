package com.example.skadl.studenttest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**
 * Created by skadl on 2018-03-08.
 */

public class Main extends AppCompatActivity implements View.OnClickListener{

    private final String IP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com/mobileLogout";

    private ImageButton admission, record, feedback;
    private TextView    nameView;
    private String      sessionNum, stdName;
    private String      returnValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        Intent getInfo = getIntent();

        sessionNum = getInfo.getStringExtra("session_num");
        stdName = getInfo.getStringExtra("Student_name");

        nameView = (TextView)findViewById(R.id.std_name);
        nameView.setText(stdName);

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
            finish();

        }
        else if(view.getId() == R.id.feedback)
        {
            //  질문하기
            /*intent = new Intent(Main.this, Feedback.class);
            intent.putExtra("session_num", sessionNum);
            intent.putExtra("student_name", stdName);
            startActivity(intent);*/

            String params = null;

            params = "sessionId=" + sessionNum;

            try {

                returnValue = new MyAsyncTask(IP).execute(params).get();
                Log.e("aaa", returnValue);
                super.onBackPressed();

            } catch (InterruptedException e) {

                e.printStackTrace();

            } catch (ExecutionException e) {

                e.printStackTrace();

            }

        }
    }

    public void onBackPressed(){

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setMessage("종료하시겠습니까?");

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                moveTaskToBack(true);

                finish();

                android.os.Process.killProcess(android.os.Process.myPid());

            }
        });
        alert.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

       /* String params;

        params = "sessionId=" + sessionNum;

        try {

            returnValue = new MyAsyncTask(IP).execute(params).get();
            Log.e("dd",returnValue);

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }
*/
    }
}


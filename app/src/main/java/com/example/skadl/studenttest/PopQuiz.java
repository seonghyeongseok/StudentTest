package com.example.skadl.studenttest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by skadl on 2018-05-07.
 */

public class PopQuiz extends AppCompatActivity implements View.OnClickListener{

    public static final String ServerIP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890";

    private Socket          mSocket;

    private ArrayList<JSONObject> quiz = new ArrayList<>();
    private JSONObject obj;

    private LinearLayout    loading, choice, essay;
    private TextView        status, quizName, timer;

    private TextView        essayPassage, choicePassage;

    private EditText        essayAnswer;
    private RadioGroup      choiceAnswer;
    private RadioButton     answer1, answer2, answer3, answer4;

    private ImageButton send;

    private int         numberOfQuiz;
    private int         quizStatus = 1;
    private int         timerValue = 0;
    private Boolean     starting = true;

    private String roomNum, sessionNum, stdName, allStatus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_quiz);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        Intent getInfo = getIntent();

        sessionNum = getInfo.getStringExtra("session_num");
        stdName = getInfo.getStringExtra("student_name");
        roomNum = getInfo.getStringExtra("room_num");
        //  학생 이름 보내기 추가

        essay = (LinearLayout) findViewById(R.id.essay_view);
        choice = (LinearLayout) findViewById(R.id.choice_view);
        loading = (LinearLayout) findViewById(R.id.loading);

        status = (TextView) findViewById(R.id.status);
        quizName = (TextView) findViewById(R.id.quiz_name);
        timer = (TextView) findViewById(R.id.timer);

        essayPassage = (TextView) findViewById(R.id.essay_passage);
        choicePassage = (TextView) findViewById(R.id.choice_passage);

        essayAnswer = (EditText) findViewById(R.id.essay_answer);

        choiceAnswer = (RadioGroup) findViewById(R.id.choice_answer);

        answer1 = (RadioButton) findViewById(R.id.answer1);
        answer2 = (RadioButton) findViewById(R.id.answer2);
        answer3 = (RadioButton) findViewById(R.id.answer3);
        answer4 = (RadioButton) findViewById(R.id.answer4);

        send = (ImageButton) findViewById(R.id.send);
        send.setOnClickListener(this);

        essay.setVisibility(View.GONE);
        choice.setVisibility(View.GONE);

        try {

            mSocket = IO.socket(ServerIP);
            //mSocket.on("popQuiz_Info", quizInfo);
            mSocket.on("pop_quiz_start", quizStart);
            //mSocket.on("re_join_pop_quiz", reStart);

            mSocket.on("pop_timer", time);
            mSocket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private Emitter.Listener quizStart = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            PopQuiz.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    starting = false;

                    loading.setVisibility(View.GONE);

                    String quizInfo = arg0[0].toString();
                    String quizname = arg0[1].toString();
                    String checkSession = arg0[2].toString();
                    allStatus = arg0[3].toString();

                    Log.e("총 문제 수~", allStatus);
                    Log.e("시작?", "응 재시작");
                    Log.e("체크세션", checkSession);
                    Log.e("내 세션", sessionNum);

                    if(checkSession.equals("X")){

                    }else if(!checkSession.equals(sessionNum)){
                        return;
                    }

                    Log.e("재시작", "실행됨~");
                    quizName.setText(quizname);
                    Log.e("quizName", quizname);

                    Log.d("quizInfo", quizInfo);

                    try
                    {

                        JSONArray array = new JSONArray(quizInfo);

                        numberOfQuiz = array.length();

                        for(int i = 0 ; i < numberOfQuiz ; i++) {

                            JSONObject obj = array.getJSONObject(i);

                            quiz.add(obj);
                        }

                        Collections.shuffle(quiz);

                        status.setText(quizStatus + " / " + numberOfQuiz);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    obj = quiz.get(0);

                    Log.d("quizid", obj.optString("quizId"));
                    Log.d("문제", obj.optString("question"));
                    Log.d("정답", obj.optString("right"));
                    Log.d("답1", obj.optString("example1"));
                    Log.d("답2", obj.optString("example2"));
                    Log.d("답3", obj.optString("example3"));

                    switch (obj.optString("makeType")) {

                        case "sub":
                            essay.setVisibility(View.VISIBLE);
                            essayPassage.setText(obj.optString("question"));

                            break;

                        case "obj":
                            choice.setVisibility(View.VISIBLE);
                            choicePassage.setText(obj.optString("question"));
                            answer1.setText(obj.optString("right"));
                            answer2.setText(obj.optString("example1"));
                            answer3.setText(obj.optString("example2"));
                            answer4.setText(obj.optString("example3"));
                            break;

                        default:
                            break;

                    }
                    mHandler.sendEmptyMessage(0);
                }
            });
        }
    };

    private Emitter.Listener time = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            PopQuiz.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    timerValue = Integer.valueOf(arg0[0].toString());

                }
            });
        }
    };

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            timerValue++;
            timer.setText(timerValue+"초");

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            mHandler.sendEmptyMessageDelayed(0,1000);
        }
    };


    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.send){

            //      체크
            status.setText(quizStatus+1+" / "+numberOfQuiz);

            String answer = null;

            switch (obj.optString("makeType")) {

                case "obj":
                    answer = ((RadioButton)findViewById(choiceAnswer.getCheckedRadioButtonId())).getText().toString();
                    break;

                case "sub":
                    answer = essayAnswer.getText().toString();
                    break;

                default:
                    break;

            }

            mSocket.emit("answer", roomNum , answer, sessionNum, null, obj.optString("quizId"));

            if(quizStatus == numberOfQuiz) {
                //  실행 안됬어영
                mSocket.emit("pop_quiz_status", roomNum, sessionNum);

                Intent intent = new Intent(PopQuiz.this, Main.class);
                intent.putExtra("session_num", sessionNum);
                intent.putExtra("Student_name", stdName);
                //  학생 이름 보내기 추가
                startActivity(intent);
                finish();
                return;
            }

            //      체크
            obj = quiz.get(quizStatus);

            Log.d("quizid", obj.optString("quizId"));
            Log.d("문제", obj.optString("question"));
            Log.d("정답", obj.optString("right"));
            Log.d("답1", obj.optString("example1"));
            Log.d("답2", obj.optString("example2"));
            Log.d("답3", obj.optString("example3"));

            essay.setVisibility(View.GONE);
            choice.setVisibility(View.GONE);

            switch (obj.optString("makeType")) {

                case "obj":

                    choice.setVisibility(View.VISIBLE);
                    choicePassage.setText(obj.optString("question"));

                    answer1.setText(obj.optString("right"));
                    answer2.setText(obj.optString("example1"));
                    answer3.setText(obj.optString("example2"));
                    answer4.setText(obj.optString("example3"));

                    break;

                case "sub":

                    essay.setVisibility(View.VISIBLE);
                    essayPassage.setText(obj.optString("question"));

                    break;

                default:
                    break;

            }

            essayAnswer.setText(null);
            answer1.setChecked(true);

            quizStatus++;
            //      체크
        }
    }

    @Override
    public void onBackPressed() {

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

                onBackPressed();

            }
        });
        alert.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(starting) {

            mSocket.emit("leaveRoom", roomNum, sessionNum);

        }else{



        }
    }
}
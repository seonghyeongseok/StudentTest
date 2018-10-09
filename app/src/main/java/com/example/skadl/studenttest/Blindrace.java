package com.example.skadl.studenttest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by skadl on 2018-03-08.
 */

public class Blindrace extends AppCompatActivity implements View.OnClickListener{

    public static final String ServerIP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com:8890",
            obj = "obj", sub = "sub";

    private Socket          mSocket;
    private Handler         handler;
    private Runnable        runnable;

    private Button          button, button2, button3, button4, submit, submit2;
    private ImageView       userImage, imageView2;
    private TextView        stdResult, stdResult2;
    private EditText        essay;
    private LinearLayout    choiceView, essayView, loadingView, midResultView;
    private RelativeLayout  answerView;

    private String answerNum = null;
    private String sessionNum, nick, roomNum, point, character, quizId, type, resultInfo, answerCheck, stdName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.race_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        choiceView      = (LinearLayout)findViewById(R.id.choice_answer);
        essayView       = (LinearLayout)findViewById(R.id.essay_answer);
        loadingView     = (LinearLayout)findViewById(R.id.loading);
        midResultView   = (LinearLayout)findViewById(R.id.mid_result);

        answerView      = (RelativeLayout)findViewById(R.id.image_background);

        choiceView.setVisibility(View.GONE);
        essayView.setVisibility(View.GONE);
        midResultView.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try
        {

            mSocket = IO.socket(ServerIP);
            mSocket.on("android_game_start", gameStart);
            //  퀴즈 시작 시, (roomPin , quizId , makeType)=>핀번호 , 답전송시 퀴즈Id값 , 1번 퀴즈의 유형

            mSocket.on("timer", timer);

            mSocket.on("android_mid_result", midResult);
            //  중간결과 ( roomPin , quizId , makeType , ranking)
            // => 핀, 답에보낼퀴즈Id , 다음문제퀴즈유형 , 현재까지 전체랭킹select*값
            // ranking은 json 배열이며 여기서 반복문을 돌려 if([i].sessionId = sessionId) [i].answer
            //을 뽑아내어 현재 문제를 맞췄는지 틀렸는지를 확인한다. 맞췄을 경우 answer는 "O" 틀렸으면 "X"

            mSocket.on("android_next_quiz", nextQuiz);
            //  다음 퀴즈로 넘어갈 때 사용되는 소켓함수 인자값은 없음 추후에 필요할 경우에 추가할 예정

            mSocket.on("race_result", raceEnding);
            // 레이스가 끝날경우 실행되는 함수 중간결과에서 받는것과 같은 양식으로 인자값을
            // 줄예정이며 사용하는 방법은 동일함

            mSocket.on("race_mid_correct", result);
            mSocket.connect();

        }
        catch (URISyntaxException e)
        {

            e.printStackTrace();

        }

        Intent getInfo = getIntent();

        userImage       = (ImageView)findViewById(R.id.character);
        imageView2      = (ImageView)findViewById(R.id.answer_image);

        stdResult       = (TextView) findViewById(R.id.result);
        stdResult2      = (TextView) findViewById(R.id.point);

        character   = getInfo.getStringExtra("char");
        sessionNum  = getInfo.getStringExtra("session_num");
        stdName     = getInfo.getStringExtra("student_name");
        nick        = getInfo.getStringExtra("Nick_name");
        roomNum     = getInfo.getStringExtra("room_num");

        button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(this);

        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(this);

        button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(this);

        button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(this);

        essay = (EditText)findViewById(R.id.essay);

        submit2 = (Button)findViewById(R.id.submit2);
        submit2.setOnClickListener(this);

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    private Emitter.Listener result = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            Blindrace.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                   stdResult.setText(arg0[0].toString());
                }
            });
        }
    };



    private Emitter.Listener timer = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            Blindrace.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                   /* String timer = arg0[0].toString();

                    //  시간초
                    runnable = new Runnable() {
                        @Override
                        public void run() {

                            essayView.setVisibility(View.GONE);
                            choiceView.setVisibility(View.GONE);
                            loadingView.setVisibility(View.GONE);
                            midResultView.setVisibility(View.VISIBLE);

                        }
                    };

                    handler = new Handler();
                    handler.postDelayed(runnable, Integer.parseInt(timer));
*/
                }
            });
        }
    };

    private Emitter.Listener nextQuiz = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            Blindrace.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //essayView.setVisibility(View.GONE);
                    //choiceView.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    midResultView.setVisibility(View.GONE);

                    /*submit2.setEnabled(true);
                    submit.setEnabled(true);*/

                    Log.d("11", type);

                    switch (type){
                        case sub:
                            //  주관식으로

                            essayView.setVisibility(View.VISIBLE);
                            Log.d("sub", sub);
                            break;
                        case obj:
                            //  객관식으로

                            choiceView.setVisibility(View.VISIBLE);
                            Log.d("obj", obj);
                            break;
                        default:
                            //  토스트 출력 ㄱㄱ
                            break;
                    }
                }
            });
        }
    };

    private Emitter.Listener midResult = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            Blindrace.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    quizId      = arg0[0].toString();
                    type        = arg0[1].toString();
                    resultInfo  = arg0[2].toString();

                    Log.d("nexttype", type);
                    Log.d("next2", arg0[1].toString());

                    essayView.setVisibility(View.GONE);
                    choiceView.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    midResultView.setVisibility(View.VISIBLE);

                    try
                    {
                        JSONArray array = new JSONArray(resultInfo);

                        for(int i = 0 ; i < array.length() ; i++) {

                            JSONObject obj = array.getJSONObject(i);

                            String temp = obj.optString("sessionId");

                            if (sessionNum.equals(temp)) {

                                point = obj.optString("rightCount");
                                // 맞는 개수

                                if (point == null) {
                                    point = "0";
                                }

                                answerCheck = obj.optString("answerCheck");
                                Log.e("췤", answerCheck);

                            }
                        }

                    }
                    catch (Exception e)
                    {

                        e.printStackTrace();

                    }

                    if(answerCheck.equals("O")){
                        imageView2.setImageResource(getResources().getIdentifier(
                                "correct", "drawable", "com.example.skadl.studenttest"));
                        stdResult.setTextColor(Color.parseColor("#33adff"));
                        answerView.setBackgroundColor(Color.parseColor("#33adff"));
                    }else if(answerCheck.equals("X")){
                        imageView2.setImageResource(getResources().getIdentifier(
                                "cross", "drawable", "com.example.skadl.studenttest"));
                        stdResult.setTextColor(Color.parseColor("#ff3333"));
                        answerView.setBackgroundColor(Color.parseColor("#ff3333"));
                    }

                    stdResult2.setText(Integer.parseInt(point) * 100 + "Point");
                    userImage.setImageResource(getResources().getIdentifier(
                            "char"+character, "drawable", "com.example.skadl.studenttest"));
                }
            });
        }
    };

    private Emitter.Listener gameStart = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            Blindrace.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    quizId  = arg0[0].toString();
                    type = arg0[1].toString();

                    Log.d("firsttype", type);

                    /*loadingView.setVisibility(View.GONE);
                    essayView.setVisibility(View.GONE);
                    choiceView.setVisibility(View.GONE);*/
                    loadingView.setVisibility(View.GONE);

                    switch (type){
                        case sub:
                            //  주관식으로

                            essayView.setVisibility(View.VISIBLE);
                            Log.d("sub", sub);
                            break;
                        case obj:
                            //  객관식으로
                            choiceView.setVisibility(View.VISIBLE);
                            Log.d("obj", obj);
                            break;
                        default:
                            //  토스트 출력 ㄱㄱ
                            break;
                    }
                }
            });
        }
    };

    private Emitter.Listener raceEnding = new Emitter.Listener() {
        @Override
        public void call(final Object... arg0) {
            Blindrace.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String finalResult = arg0[0].toString();

                    Intent intent = new Intent(Blindrace.this, FinalResult.class);

                    intent.putExtra("session_num", sessionNum);
                    intent.putExtra("Nick_name", nick);
                    intent.putExtra("char", character);
                    intent.putExtra("student_name", stdName);
                    intent.putExtra("result", finalResult);
                    startActivity(intent);
                    finish();
                }
            });
        }
    };

    @Override
    public void onClick(View view) {

        if(R.id.button1 == view.getId()){

            answerNum = "1";
            button.setBackgroundResource(R.drawable.selected1);
            button2.setBackgroundResource(R.drawable.number2);
            button3.setBackgroundResource(R.drawable.number3);
            button4.setBackgroundResource(R.drawable.number4);

        }
        else if(R.id.button2 == view.getId()){

            answerNum = "2";
            button2.setBackgroundResource(R.drawable.selected2);
            button.setBackgroundResource(R.drawable.button);
            button3.setBackgroundResource(R.drawable.number3);
            button4.setBackgroundResource(R.drawable.number4);

        }
        else if(R.id.button3 == view.getId()){

            answerNum = "3";
            button3.setBackgroundResource(R.drawable.selected3);
            button.setBackgroundResource(R.drawable.button);
            button2.setBackgroundResource(R.drawable.number2);
            button4.setBackgroundResource(R.drawable.number4);

        }
        else if(R.id.button4 == view.getId()){

            answerNum = "4";
            button4.setBackgroundResource(R.drawable.selected4);
            button.setBackgroundResource(R.drawable.button);
            button2.setBackgroundResource(R.drawable.number2);
            button3.setBackgroundResource(R.drawable.number3);

        }
        else if(R.id.submit2 == view.getId()){

            answerNum = essay.getText().toString();

            if(answerNum.equals("")){
                Toast.makeText(getApplicationContext(), "正解を入力してください !", Toast.LENGTH_LONG).show();
                return;
            }

            midResultView.setVisibility(View.GONE);
            essayView.setVisibility(View.GONE);
            choiceView.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);

            mSocket.emit("answer", roomNum , answerNum, sessionNum, nick, quizId);
            answerNum = null;
            essay.setText("");

        }
        else if(R.id.submit == view.getId()){

            if(answerNum == null){

                Toast.makeText(getApplicationContext(), "正解を入力してください!", Toast.LENGTH_LONG).show();
                return;
            }

            midResultView.setVisibility(View.GONE);
            essayView.setVisibility(View.GONE);
            choiceView.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);

            button.setBackgroundResource(R.drawable.button);
            button2.setBackgroundResource(R.drawable.number2);
            button3.setBackgroundResource(R.drawable.number3);
            button4.setBackgroundResource(R.drawable.number4);

            mSocket.emit("answer", roomNum , answerNum, sessionNum, nick, quizId);
            answerNum = null;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("leaveRoom", roomNum, sessionNum);

        Log.e("destroy", "destroy");

        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("pause", "pause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e("stop", "stop");
    }
}
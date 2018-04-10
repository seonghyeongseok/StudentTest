package com.example.skadl.studenttest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by skadl on 2018-03-09.
 */

public class Note extends AppCompatActivity implements View.OnClickListener{

    //  정보 받아와서 초기화
    private TextView questName;

    //  버튼 정보
    private Button question, submit;

    //  메인
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);

        //  문제 이름 가지고
        String name = "레이스1";
        questName = (TextView)findViewById(R.id.questName);
        questName.setText(name);

        //  문제 출력하기

        question = (Button)findViewById(R.id.question);
        question.setOnClickListener(this);

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    //  온클릭
    public void onClick(View view) {
        if(view.getId() == R.id.question){
            questionDialog();
        }else if(view.getId() == R.id.submit){
            submitDialog();
        }
    }

    //  질문 버튼 다이알로그
    private void questionDialog(){
        //  다이알로그를 상속받아 따로 액티비티을 만들고
        //  해당 액티비티를 띄우면 다이알로그처럼 보ㅑ인다고 하신다.!
        //
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText questionTitle = new EditText(Note.this);
        final EditText questionBody = new EditText(Note.this);

        questionBody.setLines(5);

        alert.setView(questionTitle);
        alert.setView(questionBody);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String title = questionTitle.getText().toString();
                String body = questionBody.getText().toString();
                //  질문올리기
            }
        });
        alert.show();

    }

    //  제출 버튼 다이알로그
    private void submitDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("알림");
        alert.setMessage("제출하시겠습니까?");

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  제출
            }
        });
        alert.show();
    }
}

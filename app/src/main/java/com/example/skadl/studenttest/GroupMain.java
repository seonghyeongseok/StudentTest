package com.example.skadl.studenttest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by skadl on 2018-03-08.
 */

public class GroupMain extends AppCompatActivity implements View.OnClickListener {

    private String stdName, sessionNum;
    private Button button;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_main);

        Intent getInfo = getIntent();

        stdName     = getInfo.getStringExtra("student_name");
        sessionNum  = getInfo.getStringExtra("session_num");

        ListView listView = (ListView) findViewById(R.id.myGroup);

        String[] className = {"1학년 A반", "1학년 B반", "1학년 C반",
                "2학년 A반", "2학년 B반", "2학년 C반"};

        ArrayList<MyGroupItem> classList = new ArrayList<>();

        for(int i = 0 ; i < className.length ; i++)
        {

            MyGroupItem list = new MyGroupItem();

            list.className = className[i];

            classList.add(list);
            list.onClickListener = this;

        }

        MyGroupAdapter adapter = new MyGroupAdapter(classList);
        listView.setAdapter(adapter);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        //  학생 별 그룹 개수만큼 이미지/반이름 버튼 만들기 -> 메서드작성
        //  버튼은 이후 해당 반에서의 학생 성적조회를 보는 페이지로 이동

    }

    public void onClick(View view) {

        if (view.getId() == R.id.button) {
            dialog();
            return;
        }/*else if(view.)*/
        //  요곤 어떻게..?
        //  반 이름을 가지고 와서 성적조회 페이지로 이동

        View parentView = (View)view.getParent();
        TextView className = (TextView)parentView.findViewById(R.id.class_name);
        //String position = (String)parentView.getTag();

        Intent intent = new Intent(GroupMain.this, GradeRecord.class);

        intent.putExtra("group_name", className.getText().toString());
        intent.putExtra("session_num", sessionNum);
        intent.putExtra("student_name", stdName);
        startActivity(intent);

    }

    private void dialog() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText groupNumText = new EditText(GroupMain.this);

        alert.setTitle("새 그룹 입장");
        alert.setMessage("그룹 번호를 입력해 주세요");
        alert.setView(groupNumText);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  그룹 번호 남기기


                String groupNum = groupNumText.getText().toString();
                //  값 확인 완료


                //  db연결 후 그룹 번호 확인...
            }
        });
        alert.show();
    }
}

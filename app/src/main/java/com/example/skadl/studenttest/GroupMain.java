package com.example.skadl.studenttest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by skadl on 2018-03-08.
 */

public class GroupMain extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        //  학생 별 그룹 개수만큼 이미지/반이름 버튼 만들기 -> 메서드작성
        //  버튼은 이후 해당 반에서의 학생 성적조회를 보는 페이지로 이동

    }

    public void onClick(View view) {

        if (view.getId() == R.id.button) {
            dialog();
        }/*else if(view.)*/
        //  요곤 어떻게..?
        //  반 이름을 가지고 와서 성적조회 페이지로 이동

    }

    private void dialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
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

                //  db연결 후 그룹 번호 확인...
            }
        });
        alert.show();
    }
}

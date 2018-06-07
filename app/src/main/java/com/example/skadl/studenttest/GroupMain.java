package com.example.skadl.studenttest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by skadl on 2018-03-08.
 */

public class GroupMain extends AppCompatActivity implements View.OnClickListener {

    private final String IP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com/mobileStudentGroupsGet";

    private String stdName, sessionNum;
    private Button button;
    private String returnValue;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_main);

        Intent getInfo = getIntent();

        stdName     = getInfo.getStringExtra("student_name");
        sessionNum  = getInfo.getStringExtra("session_num");

        ListView listView = (ListView) findViewById(R.id.myGroup);

        String params;

        params = "sessionId=" + sessionNum;

        try {

            returnValue = new MyAsyncTask(IP).execute(params).get();
            Log.e("dd", returnValue);


        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        ArrayList<MyGroupItem> classList = new ArrayList<>();

        try {

            JSONObject value = new JSONObject(returnValue);

            Boolean check = value.optBoolean("check");
            JSONArray list = value.optJSONArray("groups");

            for(int i = 0 ; i < list.length() ; i++){
                JSONObject groupInfo = list.getJSONObject(i);

                MyGroupItem groupList = new MyGroupItem();

                groupList.className = groupInfo.optString("groupName");
                groupList.retestState = groupInfo.optString("retestStateCount");
                groupList.noteState = groupInfo.optString("wrongStateCount");
                groupList.classId = groupInfo.optString("groupId");

                classList.add(groupList);
                groupList.onClickListener = this;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        MyGroupAdapter adapter = new MyGroupAdapter(classList);
        listView.setAdapter(adapter);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        //button.setText(returnValue);

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
        TextView classId = (TextView)parentView.findViewById(R.id.class_id);
        //String position = (String)parentView.getTag();

        Intent intent = new Intent(GroupMain.this, GradeRecord.class);

        intent.putExtra("group_name", className.getText().toString());
        intent.putExtra("group_id", classId.getText().toString());
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

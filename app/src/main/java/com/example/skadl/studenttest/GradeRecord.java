package com.example.skadl.studenttest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by skadl on 2018-03-08.
 */

public class GradeRecord extends AppCompatActivity implements View.OnClickListener{

    private final String IP = "http://ec2-52-79-176-51.ap-northeast-2.compute.amazonaws.com/mobileGetStudents";

    private String sessionNum, stdName, className, classId, returnValue;
    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_record);

        Intent getInfo = getIntent();

        sessionNum  = getInfo.getStringExtra("session_num");
        stdName     = getInfo.getStringExtra("student_name");
        className   = getInfo.getStringExtra("group_name");
        classId     = getInfo.getStringExtra("group_id");

        String params;

        params = "sessionId=" + sessionNum + "&groupId=" + classId;

        try {

            returnValue = new MyAsyncTask(IP).execute(params).get();
            Log.e("dd", returnValue);


        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        textView = (TextView)findViewById(R.id.student_name);
        textView.setText(className);

        ListView listView = (ListView)findViewById(R.id.grade_list);

        ArrayList<GradeItem> gradeList = new ArrayList<>();

        try {

            JSONObject returnInfo = new JSONObject(returnValue);

            Boolean check = returnInfo.optBoolean("check");
            JSONArray listString = returnInfo.getJSONArray("races");


            Log.e("list", listString.toString());


            for(int i = 0 ; i < listString.length() ; i++){
                String jsonString = listString.getString(i);

                JSONObject gradeInfo = new JSONObject(jsonString);

                GradeItem grade = new GradeItem();

                String month = gradeInfo.optString("month");
                String day = gradeInfo.optString("day");

                String retestState = gradeInfo.optString("retestState");
                String noteState = gradeInfo.optString("wrongState");

                if(retestState.equals("clear")){
                    retestState = "응시완료";
                }else if(retestState.equals("not")){
                    retestState = " ";
                }else if(retestState.equals("order")){
                    retestState = "미응시";
                }

                if(noteState.equals("clear")){
                    noteState = "제출완료";
                }else if(noteState.equals("not")){
                    noteState = " ";
                }else if(noteState.equals("order")){
                    noteState = "미제출";
                }

                float allCount = Integer.parseInt(gradeInfo.optString("allCount"));
                float rightCount = Integer.parseInt(gradeInfo.optString("allRightCount"));

                Log.e("전체 문제", String.valueOf(allCount));
                Log.e("맞춘 문제", String.valueOf(rightCount));
                Log.e("점수", String.valueOf(rightCount/allCount*100));

                grade.date = month + " / " + day;
                grade.quizName = gradeInfo.optString("listName");
                grade.grade = String.valueOf(Math.round(rightCount/allCount*100));
                grade.retest = retestState;
                grade.note = noteState;

                gradeList.add(grade);
                grade.onClickListener = this;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        GradeAdapter adapter = new GradeAdapter(gradeList);
        listView.setAdapter(adapter);

        setChart();

        //  학생별 날짜 퀴즈명 점수 오답노트 재시험응시결과 받아오기
    }

    public void onClick(View view) {

        View parentView = (View)view.getParent();
        TextView quizName = (TextView)parentView.findViewById(R.id.quiz_name);
        String position = (String) parentView.getTag();

        if(view.getId() == R.id.note){

            Intent intent = new Intent(GradeRecord.this, Note.class);
            intent.putExtra("quiz_name", quizName.getText().toString());
            intent.putExtra("session_num", sessionNum);
            intent.putExtra("student_name", stdName);
            startActivity(intent);


        }else if(view.getId() == R.id.retest){

            Intent intent = new Intent(GradeRecord.this, Retest.class);
            intent.putExtra("quiz_name", quizName.getText().toString());
            intent.putExtra("session_num", sessionNum);
            intent.putExtra("student_name", stdName);
            startActivity(intent);

        }
    }

    public void setChart(/* 날짜, 성적*/){

        LineChart lineChart = (LineChart)findViewById(R.id.chart);

        List<Entry> word = new ArrayList<Entry>();
        List<Entry> grammar = new ArrayList<Entry>();
        List<Entry> vocabulary = new ArrayList<Entry>();

        Entry word1 = new Entry(0f, 100f);
        word.add(word1);
        Entry word2 = new Entry(1f, 85f);
        word.add(word2);
        Entry word3 = new Entry(2f, 89f);
        word.add(word3);
        Entry word4 = new Entry(3f, 97f);
        word.add(word4);

        Entry grammar1 = new Entry(0f, 60f);
        grammar.add(grammar1);
        Entry grammar2 = new Entry(1f, 63f);
        grammar.add(grammar2);
        Entry grammar3 = new Entry(2f, 70f);
        grammar.add(grammar3);
        Entry grammar4 = new Entry(3f, 65f);
        grammar.add(grammar4);

        Entry vocabulary1 = new Entry(0f, 90f);
        vocabulary.add(vocabulary1);
        Entry vocabulary2 = new Entry(1f, 87f);
        vocabulary.add(vocabulary2);
        Entry vocabulary3 = new Entry(2f, 85f);
        vocabulary.add(vocabulary3);
        Entry vocabulary4 = new Entry(3f, 90f);
        vocabulary.add(vocabulary4);

        LineDataSet setComp1 = new LineDataSet(word, "단어");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setColor(Color.RED);
        setComp1.setCircleColorHole(Color.RED);
        setComp1.setCircleColor(Color.RED);

        LineDataSet setComp2 = new LineDataSet(grammar, "문법");
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp2.setColor(Color.BLUE);
        setComp2.setCircleColorHole(Color.BLUE);
        setComp2.setCircleColor(Color.BLUE);

        LineDataSet setComp3 = new LineDataSet(vocabulary, "어휘");
        setComp3.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp2.setColor(Color.GREEN);
        setComp2.setCircleColorHole(Color.GREEN);
        setComp2.setCircleColor(Color.GREEN);

        final String[] quarters = new String[] { "2월", "3월", "4월", "5월" };

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }

            public int getDecimalDigits() {  return 0; }
        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);
        dataSets.add(setComp3);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }

}

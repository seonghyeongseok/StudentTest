package com.example.skadl.studenttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by skadl on 2018-05-14.
 */

public class GradeAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<GradeItem> gradeList = null;
    private int listSize = 0;

    public GradeAdapter(ArrayList<GradeItem> list) {
        gradeList = list;
        listSize = list.size();
    }

    @Override
    public int getCount() {
        return listSize;
    }
    //  학생 수

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null)
        {
            final Context context = viewGroup.getContext();

            if (inflater == null)
            {

                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            }

            view = inflater.inflate(R.layout.grade_item, viewGroup, false);
        }

        TextView date = (TextView)view.findViewById(R.id.date);
        TextView quizName = (TextView)view.findViewById(R.id.quiz_name);
        TextView grade = (TextView)view.findViewById(R.id.grade);
        TextView note = (TextView)view.findViewById(R.id.note);
        TextView retest = (TextView)view.findViewById(R.id.retest);

        note.setOnClickListener(gradeList.get(i).onClickListener);
        retest.setOnClickListener(gradeList.get(i).onClickListener);

        date.setText(gradeList.get(i).date);
        quizName.setText(gradeList.get(i).quizName);
        grade.setText(gradeList.get(i).grade);
        note.setText(gradeList.get(i).note);
        retest.setText(gradeList.get(i).retest);

        view.setTag(""+i);

        return view;
    }
}

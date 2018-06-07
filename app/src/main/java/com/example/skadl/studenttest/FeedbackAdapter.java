package com.example.skadl.studenttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by skadl on 2018-06-04.
 */

public class FeedbackAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<FeedbackItem> FeedbackList = null;
    private int listSize = 0;

    public FeedbackAdapter(ArrayList<FeedbackItem> list) {
        FeedbackList = list;
        listSize = list.size();
    }

    @Override
    public int getCount() {
        return 0;
    }

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
        if (view == null) {
            final Context context = viewGroup.getContext();

            if (inflater == null) {

                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            }

            view = inflater.inflate(R.layout.grade_item, viewGroup, false);
        }

        TextView date = (TextView) view.findViewById(R.id.date);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView reply = (TextView) view.findViewById(R.id.reply);

        date.setText(FeedbackList.get(i).Date);
        title.setText(FeedbackList.get(i).Title);
        reply.setText(FeedbackList.get(i).Reply);

        title.setOnClickListener(FeedbackList.get(i).onClickListener);

        return view;
    }
}

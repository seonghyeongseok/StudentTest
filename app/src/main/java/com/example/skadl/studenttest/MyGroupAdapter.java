package com.example.skadl.studenttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by skadl on 2018-05-14.
 */

public class MyGroupAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<MyGroupItem> classList = null;
    private int listSize = 0;

    public MyGroupAdapter(ArrayList<MyGroupItem> list) {
        classList = list;
        listSize = list.size();
    }

    @Override
    public int getCount() {
        return listSize;
    }
    //  담당 클래스 개수

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
            view = inflater.inflate(R.layout.group_main_item, viewGroup, false);
        }

        TextView className = (TextView)view.findViewById(R.id.class_name);
        ImageButton classImage = (ImageButton)view.findViewById(R.id.imageButton);

        className.setText(classList.get(i).className);
        classImage.setOnClickListener(classList.get(i).onClickListener);
        className.setOnClickListener(classList.get(i).onClickListener);

        view.setTag(""+i);
        return view;
    }
}

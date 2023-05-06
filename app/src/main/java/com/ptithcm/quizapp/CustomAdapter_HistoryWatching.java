package com.ptithcm.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter_HistoryWatching extends ArrayAdapter<String> {
    Context context;

    int resource;

    ArrayList<String> data;

    ArrayList<String> data_temp = new ArrayList<>();

    public CustomAdapter_HistoryWatching(@NonNull Context context,
                                         int resource,
                                         @NonNull ArrayList<String> data){
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        data_temp.addAll(data);
    }

    private class ViewHolder{
        TextView testDateTv;
        Button resultBtn;
        public ViewHolder(View view){
            testDateTv = view.findViewById(R.id.testDateTv);
            resultBtn = view.findViewById(R.id.resultBtn);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        String testDate = data.get(position);
        viewHolder.testDateTv.setText(testDate);
        viewHolder.testDateTv.setTextColor(Color.WHITE);
        return convertView;
    }
}

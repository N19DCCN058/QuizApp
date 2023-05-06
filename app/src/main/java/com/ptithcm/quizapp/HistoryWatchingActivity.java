package com.ptithcm.quizapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryWatchingActivity extends AppCompatActivity {
    ListView testLv;
    ArrayList<String> testData = new ArrayList<>();
    CustomAdapter_HistoryWatching adapter_historyWatching;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_watching);
        setControl();
        setEvent();
    }

    private void setEvent() {
        Declare();
        adapter_historyWatching = new CustomAdapter_HistoryWatching(this, R.layout.history_showing, testData);
        testLv.setAdapter(adapter_historyWatching);
    }

    private void Declare() {
        testData.add(new String("Ngay 20/04/2023 15:15:15"));
        testData.add(new String("Ngay 21/04/2023 15:15:15"));
        testData.add(new String("Ngay 22/04/2023 15:15:15"));
        testData.add(new String("Ngay 23/04/2023 15:15:15"));
    }

    private void setControl() {
        testLv = findViewById(R.id.testLv);
    }
}
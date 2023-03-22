package com.ptithcm.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {

    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Welcome.this, LoginActivity.class));
            }
        },2000);
    }
}
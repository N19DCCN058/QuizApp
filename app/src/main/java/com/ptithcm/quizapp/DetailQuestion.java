package com.ptithcm.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailQuestion extends AppCompatActivity {
    private Toolbar toolbar;
    FloatingActionButton fabtbSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_detail_question);
        Bundle bundle = getIntent().getExtras();
        question qs = (question) bundle.get("obj_Question");
        setControl();
        setEvent();
    }

    private void setEvent() {
        fabtbSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailQuestion.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailQuestion.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbarAddNew);
        fabtbSave = findViewById(R.id.fabtbSave);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_detail_qs,menu);
        return true;
    }
}
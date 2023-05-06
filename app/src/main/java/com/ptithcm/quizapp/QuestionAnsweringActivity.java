package com.ptithcm.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnsweringActivity extends AppCompatActivity {
    private List<Object> objects = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answering);
        setControl();
    }

    private void setControl() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String a = "Anh ___ em ___ghjkhj";
        String b = "____ alsfla ____ aksf";
        BlankQuestion blankQuestion = new BlankQuestion(a);
        BlankQuestion blankQuestionb = new BlankQuestion(b);
        objects.add(new QuestionOnlyOneAnswer("abc","abc","abc","abc","abc"));
        objects.add(blankQuestion);
        objects.add(blankQuestionb);
        objects.add(new QuestionOnlyOneAnswer("abc","abc","abc","abc","abc"));

        int index = 1;
        for (Object object: objects) {
            if (object instanceof QuestionOnlyOneAnswer){
                ((QuestionOnlyOneAnswer) object).setOrderNumber(Integer.toString(index));
            }
            else{
                ((BlankQuestion) object).setOrderNumber(Integer.toString(index));
            }
            index++;
        }

        // create the custom adapter for the ListView
        MultiAdapter adapter = new MultiAdapter(this, objects);
        ListView listView = findViewById(R.id.questionLv);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_submit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitBtn_tb:
                break;
            case android.R.id.home:
                Intent myIntent = new Intent(getApplicationContext(), SelectedLevelActivity.class);
                startActivityForResult(myIntent, 0);
        }
        return true;
    }
}
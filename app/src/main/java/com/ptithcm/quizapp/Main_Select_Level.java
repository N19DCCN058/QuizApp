package com.ptithcm.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.quizapp.adapter.CustomAdapterLevel;
import com.ptithcm.quizapp.database.DBHelper;
import com.ptithcm.quizapp.model.Level;

import java.util.ArrayList;

public class Main_Select_Level extends AppCompatActivity {

    private TextInputEditText edtContentDialog;
    AlertDialog dialog;
    private Button btnSubmitDialog;
    private FloatingActionButton fabtbAddLevlel;
    private RecyclerView rvListLV;
    private DBHelper dbHelper = new DBHelper(this);
    private String receivedType;
    private CustomAdapterLevel customAdapterLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_select_level);
        receivedType= getIntent().getStringExtra("mType");
        setControl();
        setEvent();
        khoiTao();
    }

    private void setControl() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvListLV = findViewById(R.id.rvListLV);
        fabtbAddLevlel = findViewById(R.id.fabtbAddLevlel);
    }

    private void setEvent() {
        fabtbAddLevlel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void khoiTao() {
        switch (Integer.parseInt(receivedType)) {
            case 0:
                getSupportActionBar().setTitle("Multiple-choice");
                break;
            case 1:
                getSupportActionBar().setTitle("Sorting");
                break;
            case 2:
                getSupportActionBar().setTitle("Fill-in-the-blank");
                break;
            case 3:
                getSupportActionBar().setTitle("Vocabulary");
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Main_Select_Level.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_get_content_question, null);
        edtContentDialog = view.findViewById(R.id.edtContentDialog);
        btnSubmitDialog = view.findViewById(R.id.btnSubmitDialog);
        btnSubmitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHelper.addLevel(receivedType,edtContentDialog.getText().toString())) {
                    dialog.dismiss();
                    edtContentDialog.setText("");
                    ArrayList<Level> list = dbHelper.getLevelByType(receivedType);
                    customAdapterLevel = new CustomAdapterLevel(Main_Select_Level.this, list);
                    rvListLV.setAdapter(customAdapterLevel);
                    Toast.makeText(Main_Select_Level.this, "Success", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Main_Select_Level.this, "Level already exist", Toast.LENGTH_SHORT).show();
                }


            }
        });
        builder.setView(view);
        dialog = builder.create();
        ArrayList<Level> list = dbHelper.getLevelByType(receivedType);
        customAdapterLevel = new CustomAdapterLevel(this, list);
        rvListLV.setAdapter(customAdapterLevel);
        customAdapterLevel.notifyDataSetChanged();
    }
    private void back() {
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


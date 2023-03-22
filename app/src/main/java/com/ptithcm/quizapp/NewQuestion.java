package com.ptithcm.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class NewQuestion extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText edtContentDialog;
    Button btnSubmitDialog;
    EditText edtContent;
    Spinner spnLever,spnStyle;
    ImageView imgImage,imgAddImage,imgDeleteImage,imgAddRowAnswer,imgDeleteRowAnswer;
    LinearLayout llListAnswer;
    FloatingActionButton fabtbSave;
    AlertDialog dialog;
    RadioGroup radioGroup;
    RadioButton rbtnCorrect1, rbtnCorrect2, rbtnCorrect3, rbtnCorrect4, rbtnCorrect5, rbtnCorrect6;
    RadioButton[] rbtn = new RadioButton[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_detail_question);

        setControl();
        setEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_detail_qs,menu);
        return true;
    }
    private void setEvent() {
        imgAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery,1000);
            }
        });
        imgDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDeleteImage.setVisibility(View.GONE);
                imgImage.setVisibility(View.GONE);
                imgImage.setImageURI(null);
            }
        });
        imgAddRowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llListAnswer.getChildCount() >= 6) {
                    Toast.makeText(NewQuestion.this, "The maximum number of answers is 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                View newAnswer = getLayoutInflater().inflate(R.layout.item_answer,null,false);
                EditText edtAnswer = newAnswer.findViewById(R.id.edtAnswer);
                ImageView imgDeleteRowAnswer = newAnswer.findViewById(R.id.imgDeleteRowAnswer);
                imgDeleteRowAnswer.setVisibility(View.VISIBLE);
                llListAnswer.addView(newAnswer);
                rbtn[llListAnswer.getChildCount() ].setVisibility(View.VISIBLE);
                imgDeleteRowAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = llListAnswer.indexOfChild(newAnswer) + 1;
                        int checked_position = 1;
                        switch ( radioGroup.getCheckedRadioButtonId()){
                            case R.id.rbtnCorrect1:
                                checked_position = 1;
                                break;
                            case R.id.rbtnCorrect2:
                                checked_position = 2;
                                break;
                            case R.id.rbtnCorrect3:
                                checked_position = 3;
                                break;
                            case R.id.rbtnCorrect4:
                                checked_position = 4;
                                break;
                            case R.id.rbtnCorrect5:
                                checked_position = 5;
                                break;
                            case R.id.rbtnCorrect6: checked_position = 6;
                        }
                        if(rbtn[position].isChecked())
                        {
                            Toast.makeText(NewQuestion.this, "Can not delete!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(position < checked_position){
                                rbtn[checked_position -1].setChecked(true);
                            }
                            rbtn[llListAnswer.getChildCount()].setVisibility(View.GONE);
                            llListAnswer.removeView(newAnswer);
                        }

                    }
                });
            }
        });
        fabtbSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewQuestion.this, "Created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewQuestion.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        edtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContentDialog.setText(edtContent.getText());
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgImage.setImageURI(data.getData());
        imgDeleteImage.setVisibility(View.VISIBLE);
        imgImage.setVisibility(View.VISIBLE);
    }

    private void setControl() {

        toolbar = findViewById(R.id.toolbarAddNew);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spnLever = findViewById(R.id.spnLever);
        spnStyle = findViewById(R.id.spnStyle);
        imgImage = findViewById(R.id.imgImage);
        imgAddImage = findViewById(R.id.imgAddImage);
        imgDeleteImage = findViewById(R.id.imgDeleteImage);
        imgAddRowAnswer = findViewById(R.id.imgAddRowAnswer);
        imgDeleteRowAnswer = findViewById(R.id.imgDeleteRowAnswer);
        llListAnswer = findViewById(R.id.llListAnswer);
        fabtbSave = findViewById(R.id.fabtbSave);
        edtContent = findViewById(R.id.edtContent);
        rbtnCorrect1 = findViewById(R.id.rbtnCorrect1);
        rbtnCorrect1.setChecked(true);
        rbtnCorrect2 = findViewById(R.id.rbtnCorrect2);
        rbtnCorrect3 = findViewById(R.id.rbtnCorrect3);
        rbtnCorrect4 = findViewById(R.id.rbtnCorrect4);
        rbtnCorrect5 = findViewById(R.id.rbtnCorrect5);
        rbtnCorrect6 = findViewById(R.id.rbtnCorrect6);
        radioGroup = findViewById(R.id.radioGroup);
        rbtn[1] = rbtnCorrect1;
        rbtn[2] = rbtnCorrect2;
        rbtn[3] = rbtnCorrect3;
        rbtn[4] = rbtnCorrect4;
        rbtn[5] = rbtnCorrect5;
        rbtn[6] = rbtnCorrect6;

        AlertDialog.Builder builder = new AlertDialog.Builder(NewQuestion.this);
        View view = getLayoutInflater().inflate(R.layout.content_qs_dialog,null);
        edtContentDialog = view.findViewById(R.id.edtContentDialog);
        btnSubmitDialog = view.findViewById(R.id.btnSubmitDialog);
        btnSubmitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContent.setText(edtContentDialog.getText());
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnReload_tb:
                edtContent.setText("");
                spnLever.setSelection(0);
                spnStyle.setSelection(0);
                llListAnswer.removeAllViews();
                imgDeleteImage.setVisibility(View.GONE);
                imgImage.setVisibility(View.GONE);
                imgImage.setImageURI(null);
        }
        return super.onOptionsItemSelected(item);
    }
}
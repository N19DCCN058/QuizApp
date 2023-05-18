package com.ptithcm.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.quizapp.adapter.CustomAdapterQuestion;
import com.ptithcm.quizapp.database.DBHelper;
import com.ptithcm.quizapp.model.Answer;
import com.ptithcm.quizapp.model.Level;
import com.ptithcm.quizapp.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main_Select_Level_Question_Add_New extends AppCompatActivity {
    AlertDialog dialogContent,dialogHelp;
    TextView tvHelp;
    TextInputEditText edtContentDialog;
    Button btnSubmitDialog, btnCloseDialog;
    EditText edtContent;
    ImageView imgImage, imgAddImage, imgDeleteImage, imgAddRowAnswer, imgDeleteRowAnswer;
    LinearLayout llListAnswer;
    FloatingActionButton fabtbSave;
    RadioGroup radioGroup;
    RadioButton rbtnCorrect1, rbtnCorrect2, rbtnCorrect3, rbtnCorrect4, rbtnCorrect5, rbtnCorrect6;
    RadioButton[] rbtn = new RadioButton[7];
    ProgressBar proLoading;
    String receivedID;
    ArrayList<Answer> ans = new ArrayList<>();
    DBHelper dbHelper;
    Level lv;

    int checked_position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        receivedID = getIntent().getStringExtra("mIDLevel");
        setContentView(R.layout.main_select_level_question_detail_or_new);
        khoiTao();
        setControl();
        setEvent();
    }

    private void khoiTao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_help_for_question, null);
        tvHelp = view.findViewById(R.id.tvHelp);
        btnCloseDialog = view.findViewById(R.id.btnCloseDialog);
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHelp.dismiss();
            }
        });
        builder.setView(view);
        dialogHelp = builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_detail_question_or_addnew, menu);
        return true;
    }

    private void setEvent() {
        lv = dbHelper.getLevelByID(receivedID);
        getSupportActionBar().setTitle(lv.getTitle());
        switch (lv.getType()) {
            case 0:
                tvHelp.setText("A multiple-choice question is a type of question that presents a statement or a question followed by a list of options from which the test-taker must choose the correct answer. The correct answer is usually denoted by a letter or a number. Multiple-choice questions are commonly used in exams and assessments to test a person's knowledge, understanding, or comprehension of a particular subject or topic. They are an effective way to measure learning outcomes and can cover a wide range of topics, from factual knowledge to higher-order thinking skills.");
                break;
            case 1:
                tvHelp.setText("A sorting question is a type of question that requires the test-taker to arrange a set of items or information in a specific order according to a given criterion or rule. The items may be presented in a list or a table, and the criterion for sorting could be alphabetical order, numerical order, chronological order, or any other logical sequence. Sorting questions can help assess a person's ability to organize and categorize information, as well as their attention to detail and accuracy. They are commonly used in assessments, tests, and surveys to evaluate a person's cognitive and analytical skills.");
                radioGroup.setVisibility(View.GONE);
                imgAddRowAnswer.setVisibility(View.GONE);
                llListAnswer.setVisibility(View.GONE);
                break;
            case 2:
                tvHelp.setText("A fill-in-the-blank question is a type of question that presents a statement or a sentence with one or more missing words or phrases. The test-taker is required to fill in the blank(s) with the correct word or phrase that completes the sentence and makes it grammatically and semantically correct. Fill-in-the-blank questions can range from simple one-word answers to more complex responses requiring multiple words or even full sentences. They are commonly used in language assessments and tests to evaluate a person's understanding of grammar, vocabulary, and syntax.");
                imgAddRowAnswer.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                break;
            case 3:
                tvHelp.setText("A vocabulary question is a type of question that tests a person's knowledge and understanding of words and their meanings. It may present a word or a phrase, and the test-taker is required to select the correct definition or synonym from a list of options. Vocabulary questions can also require the test-taker to use a word or phrase in context by completing a sentence or providing the appropriate word for a given definition. Such questions are used to evaluate a person's vocabulary and comprehension skills, which are important for effective communication and reading comprehension.");
                imgAddRowAnswer.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                break;

        }
        imgDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDeleteImage.setVisibility(View.GONE);
                imgImage.setVisibility(View.GONE);
                imgImage.setImageURI(null);
            }
        });
        imgAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, 1000);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
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
                    case R.id.rbtnCorrect6:
                        checked_position = 6;
                }
            }
        });
        imgAddRowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llListAnswer.getChildCount() >= 6) {
                    Toast.makeText(Main_Select_Level_Question_Add_New.this, "The maximum number of answers is 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                View newAnswer = getLayoutInflater().inflate(R.layout.item_answer, null, false);
                EditText edtAnswer = newAnswer.findViewById(R.id.edtAnswer);
                ImageView imgDeleteRowAnswer = newAnswer.findViewById(R.id.imgDeleteRowAnswer);
                imgDeleteRowAnswer.setVisibility(View.VISIBLE);
                llListAnswer.addView(newAnswer);
                rbtn[llListAnswer.getChildCount()].setVisibility(View.VISIBLE);
                imgDeleteRowAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = llListAnswer.indexOfChild(newAnswer) + 1;
                        if (rbtn[position].isChecked()) {
                            Toast.makeText(Main_Select_Level_Question_Add_New.this, "Can not delete!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (position < checked_position) {
                                rbtn[checked_position - 1].setChecked(true);
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
                Question qs = new Question();
                qs.setQuestionLevel(String.valueOf(lv.getId()));
                CustomAdapterQuestion adapterQuestion = new CustomAdapterQuestion(Main_Select_Level_Question_Add_New.this, null);

                getAnswers();
                if (edtContent.getText().toString().equals("")) {
                    edtContent.setError("");
                }
                for (Answer answer : ans) {
                    if(answer.getAnswerContent().equals("") && lv.getType() != 1 ) {
                        adapterQuestion.showErrorDialog("Content cannot be left blank!");
                        return;
                    }
                }
                String qsContent, qsAnswer;
                Bitmap qsImage;
                qsContent = edtContent.getText().toString().trim();
                switch (lv.getType()) {
                    case 0:
                        break;
                    case 1:
                        if (imgImage.getVisibility() == View.VISIBLE) {
                            qsImage = ((BitmapDrawable) imgImage.getDrawable()).getBitmap();
                            qs.setQuestionImage(qsImage);
                        } else {
                            qs.setQuestionImage(null);
                        }
                        List<String> a = Arrays.asList(qsContent.split("/"));
                        StringBuilder str = new StringBuilder();
                        for (String l : a) {
                            if (!l.trim().isEmpty()) {
                                str.append(l.trim() + "/");
                            }
                        }
                        a = Arrays.asList(str.toString().split("/"));
                        if (a.size() < 3) {
                            edtContent.setError("Error");
                            Toast.makeText(Main_Select_Level_Question_Add_New.this, "Content can be wrong", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            qsContent = str.toString();
                        }
                        qs.setQuestionContent(qsContent);
                        if(dbHelper.addQuestion(qs)) {
                            Toast.makeText(Main_Select_Level_Question_Add_New.this, "Success", Toast.LENGTH_SHORT).show();
                            back();
                            return;
                        }
                        break;


                    case 2:
                        if (imgImage.getVisibility() == View.VISIBLE) {
                            qsImage = ((BitmapDrawable) imgImage.getDrawable()).getBitmap();
                            qs.setQuestionImage(qsImage);
                        } else {
                            qs.setQuestionImage(null);
                        }
                        qsAnswer = ans.get(0).getAnswerContent().trim();
                        StringBuilder s = new StringBuilder();
                        int count = 0;
                        for (int i = 0; i < qsContent.length(); i++) {
                            if (qsContent.charAt(i) == '_') {
                                if (i + 1 < qsContent.length()) {
                                    if (qsContent.charAt(i + 1) == '_') {
                                        s.append(' ');
                                    }
                                }
                                count++;
                            }
                            s.append(qsContent.charAt(i));
                        }
                        if (count != qsAnswer.length()) {
                            Toast.makeText(Main_Select_Level_Question_Add_New.this, "Incompatible question and answer", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        qs.setQuestionContent(s.toString());
                        qs.setExactAnswer(qsAnswer);
                        if(dbHelper.addQuestion(qs)) {
                            Toast.makeText(Main_Select_Level_Question_Add_New.this, "Success", Toast.LENGTH_SHORT).show();
                            back();
                            return;
                        }
                        break;
                    case 3:
                        if (imgImage.getVisibility() == View.VISIBLE) {
                            qsImage = ((BitmapDrawable) imgImage.getDrawable()).getBitmap();
                            qs.setQuestionImage(qsImage);
                        } else {
                            qs.setQuestionImage(null);
                        }
                        qsAnswer = ans.get(0).getAnswerContent().trim();
                        qs.setQuestionContent(edtContent.getText().toString().trim());
                        qs.setExactAnswer(qsAnswer);
                        dbHelper.addQuestion(qs);
                        Toast.makeText(Main_Select_Level_Question_Add_New.this, "Success", Toast.LENGTH_SHORT).show();
                        back();
                        return;
                }
                qsAnswer = String.valueOf(checked_position);
                try {
                    qsImage = ((BitmapDrawable) imgImage.getDrawable()).getBitmap();
                    qs = new Question(null, qsContent, String.valueOf(lv.getId()), qsAnswer, qsImage);
                } catch (Exception e) {
                    qs = new Question(null, qsContent, String.valueOf(lv.getId()), qsAnswer, null);
                }

                if (dbHelper.addQuestion_Answer(qs,ans)) {
                    back();
                } else {
                    adapterQuestion.showErrorDialog("Error!");
                }

            }
        });
        edtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContentDialog.setText(edtContent.getText());
                dialogContent.show();
            }
        });
    }
    private void back() {
        Intent intent = new Intent(Main_Select_Level_Question_Add_New.this, Main_Select_Level_Question.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mIDLevel", String.valueOf(lv.getId()));
        startActivity(intent);
        finishAffinity();
    }
    private void getAnswers() {
        ans.clear();
        for (int i = 0; i < llListAnswer.getChildCount(); i++) {
            View v = llListAnswer.getChildAt(i);
            EditText editText = v.findViewById(R.id.edtAnswer);
            if(editText.getText().toString().equals("")) {
                editText.setError("Can be not null");
            }
            ans.add(new Answer(null,editText.getText().toString()));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            imgImage.setImageURI(data.getData());
        } catch (NullPointerException e) {
            if (imgImage.getVisibility() != View.VISIBLE) {
                return;
            }
        }
        imgDeleteImage.setVisibility(View.VISIBLE);
        imgAddImage.setVisibility(View.INVISIBLE);
        imgImage.setVisibility(View.VISIBLE);
    }

    private void setControl() {
        dbHelper = new DBHelper(Main_Select_Level_Question_Add_New.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        proLoading = findViewById(R.id.proLoading);
        rbtn[1] = rbtnCorrect1;
        rbtn[2] = rbtnCorrect2;
        rbtn[3] = rbtnCorrect3;
        rbtn[4] = rbtnCorrect4;
        rbtn[5] = rbtnCorrect5;
        rbtn[6] = rbtnCorrect6;

        AlertDialog.Builder builder = new AlertDialog.Builder(Main_Select_Level_Question_Add_New.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_get_content_question, null);
        edtContentDialog = view.findViewById(R.id.edtContentDialog);
        btnSubmitDialog = view.findViewById(R.id.btnSubmitDialog);
        btnSubmitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContent.setText(edtContentDialog.getText());
                dialogContent.dismiss();
            }
        });
        builder.setView(view);
        dialogContent = builder.create();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, Main_Select_Level_Question.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mIDLevel", receivedID);
                startActivity(intent);
                finishAffinity();
            case R.id.btnReload_tb:
                proLoading.setVisibility(View.VISIBLE);
                proLoading.setProgress(0);
                CountDownTimer countDownTimer = new CountDownTimer(1000,500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int val = proLoading.getProgress() + 10;
                        proLoading.setProgress(val);
                    }

                    @Override
                    public void onFinish() {
                        edtContent.setText("");
                        for (int i = 2; i <= llListAnswer.getChildCount(); i++) {
                            rbtn[i].setVisibility(View.INVISIBLE);
                        }
                        llListAnswer.removeViewsInLayout(1, llListAnswer.getChildCount() - 1);
                        imgDeleteImage.setVisibility(View.GONE);
                        imgImage.setVisibility(View.GONE);
                        imgImage.setImageURI(null);
                        proLoading.setVisibility(View.GONE);
                    }
                }.start();
        }
        return super.onOptionsItemSelected(item);
    }
}
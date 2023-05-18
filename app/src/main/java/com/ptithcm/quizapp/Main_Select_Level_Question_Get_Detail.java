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

public class Main_Select_Level_Question_Get_Detail extends AppCompatActivity {
    AlertDialog dialogHelp;
    TextView tvHelp;
    TextInputEditText edtContentDialog;
    Button btnSubmitDialog, btnCloseDialog;
    EditText edtContent;
    ImageView imgImage, imgAddImage, imgDeleteImage, imgAddRowAnswer, imgDeleteRowAnswer;
    LinearLayout llListAnswer;
    private List<View> rowViewList = new ArrayList<View>();
    FloatingActionButton fabtbSave;
    AlertDialog dialog;
    RadioGroup radioGroup;
    RadioButton rbtnCorrect1, rbtnCorrect2, rbtnCorrect3, rbtnCorrect4, rbtnCorrect5, rbtnCorrect6;
    RadioButton[] rbtn = new RadioButton[7];
    ProgressBar proLoading;
    Level lv = new Level();
    int checked_position = 1;
    ArrayList<Answer> ans = new ArrayList<>();

    Question qs;
    ArrayList<Answer> answers;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_select_level_question_detail_or_new);
        Bundle bundle = getIntent().getExtras();
        String qsID = (String) bundle.get("questionID");
        dbHelper = new DBHelper(Main_Select_Level_Question_Get_Detail.this);

        khoiTao();
        setControl();
        setEvent(qsID);
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

    private void setEvent(String qsID) {
        qs = dbHelper.getQuestionByID(qsID);
        lv = dbHelper.getLevelByID(qs.getQuestionLevel());
        getSupportActionBar().setTitle(lv.getTitle());
        switch (lv.getType()) {
            case 0:
                tvHelp.setText(Main_Select_Level_Question_Get_Detail.this.getResources().getString(R.string.multiple_choice));
                break;
            case 1:
                tvHelp.setText(Main_Select_Level_Question_Get_Detail.this.getResources().getString(R.string.sorting));
                radioGroup.setVisibility(View.GONE);
                imgAddRowAnswer.setVisibility(View.GONE);
                llListAnswer.setVisibility(View.GONE);
                break;
            case 2:
                tvHelp.setText(Main_Select_Level_Question_Get_Detail.this.getResources().getString(R.string.fill_in));
                imgAddRowAnswer.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                break;
            case 3:
                tvHelp.setText(Main_Select_Level_Question_Get_Detail.this.getResources().getString(R.string.vocabulary));
                imgAddRowAnswer.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                break;

        }
        qs.getQuestionID();
        if (lv.getType() == 0) {
            answers = dbHelper.getAnswersByQuestionID(qsID);
        }
        setData(qs, answers);
        fabtbSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAdapterQuestion adapterQuestion = new CustomAdapterQuestion(Main_Select_Level_Question_Get_Detail.this, null);

                getAnswers();
                if (edtContent.getText().toString().equals("")) {
                    adapterQuestion.showErrorDialog("Content cannot be left blank!");
                    edtContent.setError("");
                    return;
                }
                for (Answer answer : ans) {
                    if (answer.getAnswerContent().equals("") && lv.getType() != 1) {
                        adapterQuestion.showErrorDialog("Content cannot be left blank!");
                        return;
                    }
                }
                String qsLevel, qsContent, qsAnswer;
                Bitmap qsImage;
                qsContent = edtContent.getText().toString().trim();
                DBHelper dbHelper = new DBHelper(Main_Select_Level_Question_Get_Detail.this);
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
                            Toast.makeText(Main_Select_Level_Question_Get_Detail.this, "Content can be wrong", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            qsContent = str.toString();
                        }
                        qs.setQuestionContent(qsContent);
                        dbHelper.updateContentQuestion(qs);
                        Toast.makeText(Main_Select_Level_Question_Get_Detail.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        back();
                        return;
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
                            Toast.makeText(Main_Select_Level_Question_Get_Detail.this, "Incompatible question and answer", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        qs.setQuestionContent(s.toString());
                        qs.setExactAnswer(qsAnswer);
                        dbHelper.updateContentQuestion(qs);
                        back();
                        return;
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
                        dbHelper.updateContentQuestion(qs);
                        back();
                        return;
                }
                qsLevel = String.valueOf(lv.getId());
                qsAnswer = String.valueOf(checked_position);
                Question qs = new Question();
                if (imgImage.getVisibility() == View.INVISIBLE || imgImage.getVisibility() == View.GONE) {
                    qs = new Question(qsID, qsContent, qsLevel, qsAnswer, null);
                } else {
                    qsImage = ((BitmapDrawable) imgImage.getDrawable()).getBitmap();
                    qs = new Question(qsID, qsContent, qsLevel, qsAnswer, qsImage);
                }

                if (dbHelper.updateQuestion_Answer(qs, ans)) {
                    Toast.makeText(Main_Select_Level_Question_Get_Detail.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    back();
                } else {
                    adapterQuestion.showErrorDialog("Error!");
                }

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
        edtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContentDialog.setText(edtContent.getText());
                dialog.show();
            }
        });
        imgDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAddImage.setVisibility(View.VISIBLE);
                imgDeleteImage.setVisibility(View.GONE);
                imgImage.setVisibility(View.GONE);
                imgImage.setImageURI(null);
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
                    Toast.makeText(Main_Select_Level_Question_Get_Detail.this, "The maximum number of answers is 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                View newAnswer = getLayoutInflater().inflate(R.layout.item_answer, null, false);
                EditText edtAnswer = newAnswer.findViewById(R.id.edtAnswer);
                ImageView imgDeleteRowAnswer = newAnswer.findViewById(R.id.imgDeleteRowAnswer);
                imgDeleteRowAnswer.setVisibility(View.VISIBLE);
                llListAnswer.addView(newAnswer);
                rowViewList.add(newAnswer);
                rbtn[llListAnswer.getChildCount()].setVisibility(View.VISIBLE);
                imgDeleteRowAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = llListAnswer.indexOfChild(newAnswer) + 1;
                        if (rbtn[position].isChecked()) {
                            Toast.makeText(Main_Select_Level_Question_Get_Detail.this, "Can not delete!", Toast.LENGTH_SHORT).show();
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
    }

    private void back() {
        Intent intent = new Intent(Main_Select_Level_Question_Get_Detail.this, Main_Select_Level_Question.class);
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
            if (editText.getText().toString().equals("")) {
                editText.setError("");
            }
            ans.add(new Answer(null, editText.getText().toString()));
        }
    }

    private void setData(Question qs, ArrayList<Answer> answers) {
        edtContent.setText(qs.getQuestionContent());
        if (qs.getQuestionImage() != null) {
            imgImage.setImageBitmap(qs.getQuestionImage());
            imgImage.setVisibility(View.VISIBLE);
            imgDeleteImage.setVisibility(View.VISIBLE);
            imgAddImage.setVisibility(View.INVISIBLE);
        }
        View v = llListAnswer.getChildAt(0);
        EditText edtAnswer = v.findViewById(R.id.edtAnswer);
        if (lv.getType() == 0) {
            edtAnswer.setText(answers.get(0).getAnswerContent());
        } else if (lv.getType() == 2 || lv.getType() == 3) {
            edtAnswer.setText(qs.getExactAnswer());
            return;
        } else {
            return;
        }
        checked_position = 1;
        int i = 2;
        for (Answer ans : answers.subList(1, answers.size())) {
            if (String.valueOf(i).equals(qs.getExactAnswer())) {
                rbtn[i].setChecked(true);
                checked_position = i;
            } else i++;
            View newAnswer = getLayoutInflater().inflate(R.layout.item_answer, null, false);
            edtAnswer = newAnswer.findViewById(R.id.edtAnswer);
            edtAnswer.setText(ans.getAnswerContent());
            ImageView imgDeleteRowAnswer = newAnswer.findViewById(R.id.imgDeleteRowAnswer);
            imgDeleteRowAnswer.setVisibility(View.VISIBLE);
            llListAnswer.addView(newAnswer);
            rowViewList.add(newAnswer);
            rbtn[llListAnswer.getChildCount()].setVisibility(View.VISIBLE);
            imgDeleteRowAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = llListAnswer.indexOfChild(newAnswer) + 1;
                    if (rbtn[position].isChecked()) {
                        Toast.makeText(Main_Select_Level_Question_Get_Detail.this, "Can't delete correct answer!", Toast.LENGTH_SHORT).show();
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

    }

    private void setControl() {
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


        AlertDialog.Builder builder = new AlertDialog.Builder(Main_Select_Level_Question_Get_Detail.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_get_content_question, null);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_detail_question_or_addnew, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
                break;
            case R.id.btnHelp:
                dialogHelp.show();
                break;
            case R.id.btnReload_tb:
                proLoading.setVisibility(View.VISIBLE);
                proLoading.setProgress(0);
                CountDownTimer countDownTimer = new CountDownTimer(1000, 500) {
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
                        setData(qs, answers);
                        proLoading.setVisibility(View.GONE);
                    }
                }.start();
        }
        return super.onOptionsItemSelected(item);
    }

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
}
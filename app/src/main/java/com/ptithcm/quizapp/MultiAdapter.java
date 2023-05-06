package com.ptithcm.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

public class MultiAdapter extends BaseAdapter {

    private static final int TYPE_BLANK_QUESTION = 0;
    private static final int TYPE_QUESTION_ONLY_ONE_ANSWER = 1;
    private Context context;
    private List<Object> mData;

    public MultiAdapter(Context context, List<Object> data) {
        this.context = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (item instanceof BlankQuestion) {
            return TYPE_BLANK_QUESTION;
        } else if (item instanceof QuestionOnlyOneAnswer) {
            return TYPE_QUESTION_ONLY_ONE_ANSWER;
        } else {
            throw new IllegalArgumentException("Invalid object type");
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2; // the number of different types of views in the ListView
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        Object item = getItem(position);
        // create and return the appropriate view for each type of item
        switch (type) {
            case TYPE_BLANK_QUESTION:
                if (convertView == null) {
                    convertView = LayoutInflater.
                            from(context).
                            inflate(R.layout.
                                            activity_fill_blank,
                                    parent, false);
                }

                LinearLayout linearLayout = convertView.findViewById(R.id.fillBlankId);
                linearLayout.removeAllViews();
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                BlankQuestion blankQuestion = (BlankQuestion) getItem(position);
                List<String> strings = blankQuestion.getStrings();
                SingleCharEditText prevEditText = null;
                TextView questionTv = convertView.findViewById(R.id.questionTv);
//                TextView answerTv = convertView.findViewById(R.id.answerTv);
//                answerTv.setVisibility(View.GONE);
                questionTv.setText("Câu " + blankQuestion.getOrderNumber());
                questionTv.setTextColor(Color.WHITE);
                for (String string : strings) {
                    if (string.length() > 1) {
                        TextView textView = new TextView(context);
                        textView.setText(string);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.WHITE);
                        linearLayout.addView(textView);
                    } else {
                        SingleCharEditText singleCharEditText = new SingleCharEditText(context);
                        singleCharEditText.setTextColor(Color.WHITE);
                        singleCharEditText.setTextSize(30);
                        if (prevEditText != null) {
                            prevEditText.setNextEditText(singleCharEditText);
                            singleCharEditText.setBeforeEditText(prevEditText);
                        }
                        prevEditText = singleCharEditText;
                        linearLayout.addView(singleCharEditText);
                    }
                }
                break;
            case TYPE_QUESTION_ONLY_ONE_ANSWER:
                if (convertView == null) {
                    convertView = LayoutInflater
                            .from(context)
                            .inflate(R.layout
                                            .activity_question_only_one_answer
                                    , parent, false);
                }
                TextView questionTextV, questionContentTv;
                Button aBtn, bBtn, cBtn, dBtn, cancelBtn, submitBtn;

                questionTextV = convertView.findViewById(R.id.questionTv);
                questionContentTv = convertView.findViewById(R.id.questionContentTv);
                aBtn = convertView.findViewById(R.id.aBtn);
                bBtn = convertView.findViewById(R.id.bBtn);
                cBtn = convertView.findViewById(R.id.cBtn);
                dBtn = convertView.findViewById(R.id.dBtn);
                aBtn.setBackgroundColor(Color.CYAN);
                bBtn.setBackgroundColor(Color.CYAN);
                cBtn.setBackgroundColor(Color.CYAN);
                dBtn.setBackgroundColor(Color.CYAN);

                aBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bBtn.setBackgroundColor(Color.CYAN);
                        cBtn.setBackgroundColor(Color.CYAN);
                        dBtn.setBackgroundColor(Color.CYAN);
                        aBtn.setBackgroundColor(Color.GREEN);
                    }
                });

                bBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aBtn.setBackgroundColor(Color.CYAN);
                        cBtn.setBackgroundColor(Color.CYAN);
                        dBtn.setBackgroundColor(Color.CYAN);
                        bBtn.setBackgroundColor(Color.GREEN);
                    }
                });

                cBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aBtn.setBackgroundColor(Color.CYAN);
                        bBtn.setBackgroundColor(Color.CYAN);
                        dBtn.setBackgroundColor(Color.CYAN);
                        cBtn.setBackgroundColor(Color.GREEN);
                    }
                });

                dBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aBtn.setBackgroundColor(Color.CYAN);
                        bBtn.setBackgroundColor(Color.CYAN);
                        cBtn.setBackgroundColor(Color.CYAN);
                        dBtn.setBackgroundColor(Color.GREEN);

                    }
                });

                QuestionOnlyOneAnswer questionOnlyOneAnswer = new QuestionOnlyOneAnswer();
                questionOnlyOneAnswer = (QuestionOnlyOneAnswer) item;

                questionTextV.setText("Câu " + questionOnlyOneAnswer.getOrderNumber());
                questionContentTv.setText(questionOnlyOneAnswer.getQuestionContent());
                aBtn.setText(questionOnlyOneAnswer.getaAnswer());
                bBtn.setText(questionOnlyOneAnswer.getbAnswer());
                cBtn.setText(questionOnlyOneAnswer.getcAnswer());
                dBtn.setText(questionOnlyOneAnswer.getdAnswer());
                break;
        }
        return convertView;
    }
}
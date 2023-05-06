package com.ptithcm.quizapp;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

public class SingleCharEditText extends AppCompatEditText implements TextWatcher {

    private EditText nextEditText;
    private EditText beforeEditText;

    public SingleCharEditText(Context context) {
        super(context);
        init();
    }

    public SingleCharEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleCharEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        addTextChangedListener(this);
    }

    public void setNextEditText(EditText editText) {
        nextEditText = editText;
    }

    public void setBeforeEditText(EditText editText){ beforeEditText = editText;}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 1) {
            if (nextEditText != null) {
                nextEditText.requestFocus();
            }
        }
        else if (s.length() == 0) {
            if (beforeEditText != null) {
                beforeEditText.requestFocus();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void afterTextChanged(Editable s) {}
}


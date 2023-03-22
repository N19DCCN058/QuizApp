package com.ptithcm.quizapp;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAlertDialog extends AppCompatActivity {
    private Context context;
    AlertDialog.Builder builder;
    TextView textTitle, textMessage;
    Button buttonYes, buttonNo, buttonAction;
    ImageView imageIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setControl(View view) {
        textTitle = view.findViewById(R.id.textTitle);
        textMessage = view.findViewById(R.id.textMessage);
        buttonYes = view.findViewById(R.id.buttonYes);
        buttonNo = view.findViewById(R.id.buttonNo);
        buttonAction = view.findViewById(R.id.buttonAction);
        imageIcon = view.findViewById(R.id.imageIcon);
    }

    public CustomAlertDialog(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
    }

    void showSuccessDialog(View view){
        view = LayoutInflater.from(context).inflate(R.layout.layout_success_dailog,null);
        setControl(view);
        builder.setView(view);

        textTitle.setText(context.getResources().getString(R.string.success_title));
        textMessage.setText(context.getResources().getString(R.string.dummy_text));
        buttonAction.setText(context.getResources().getString(R.string.okay));
        imageIcon.setImageResource(R.drawable.ic_baseline_done_24);

        final AlertDialog alertDialog = builder.create();


        alertDialog.show();

    }

    void showWarningDialog(View view ){
        setControl(view);
        builder.setView(view);
        textTitle.setText(context.getResources().getString(R.string.warning_title));
        textMessage.setText(context.getResources().getString(R.string.dummy_text));
        buttonYes.setText(context.getResources().getString(R.string.yes));
        buttonNo.setText(context.getResources().getString(R.string.no));
        imageIcon.setImageResource(R.drawable.ic_baseline_warning_24);


        final AlertDialog alertDialog = builder.create();
//        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });
//
        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    void showErrorDialog(View view){
        view = LayoutInflater.from(context).inflate(R.layout.layout_error_dailog,null);
        builder.setView(view);
        setControl(view);
        textTitle.setText(context.getResources().getString(R.string.error_title));
        textMessage.setText(context.getResources().getString(R.string.dummy_text));
        buttonAction.setText(context.getResources().getString(R.string.okay));
        imageIcon.setImageResource(R.drawable.ic_baseline_error_24);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}
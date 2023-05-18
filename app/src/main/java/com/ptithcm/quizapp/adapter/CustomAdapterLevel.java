package com.ptithcm.quizapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.quizapp.R;
import com.ptithcm.quizapp.database.DBHelper;
import com.ptithcm.quizapp.model.Level;
import com.ptithcm.quizapp.Main_Select_Level_Question;

import java.util.ArrayList;
import java.util.Objects;

public class CustomAdapterLevel extends RecyclerView.Adapter<CustomAdapterLevel.LVViewHolder> {
    private Context context;
    private ArrayList<Level> data;
    private AlertDialog dialog;
    private TextInputEditText edtTitleDialog;
    private Button btnSubmitLVDialog;

    private AlertDialog.Builder builder;
    private TextView textTitle, textMessage;
    private Button buttonAction;
    private ImageView imageIcon;
    public CustomAdapterLevel(@NonNull Context context, @NonNull ArrayList<Level> data) {
        this.context = context;
        this.data= data;
        builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
    }

    @NonNull
    @Override
    public LVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_level, parent, false);
        return new LVViewHolder(v);
    }
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull LVViewHolder holder, int position) {
        Level lv = data.get(position);
        holder.tvTitleLevel.setText(lv.getTitle());
        holder.tvTitleLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Main_Select_Level_Question.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mIDLevel", String.valueOf(lv.getId()));
                context.startActivity(intent);

//                Intent intent = new Intent(context, questionSelect.class);
//                intent.putExtra("mIDLevel", String.valueOf(lv.getId()));
//                context.startActivity(intent);
            }
        });
        holder.imgDeleteLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(context);
                if(dbHelper.deleteLevel(String.valueOf(lv.getId()))){
                    data.remove(lv);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                }
                else {
                    showErrorDialog("The level contains questions so it can't be deleted");
                    Toast.makeText(context, "Can not delete. The level contains questions so it can't be deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imgEditLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.dialog_get_title_level, null);
                edtTitleDialog = view.findViewById(R.id.edtTitleLV);
                btnSubmitLVDialog = view.findViewById(R.id.btnSubmitLVDialog);
                builder.setView(view);
                dialog = builder.create();
                edtTitleDialog.setText(lv.getTitle());
                dialog.show();
                btnSubmitLVDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = edtTitleDialog.getText().toString();
                        if(content.equals("")) {
                            showErrorDialog("Can be not NULL");
                            Toast.makeText(context, "Can be not NULL", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(dbHelper.updateLevel(String.valueOf(lv.getId()), content.trim()) && !content.trim().equals("")) {
                            for (int i = 0; i < data.size(); i++) {
                                if(data.get(i).getId() == lv.getId()) {
                                    data.get(i).setTitle( edtTitleDialog.getText().toString());
                                }
                            }

                            notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        }
                        else  {
                            showErrorDialog("Failed");
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public class LVViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDeleteLevel,imgEditLevel;
        TextView tvTitleLevel;
        LinearLayout layoutItemLV;
        public LVViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleLevel = itemView.findViewById(R.id.tvTitleLevel);
            layoutItemLV = itemView.findViewById(R.id.layoutItemLV);
            imgDeleteLevel = itemView.findViewById(R.id.imgDeleteLevel);
            imgEditLevel = itemView.findViewById(R.id.imgEditLevel);
        }
    }
    private void setControl(View view) {
        textTitle = view.findViewById(R.id.textTitle);
        textMessage = view.findViewById(R.id.textMessage);
        buttonAction = view.findViewById(R.id.buttonAction);
        imageIcon = view.findViewById(R.id.imageIcon);
    }
    public void showErrorDialog(String mess) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_notify_error, null);
        builder.setView(view);
        setControl(view);
        textTitle.setText(context.getResources().getString(R.string.error));
        textMessage.setText(mess);
        buttonAction.setText(context.getResources().getString(R.string.okay));
        imageIcon.setImageResource(R.drawable.ic_baseline_error_24);

        AlertDialog alertDialog;
        alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}

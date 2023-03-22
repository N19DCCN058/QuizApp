package com.ptithcm.quizapp;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterQuestion extends RecyclerView.Adapter<CustomAdapterQuestion.QSViewHolder> implements Filterable {
    private Context context;
    private ArrayList<question> data_filter;
    private ArrayList<question> data_old;
    private SQLiteDatabase database = null;
    ArrayList<question> data_selected = new ArrayList<>();
    private boolean isEnable = false;

    public CustomAdapterQuestion(@NonNull Context context, @NonNull ArrayList<question> data, SQLiteDatabase database) {
        this.context = context;
        this.data_filter = data;
        this.data_old = data;
        this.database = database;
    }


    @NonNull
    @Override
    public QSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QSViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QSViewHolder holder, int position) {
        question qs = data_old.get(position);
        holder.tvNumbId.setText(qs.getQuestionID());
        String txt = qs.getQuestionContent();
        if (txt.length() > 20) {
            holder.tvTitle.setText(txt.substring(0, 20) + "...");
        } else {
            holder.tvTitle.setText(qs.getQuestionContent());
        }
        holder.tvLever.setText("Level " + qs.getQuestionLevel());
        holder.layoutItemQS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailQuestion.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("obj_question", qs);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }


        });
        holder.tvNumbId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if (!isEnable) {
                        ActionMode.Callback callback = new ActionMode.Callback() {
                            @Override
                            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                                MenuInflater menuInflater = mode.getMenuInflater();
                                menuInflater.inflate(R.menu.menu_qs_selected, menu);
                                return true;
                            }

                            @Override
                            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                                isEnable = true;
                                clickItem(holder);
                                return true;
                            }

                            @Override
                            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.btnDelete_tb:
                                        for (question qs : data_selected) {
                                            database.delete("Answers", "questionID =? ", new String[]{qs.getQuestionID()});
                                            database.delete("Questions", "questionID =? ", new String[]{qs.getQuestionID()});
                                        }
                                        mode.finish();
                                        break;
                                }
                                return true;
                            }

                            @Override
                            public void onDestroyActionMode(ActionMode mode) {
                                isEnable = false;
                                data_selected.clear();
                                notifyDataSetChanged();
                            }
                        };
                        ((AppCompatActivity) v.getContext()).startActionMode(callback);
                    } else {
                        clickItem(holder);
                    }
                }
            }
        });
        if (data_selected.isEmpty()) {
            holder.isSelected = false;
            holder.layoutItemQS.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void clickItem(QSViewHolder holder) {
        question qs = data_old.get(holder.getAdapterPosition());
        if (!holder.isSelected) {
            holder.layoutItemQS.setBackgroundColor(Color.rgb(102, 102, 153));
            holder.isSelected = true;
            data_selected.add(qs);
        } else {
            holder.layoutItemQS.setBackgroundColor(Color.TRANSPARENT);
            holder.isSelected = false;
            data_selected.remove(qs);
        }
    }

    @Override
    public int getItemCount() {
        return data_filter.size();
    }

    public class QSViewHolder extends RecyclerView.ViewHolder {

        TextView tvNumbId, tvTitle, tvLever;
        LinearLayout layoutItemQS;
        boolean isSelected;

        public QSViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumbId = itemView.findViewById(R.id.tvNumbId);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLever = itemView.findViewById(R.id.tvLever);
            layoutItemQS = itemView.findViewById(R.id.layoutItemQS);
            isSelected = false;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString().toLowerCase().trim();
                if (strSearch.isEmpty()) {
                    data_filter = data_old;
                } else {
                    ArrayList<question> list = new ArrayList<>();
                    for (question qs : data_old) {
                        if (qs.getQuestionContent().toLowerCase().contains(strSearch)) {
                            list.add(qs);
                        }
                    }
                    data_filter = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = data_filter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data_filter = (ArrayList<question>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}

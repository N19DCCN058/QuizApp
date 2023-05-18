package com.ptithcm.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.ptithcm.quizapp.adapter.CustomAdapterQuestion;
import com.ptithcm.quizapp.database.DBHelper;
import com.ptithcm.quizapp.model.Level;

public class Main_Select_Level_Question extends AppCompatActivity {
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private SearchView svSearch_tb;
    private String currentSort = "questionID";
    private DBHelper dbHelper;

    private FloatingActionButton fabAddNewQS;
    private RecyclerView rvListQS;
    private CustomAdapterQuestion adapterQS;
    private SwipeRefreshLayout srfRefresh;
    private String receivedID;
    private Level lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivedID= getIntent().getStringExtra("mIDLevel");
        setContentView(R.layout.main_select_level_question);
        setControl();
        setEvent();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_list_question, menu);
        svSearch_tb = (SearchView) menu.findItem(R.id.svSearch_tb).getActionView();
        svSearch_tb.setQueryHint("Search Here....");
        svSearch_tb.setMaxWidth(Integer.MAX_VALUE);
        svSearch_tb.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterQS.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterQS.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void setEvent() {
        getData(currentSort);
        getSupportActionBar().setTitle(lv.getTitle());
        fabAddNewQS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Select_Level_Question.this, Main_Select_Level_Question_Add_New.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mIDLevel", String.valueOf(receivedID));
                startActivity(intent);
            }
        });
        srfRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(currentSort);
                srfRefresh.setRefreshing(false);
            }
        });
    }

    private void getData(String currentSort) {
        lv = dbHelper.getLevelByID(receivedID);
        adapterQS = new CustomAdapterQuestion(this, dbHelper.getListQuestion(currentSort,receivedID));
        rvListQS.setAdapter(adapterQS);
        adapterQS.notifyDataSetChanged();
    }

    private void setControl() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvListQS = findViewById(R.id.rvListQS);
        fabAddNewQS = findViewById(R.id.fabtbAdd);
        srfRefresh = findViewById(R.id.srfRefresh);
        dbHelper = new DBHelper(this);
    }
    private void back() {
        Intent intent = new Intent(this, Main_Select_Level.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mType", String.valueOf(lv.getType()));
        startActivity(intent);
        finishAffinity();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
                break;
            case R.id.btnHome_tb:
                Intent intent = new Intent(getApplicationContext(), Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                adapterQS.showWarningDialog(QuestionSelect.this.getResources().getString(R.string.exit));
                break;
            case R.id.btnSort_tb: {
                if(currentSort != "questionID")
                    currentSort = "questionID";
                else
                    currentSort = "questionContent";
                getData(currentSort);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (!svSearch_tb.isIconified()) {
            svSearch_tb.setIconified(true);
            return;
        }
        super.onBackPressed();

    }
}
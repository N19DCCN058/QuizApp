package com.ptithcm.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private SearchView svSearch_tb;
    private String dbName = "QuizAppDB.db";
    private SQLiteDatabase database = null;
    private String currentSort = "questionID";

    CustomAlertDialog alertDialog;
    FloatingActionButton fabAddNewQS;
    RecyclerView rvListQS;
    ArrayList<question> data_QS = new ArrayList<>();
    CustomAdapterQuestion adapterQS;
    SwipeRefreshLayout srfRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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
        khoitao();
        getData(currentSort);
        fabAddNewQS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewQuestion.class);
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
        data_QS.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM Questions ORDER BY " + currentSort, null);
        while (cursor.moveToNext()) {
            data_QS.add(new question(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }
        adapterQS = new CustomAdapterQuestion(this, data_QS, database);
        rvListQS.setAdapter(adapterQS);
        cursor.close();
        adapterQS.notifyDataSetChanged();
    }

    private void setControl() {
//        toolbar = findViewById(R.id.toolbarMain);
//        setSupportActionBar(toolbar);
        alertDialog = new CustomAlertDialog(MainActivity.this);
        rvListQS = findViewById(R.id.rvListQS);
        fabAddNewQS = findViewById(R.id.fabtbAdd);
        srfRefresh = findViewById(R.id.srfRefresh);
    }

    private void khoitao() {
        File dbFile = getDatabasePath(dbName);
        if (dbFile.exists()) {
            dbFile.delete();
        }
        try {
            InputStream myInput = getAssets().open(dbName);
            String outFile = getApplicationInfo().dataDir + "/databases/" + dbName;
            File f = new File(getApplicationInfo().dataDir + "/databases/");
            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream myOutPut = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = myInput.read(buffer)) > 0) {
                myOutPut.write(buffer, 0, len);
            }
            myOutPut.flush();
            myInput.close();
            myOutPut.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Loi sao chep DB!", Toast.LENGTH_SHORT).show();
        }
        database = openOrCreateDatabase(dbName, MODE_PRIVATE, null);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnExit_tb:
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_warning_dailog,null);
                view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                alertDialog.showWarningDialog(view);

//
//                System.exit(1);
                break;
            case R.id.btnSortByID: {
                currentSort = "questionID";
                getData(currentSort);
                break;
            }
            case R.id.btnSortByContent: {
                currentSort = "questionContent";
                getData(currentSort);
                break;
            }
            case R.id.btnSortByLever: {
                currentSort = "questionLever";
                getData(currentSort);
                break;
            }
            case R.id.btnAdd_tb: {
                Intent intent = new Intent(MainActivity.this, NewQuestion.class);
                startActivity(intent);
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
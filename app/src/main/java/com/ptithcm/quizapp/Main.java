package com.ptithcm.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.navigation.NavigationView;
import com.ptithcm.quizapp.adapter.CustomAdapterQuestion;
import com.ptithcm.quizapp.database.DBHelper;
import com.ptithcm.quizapp.model.NumbAccountsByDay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity{
    private LineChart lineChart;
    private ImageView typeSelect, typeSort, typeFill, typeVocabulary;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private SwipeRefreshLayout rfrLineChar;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setControl();
        setEvent();
        khoiTao();
    }
    private List<Entry> getdata() {
        ArrayList<NumbAccountsByDay> list = dbHelper.getCountAccByDay();

        List<Entry> entries = new ArrayList<>();
        for (NumbAccountsByDay l :list ) {
            entries.add(new Entry(l.getDay(),l.getCount()));
        }
        return entries;
    }
    private void khoiTao() {
        LineDataSet dataSet = new LineDataSet(getdata(), "");
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(18);
        dataSet.setValueFormatter(new DefaultValueFormatter(0));
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        lineChart.getXAxis().setTextSize(18);
        lineChart.getXAxis().setTextColor(Color.BLACK);
        lineChart.getAxisLeft().setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        lineChart.getAxisLeft().setTextColor(Color.BLACK);
        lineChart.getAxisLeft().setTextSize(18);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
    }


    private void setControl() {
        navigationView = findViewById(R.id.navigation_drawer);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        copyData();
        rfrLineChar = findViewById(R.id.rfrLineChar);
        lineChart = findViewById(R.id.lineChart);
        lineChart.setTouchEnabled(false);
        typeSelect = findViewById(R.id.typeSelect);
        typeSort = findViewById(R.id.typeSort);
        typeFill = findViewById(R.id.typeFill);
        typeVocabulary = findViewById(R.id.typeVocabulary);
    }
    private void copyData() {
        dbHelper.getReadableDatabase();
        File dbFile = getDatabasePath(dbHelper.DBNAME);
        if (dbFile.exists()) {
//            dbFile.delete();
            return;
        }
        try {
            InputStream myInput = getAssets().open(dbHelper.DBNAME);
            String outFile = getApplicationInfo().dataDir + "/databases/" + dbHelper.DBNAME;
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
            Toast.makeText(this, "Error when copying data!", Toast.LENGTH_SHORT).show();
        }
        SQLiteDatabase database = openOrCreateDatabase(dbHelper.DBNAME, MODE_PRIVATE, null);

    }
    private void setEvent() {
        rfrLineChar.setColorSchemeColors(Color.BLUE,Color.BLUE);
        rfrLineChar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lineChart.clear();
                khoiTao();
                rfrLineChar.setRefreshing(false);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_account:
                        Intent intent = new Intent(Main.this, Main_Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userID", String.valueOf("1"));
                        intent.putExtra("roleID", String.valueOf("2"));
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        CustomAdapterQuestion adapterQuestion = new CustomAdapterQuestion(Main.this,null);
                        adapterQuestion.showWarningDialog(Main.this.getResources().getString(R.string.exit));
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
        typeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Main_Select_Level.class);
                intent.putExtra("mType", "0");
                startActivity(intent);
            }
        });
        typeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Main_Select_Level.class);
                intent.putExtra("mType", "1");
                startActivity(intent);
            }
        });
        typeFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Main_Select_Level.class);
                intent.putExtra("mType", "2");
                startActivity(intent);
            }
        });
        typeVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Main_Select_Level.class);
                intent.putExtra("mType", "3");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
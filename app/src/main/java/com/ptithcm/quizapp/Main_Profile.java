package com.ptithcm.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ptithcm.quizapp.database.DBHelper;
import com.ptithcm.quizapp.model.User;

import java.util.Objects;

public class Main_Profile extends AppCompatActivity {

    private String receivedID;
    private String receivedRole;
    private TextView tvRoleUser;
    private EditText edtUserName, edtPhoneNumber, edtEmail;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_profile);
        getSupportActionBar().setTitle("Profile");
        receivedID= getIntent().getStringExtra("userID");
        receivedRole= getIntent().getStringExtra("roleID");
        setControl();
        khoiTao();
        setEvent();

    }

    private void setControl() {
        tvRoleUser = findViewById(R.id.tvRoleUser);
        edtUserName = findViewById(R.id.edtUserName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtEmail = findViewById(R.id.edtEmail);
    }
    private void khoiTao() {
        dbHelper = new DBHelper(Main_Profile.this);
        User user = dbHelper.getUserByID(receivedID);
        if(Objects.equals(receivedRole, "1")) {
            tvRoleUser.setText("CLIENT");
        }
        else {
            tvRoleUser.setText("ADMIN");
        }
        edtUserName.setText(user.getUserName());
        edtPhoneNumber.setText(user.getPhoneNumber());
        edtEmail.setText(user.getEmail());
    }
    private void setEvent() {
    }

    private void back() {
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
                break;
            case R.id.btnSaveProfile:
                if(edtUserName.getText().toString().equals("")) {
                    edtUserName.setError("Can be not null");
                    break;
                }
                if(edtPhoneNumber.getText().toString().equals("")) {
                    edtPhoneNumber.setError("Can be not null");
                    break;
                }
                if (validCellPhone(edtPhoneNumber.getText().toString())) {
                    edtPhoneNumber.setError("Invalid phone number");
                    break;
                }
                User user = new User(edtUserName.getText().toString().trim(),
                        edtPhoneNumber.getText().toString().trim(),
                        edtEmail.getText().toString().trim());
                if(!dbHelper.updateUser(user,receivedID)) {
                    Toast.makeText(this, "Registered phone number", Toast.LENGTH_SHORT).show();
                    break;
                }
                back();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean validCellPhone(String number) {
        if(number.length() < 10 || number.length() > 13 || containsNonNumeric(number) )
            return true;
        else
            return false;
    }
    public static boolean containsNonNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
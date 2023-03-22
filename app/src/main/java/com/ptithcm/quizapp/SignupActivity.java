package com.ptithcm.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity<SignUpActivity> extends AppCompatActivity {

    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.Signup_button);
        loginRedirectText = findViewById(R.id.signupRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                if (user.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                } else{
                    if (creattAcc(user,pass)) {
                        Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(SignupActivity.this, "SignUp Failed" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });


    }

    private boolean creattAcc(String user, String pass) {
        return true;
    }
}
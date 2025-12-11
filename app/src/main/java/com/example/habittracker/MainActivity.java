package com.example.habittracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button createAccount, btnLogin;
    DatabaseHelper db;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        createAccount = findViewById(R.id.createAccount);
        btnLogin = findViewById(R.id.btnLogin);
        LinearLayout googleSign = findViewById(R.id.googleSign);
        LinearLayout appleSign = findViewById(R.id.appleSign);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString().trim();
                String p = password.getText().toString().trim();
                if (e.isEmpty() || p.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {

                    boolean isInserted = db.addUser(e, p);
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "Account Created! Logging in...", Toast.LENGTH_SHORT).show();
                        goToDashboard();
                    } else {
                        Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString().trim();
                String p = password.getText().toString().trim();

                if (e.isEmpty() || p.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {

                    boolean check = db.checkUser(e, p);
                    if (check) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        goToDashboard();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        googleSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Google sign clicked", Toast.LENGTH_SHORT).show();
            }
        });

        appleSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Apple sign clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void goToDashboard() {
        Intent intent = new Intent(MainActivity.this, HabitDashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
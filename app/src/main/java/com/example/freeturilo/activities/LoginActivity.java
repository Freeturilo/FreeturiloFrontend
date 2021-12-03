package com.example.freeturilo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.freeturilo.R;
import com.example.freeturilo.misc.AuthTools;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText emailInput = findViewById(R.id.email);
        String email = emailInput.getText().toString();
        EditText passwordInput = findViewById(R.id.password);
        String password = passwordInput.getText().toString();
        if (AuthTools.login(email, password)) {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
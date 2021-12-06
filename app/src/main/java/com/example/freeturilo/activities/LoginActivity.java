package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.freeturilo.R;
import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIActivityHandler;
import com.example.freeturilo.connection.APIMock;
import com.example.freeturilo.misc.AuthTools;
import com.example.freeturilo.misc.ValidationTools;

public class LoginActivity extends AppCompatActivity {
    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        api = new APIMock();
    }

    private boolean validate() {
        boolean valid = true;
        EditText emailInput = findViewById(R.id.email);
        EditText passwordInput = findViewById(R.id.password);
        if (!ValidationTools.hasEmail(emailInput)) {
            valid = false;
            ValidationTools.setInputError(this, emailInput, R.string.email_invalid_text);
        }
        if (ValidationTools.isEmpty(passwordInput)) {
            valid = false;
            ValidationTools.setInputError(this, passwordInput, R.string.password_empty_text);
        }
        return valid;
    }

    public void login(@NonNull View view) {
        if (validate()) {
            EditText emailInput = findViewById(R.id.email);
            String email = emailInput.getText().toString();
            EditText passwordInput = findViewById(R.id.password);
            String password = passwordInput.getText().toString();
            api.postUserAsync(email, password, this::goToAdmin, new APIActivityHandler(this));
        }
    }

    private void goToAdmin(@NonNull String token) {
        AuthTools.setToken(token);
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
    }
}
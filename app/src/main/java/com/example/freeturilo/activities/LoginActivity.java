package com.example.freeturilo.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.freeturilo.R;
import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIActivityHandler;
import com.example.freeturilo.connection.APIConnector;
import com.example.freeturilo.misc.AuthCredentials;
import com.example.freeturilo.misc.AuthTools;
import com.example.freeturilo.misc.Validation;

public class LoginActivity extends FreeturiloActivity {
    private final API api = new APIConnector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private boolean validate() {
        boolean valid = true;
        EditText emailInput = findViewById(R.id.email);
        EditText passwordInput = findViewById(R.id.password);
        if (!Validation.hasEmail(emailInput)) {
            valid = false;
            Validation.setInputError(this, emailInput, R.string.email_invalid_text);
        }
        if (Validation.isEmpty(passwordInput)) {
            valid = false;
            Validation.setInputError(this, passwordInput, R.string.password_empty_text);
        }
        return valid;
    }

    public void login(@NonNull View view) {
        if (validate()) {
            EditText emailInput = findViewById(R.id.email);
            String email = emailInput.getText().toString();
            EditText passwordInput = findViewById(R.id.password);
            String password = passwordInput.getText().toString();
            AuthCredentials authCredentials = new AuthCredentials(email, password);
            api.postUserAsync(authCredentials, this::goToAdmin, new APIActivityHandler(this, false));
        }
    }

    private void goToAdmin(@NonNull String token) {
        AuthTools.setToken(token);
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
    }
}
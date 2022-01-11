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

/**
 * An activity displaying the login form for administrators of the system.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FreeturiloActivity
 * @see AuthCredentials
 */
public class LoginActivity extends FreeturiloActivity {
    /**
     * Stores the API used by this activity to perform administrator
     * authentication.
     */
    private final API api = new APIConnector();

    /**
     * Called when this activity is created.
     * <p>
     * Calls {@link FreeturiloActivity#onCreate}. Initializes the layout of
     * this activity.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with
     *                              {@code AppCompatActivity}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Validates input data of the login form.
     * <p>
     * Checks if the email input contains an email address and if the password
     * input is not empty. If one of this conditions is false, a validation
     * error occurs.
     * @return              a boolean indicating whether validation has passed
     */
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

    /**
     * Authenticates the user. Called when the login button is clicked.
     * <p>
     * Validates input data of the login form. If validation passes, the
     * authentication request is sent to {@link #api}.
     * @param view          unused parameter, the login button
     * @see API#postUserAsync
     */
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

    /**
     * Starts the {@code AdminActivity}. Called after a successful
     * authentication.
     * <p>
     * Sets the authorization token for global application usage. Finishes this
     * activity.
     * @param token         an authorization token returned from api
     * @see #finish
     * @see AuthTools#setToken
     */
    private void goToAdmin(@NonNull String token) {
        AuthTools.setToken(token);
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
    }
}
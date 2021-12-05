package com.example.freeturilo.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.freeturilo.R;
import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIMock;
import com.example.freeturilo.dialogs.MailNotifyDialog;

public class AdminActivity extends AppCompatActivity {
    private API api;
    int checkedButtonId = R.id.start_state_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        api = new APIMock();
        RadioGroup systemStateButtons = findViewById(R.id.system_state_buttons);
        systemStateButtons.setOnCheckedChangeListener(this::showChangeSystemStateDialog);
    }

    private void showChangeSystemStateDialog(RadioGroup radioGroup, int checkedButtonId) {
        if (this.checkedButtonId == checkedButtonId) return;
        RadioButton stateButton = findViewById(checkedButtonId);
        new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                .setMessage(String.format("%s %s?", getString(R.string.change_system_state_message),
                        stateButton.getText()))
                .setPositiveButton(R.string.yes_text, (dialog, id) -> {
                            this.checkedButtonId = checkedButtonId;
                            changeSystemState(checkedButtonId);
                        })
                .setNegativeButton(R.string.cancel_text, (dialog, id) -> {
                            RadioButton previousStateButton = findViewById(this.checkedButtonId);
                            previousStateButton.setChecked(true);
                        })
                .show();
    }

    private void changeSystemState(int stateButtonId) {
        final int startStateButton = R.id.start_state_button;
        final int demoStateButton = R.id.demo_state_button;
        final int stopStateButton = R.id.stop_state_button;
        switch (stateButtonId) {
            case startStateButton:
                api.postStateStartAsync(null);
                break;
            case demoStateButton:
                api.postStateDemoAsync(null);
                break;
            case stopStateButton:
                api.postStateStopAsync(null);
                break;
        }
    }

    public void showMailNotifyDialog(View view) {
        MailNotifyDialog dialog = new MailNotifyDialog();
        dialog.show(getSupportFragmentManager(), null);
    }

    public void setNotifyThreshold(int threshold) {
        api.postNotifyThresholdAsync(threshold, null);
    }
}
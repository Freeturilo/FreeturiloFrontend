package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.freeturilo.R;
import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIActivityHandler;
import com.example.freeturilo.connection.APIMock;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.core.SystemStateTools;
import com.example.freeturilo.dialogs.MailNotifyDialog;

public class AdminActivity extends AppCompatActivity {
    private API api;
    private int checkedButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        api = new APIMock();
        api.getStateAsync(this::onSystemStateReady, new APIActivityHandler(this));
    }

    private void onSystemStateReady(@NonNull SystemState systemState) {
        checkedButtonId = SystemStateTools.getButtonId(systemState);
        RadioButton checkedButton = findViewById(checkedButtonId);
        checkedButton.setChecked(true);
        RadioGroup systemStateButtons = findViewById(R.id.system_state_buttons);
        systemStateButtons.setOnCheckedChangeListener(this::showChangeSystemStateDialog);
        enableStateButtons();
    }

    private void enableStateButtons() {
        RadioButton startedStateButton = findViewById(R.id.started_state_button);
        startedStateButton.setEnabled(true);
        RadioButton demoStateButton = findViewById(R.id.demo_state_button);
        demoStateButton.setEnabled(true);
        RadioButton stoppedStateButton = findViewById(R.id.stopped_state_button);
        stoppedStateButton.setEnabled(true);
    }

    private void showChangeSystemStateDialog(@NonNull RadioGroup radioGroup, int checkedButtonId) {
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
        api.postStateAsync(SystemStateTools.getState(stateButtonId),
                new APIActivityHandler(this));
    }

    public void showMailNotifyDialog(@NonNull View view) {
        MailNotifyDialog dialog = new MailNotifyDialog(api, this::setNotifyThreshold);
        dialog.show(getSupportFragmentManager(), null);
    }

    private void setNotifyThreshold(int threshold) {
        api.postNotifyThresholdAsync(threshold, new APIActivityHandler(this));
    }
}
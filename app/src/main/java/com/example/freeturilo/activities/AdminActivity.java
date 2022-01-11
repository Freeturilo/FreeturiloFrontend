package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.freeturilo.R;
import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIActivityHandler;
import com.example.freeturilo.connection.APIConnector;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.dialogs.MailNotifyDialog;

/**
 * An activity displaying the administrator panel.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FreeturiloActivity
 * @see SystemState
 */
public class AdminActivity extends FreeturiloActivity {
    /**
     * Stores the API used by this activity to perform external data
     * transaction.
     */
    private final API api = new APIConnector();
    /**
     * Stores the resource identifier of the currently checked system state
     * button.
     */
    private int checkedButtonId;

    /**
     * Called when this activity is created.
     * <p>
     * Calls {@link FreeturiloActivity#onCreate}. Initializes the layout of
     * this activity and initializes system state buttons to show dialogs
     * when checked.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with
     *                              {@code AppCompatActivity}
     * @see #showChangeSystemStateDialog
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        RadioGroup systemStateButtons = findViewById(R.id.system_state_buttons);
        systemStateButtons.setOnCheckedChangeListener(this::showChangeSystemStateDialog);
    }

    /**
     * Called when this activity is resumed.
     * <p>
     * Sends a request for the current system state to {@link #api}.
     * @see API#getStateAsync
     */
    @Override
    protected void onResume() {
        super.onResume();
        api.getStateAsync(this::onSystemStateReady, new APIActivityHandler(this, true));
    }

    /**
     * Called when this activity is paused.
     * <p>
     * Disables system state buttons.
     */
    @Override
    protected void onPause() {
        super.onPause();
        disableStateButtons();
    }

    /**
     * Called when the current system state is retrieved from {@link #api}.
     * <p>
     * Checks the corresponding system state button and sets
     * {@link #checkedButtonId}. Enables system state buttons.
     * @param systemState       current value of system state
     * @see #enableStateButtons
     */
    private void onSystemStateReady(@NonNull SystemState systemState) {
        checkedButtonId = getButtonId(systemState);
        RadioButton checkedButton = findViewById(checkedButtonId);
        checkedButton.setChecked(true);
        enableStateButtons();
    }

    /**
     * Disables system state buttons.
     */
    private void disableStateButtons() {
        RadioButton startedStateButton = findViewById(R.id.started_state_button);
        startedStateButton.setEnabled(false);
        RadioButton demoStateButton = findViewById(R.id.demo_state_button);
        demoStateButton.setEnabled(false);
        RadioButton stoppedStateButton = findViewById(R.id.stopped_state_button);
        stoppedStateButton.setEnabled(false);
    }

    /**
     * Enables system state buttons.
     */
    private void enableStateButtons() {
        RadioButton startedStateButton = findViewById(R.id.started_state_button);
        startedStateButton.setEnabled(true);
        RadioButton demoStateButton = findViewById(R.id.demo_state_button);
        demoStateButton.setEnabled(true);
        RadioButton stoppedStateButton = findViewById(R.id.stopped_state_button);
        stoppedStateButton.setEnabled(true);
    }

    /**
     * Shows a dialog confirming system state change. Called when one of the
     * system state buttons is checked.
     * @param radioGroup        unused parameter, the system state buttons
     *                          group
     * @param checkedButtonId   an integer equal to the resource identifier of
     *                          the checked button
     */
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

    /**
     * Sends a request with new system state to {@link #api}.
     * @param stateButtonId     an integer equal to the resource identifier
     *                          of the system state button used to change the
     *                          system state
     * @see API#postStateAsync
     */
    private void changeSystemState(int stateButtonId) {
        api.postStateAsync(getState(stateButtonId), new APIActivityHandler(this, false));
    }

    /**
     * Shows a dialog for managing the mail notification settings. Called when
     * the mail notifications button is clicked.
     * @param view              unused parameter, the mail notifications button
     * @see MailNotifyDialog
     */
    public void showMailNotifyDialog(@NonNull View view) {
        MailNotifyDialog dialog = new MailNotifyDialog(api, this::setNotifyThreshold);
        dialog.show(getSupportFragmentManager(), null);
    }

    /**
     * Sends a request with new notification threshold to {@link #api}.
     * @param threshold         an integer equal to the new notification
     *                          threshold
     * @see API#postNotifyThresholdAsync
     */
    private void setNotifyThreshold(int threshold) {
        api.postNotifyThresholdAsync(threshold, new APIActivityHandler(this, false));
    }

    /**
     * Gets the resource identifier of the button representing a
     * {@code SystemState}. Reverse of {@code getState}.
     * @param state     a system state
     * @return          an integer equal to the resource identifier of the
     *                  button representing the system state
     */
    public static int getButtonId(SystemState state) {
        switch (state) {
            case STARTED:
                return R.id.started_state_button;
            case DEMO:
                return R.id.demo_state_button;
            default:
                return R.id.stopped_state_button;
        }
    }

    /**
     * Gets the {@code SystemState} that is represented by a button of
     * a resource identifier. Reverse of {@code getButtonId}.
     * @param stateButtonId     an integer equal to the resource identifier of
     *                          a button
     * @return                  a system state represented by the button
     *                          of the button resource identifier
     */
    @NonNull
    public static SystemState getState(int stateButtonId) {
        final int startedStateButton = R.id.started_state_button;
        final int demoStateButton = R.id.demo_state_button;
        switch (stateButtonId) {
            case startedStateButton:
                return SystemState.STARTED;
            case demoStateButton:
                return SystemState.DEMO;
            default:
                return SystemState.STOPPED;
        }
    }
}
package com.example.freeturilo.connection;

import android.app.Activity;
import android.content.Intent;

import com.example.freeturilo.activities.ErrorActivity;
import com.example.freeturilo.core.ErrorType;

/**
 * A handler for errors in Freeturilo API transactions compatible with
 * activities.
 * <p>
 * Object of this class handles errors occurring in Freeturilo API transactions
 * that have been performed with the use of an {@link #activity}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #activity
 * @see #finishActivity
 * @see #handle
 * @see APIHandler
 * @see APIException
 * @see APIRunnable
 * @see Activity
 */
public class APIActivityHandler implements APIHandler {
    /**
     * Stores the activity which has been used to perform the transaction.
     */
    private final Activity activity;
    /**
     * Stores the flag defining whether to finish the activity after handling
     * an error.
     */
    private final boolean finishActivity;

    /**
     * Class constructor.
     * @param activity          an activity which typically is the activity
     *                          which has been used to perform the transaction
     * @param finishActivity    a boolean defining whether to finish the
     *                          activity after handling the error
     */
    public APIActivityHandler(Activity activity, boolean finishActivity) {
        this.activity = activity;
        this.finishActivity = finishActivity;
    }

    /**
     * Handles an error in a Freeturilo API transaction. Displays an
     * {@code ErrorActivity} customized for the {@code APIException}. Then
     * finishes the {@code activity} if {@code finishActivity} is set to true.
     * @param error     an error that occurred in an API transaction
     * @see #activity
     * @see #finishActivity
     * @see ErrorActivity
     * @see ErrorType
     * @see APIException
     */
    public void handle(APIException error) {
        ErrorType errorType = ErrorType.getType(error.responseCode);
        Intent intent = new Intent(activity, ErrorActivity.class);
        intent.putExtra(ErrorActivity.ERROR_TYPE_INTENT, errorType);
        activity.startActivity(intent);
        if (finishActivity)
            activity.finish();
    }
}

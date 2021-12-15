package com.example.freeturilo.connection;

import android.app.Activity;
import android.content.Intent;

import com.example.freeturilo.activities.ErrorActivity;
import com.example.freeturilo.core.ErrorType;

public class APIActivityHandler implements APIHandler {
    private final Activity activity;
    private final boolean finishActivity;

    public APIActivityHandler(Activity activity) {
        this(activity, false);
    }

    public APIActivityHandler(Activity activity, boolean finishActivity) {
        this.activity = activity;
        this.finishActivity = finishActivity;
    }

    public void handle(APIException e) {
        ErrorType errorType = ErrorType.getType(e.responseCode);
        Intent intent = new Intent(activity, ErrorActivity.class);
        intent.putExtra(ErrorActivity.ERROR_TYPE_INTENT, errorType);
        activity.startActivity(intent);
        if (finishActivity)
            activity.finish();
    }
}

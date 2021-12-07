package com.example.freeturilo.connection;

import android.content.Context;
import android.content.Intent;

import com.example.freeturilo.R;
import com.example.freeturilo.activities.ErrorActivity;
import com.example.freeturilo.core.ErrorType;

public class APIActivityHandler implements APIHandler {
    final private Context context;

    public APIActivityHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handle(APIException e) {
        ErrorType errorType = ErrorType.getType(e.responseCode);
        Intent intent = new Intent(context, ErrorActivity.class);
        intent.putExtra(context.getString(R.string.error_type_intent_name), errorType);
        context.startActivity(intent);
    }
}

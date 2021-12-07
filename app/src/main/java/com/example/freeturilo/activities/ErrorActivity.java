package com.example.freeturilo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freeturilo.R;
import com.example.freeturilo.core.ErrorType;

public class ErrorActivity extends AppCompatActivity {
    public static final String ERROR_TYPE_INTENT = "error_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ErrorType errorType = (ErrorType) getIntent().getSerializableExtra(ERROR_TYPE_INTENT);
        ImageView errorImage = findViewById(R.id.error_image);
        TextView errorSecondaryText = findViewById(R.id.error_secondary_text);
        errorImage.setImageBitmap(ErrorType.getTypeImage(this, errorType));
        errorSecondaryText.setText(ErrorType.getTypeText(this, errorType));
    }
}
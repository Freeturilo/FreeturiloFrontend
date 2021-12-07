package com.example.freeturilo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freeturilo.R;
import com.example.freeturilo.core.ErrorType;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ErrorType errorType = (ErrorType) getIntent()
                .getSerializableExtra(getString(R.string.error_type_intent_name));
        ImageView errorImage = findViewById(R.id.error_image);
        errorImage.setImageBitmap(ErrorType.getTypeImage(this, errorType));
        TextView errorSecondaryText = findViewById(R.id.error_secondary_text);
        errorSecondaryText.setText(ErrorType.getTypeText(this, errorType));
    }
}
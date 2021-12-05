package com.example.freeturilo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freeturilo.R;
import com.example.freeturilo.core.ErrorType;
import com.example.freeturilo.core.ErrorTypeTools;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ErrorType errorType = (ErrorType) getIntent()
                .getSerializableExtra(getString(R.string.error_type_intent_name));
        ImageView errorImage = findViewById(R.id.error_image);
        errorImage.setImageBitmap(ErrorTypeTools.getTypeImage(this, errorType));
        TextView errorSecondaryText = findViewById(R.id.error_secondary_text);
        errorSecondaryText.setText(ErrorTypeTools.getTypeText(this, errorType));
    }
}
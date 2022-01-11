package com.example.freeturilo.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freeturilo.R;
import com.example.freeturilo.core.ErrorType;

/**
 * An activity displaying an error view.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FreeturiloActivity
 * @see ErrorType
 */
public class ErrorActivity extends FreeturiloActivity {
    /**
     * Stores the name of the {@code ErrorType} in the {@code Intent} used to
     * create this activity.
     */
    public static final String ERROR_TYPE_INTENT = "error_type";

    /**
     * Called when this activity is created.
     * <p>
     * Calls {@link FreeturiloActivity#onCreate}. Initializes the layout of
     * this activity customized for {@code ErrorType} passed in the
     * {@code Intent}.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with
     *                              {@code AppCompatActivity}
     */
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
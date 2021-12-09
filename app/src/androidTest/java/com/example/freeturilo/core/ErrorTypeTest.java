package com.example.freeturilo.core;

import static org.junit.Assert.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.freeturilo.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

@RunWith(Parameterized.class)
public class ErrorTypeTest {

    @Parameterized.Parameters
    public static Iterable<Object[]> types() {
        return Arrays.asList(new Object[][] {
                {-1, ErrorType.NETWORK, R.string.error_network_text, R.drawable.image_error_network},
                {HttpsURLConnection.HTTP_UNAUTHORIZED, ErrorType.AUTH, R.string.error_auth_text, R.drawable.image_error_auth},
                {HttpsURLConnection.HTTP_UNAVAILABLE, ErrorType.STOPPED, R.string.error_stopped_text, R.drawable.image_error_stopped},
                {613, ErrorType.SERVER, R.string.error_server_text, R.drawable.image_error_server}
        });
    }

    private final int responseCode;
    private final ErrorType errorType;
    private final int typeTextId;
    private final int typeImageId;

    public ErrorTypeTest(int responseCode, ErrorType errorType, int typeTextId, int typeImageId) {
        this.responseCode = responseCode;
        this.errorType = errorType;
        this.typeTextId = typeTextId;
        this. typeImageId = typeImageId;
    }

    @Test
    public void getTypeText() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        String result = ErrorType.getTypeText(context, errorType);

        assertEquals(context.getString(typeTextId), result);
    }

    @Test
    public void getTypeImage() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int errorImageSize = context.getResources().getDimensionPixelSize(R.dimen.error_image_size);
        Bitmap expected = BitmapFactory.decodeResource(context.getResources(), typeImageId);
        expected = Bitmap.createScaledBitmap(expected, errorImageSize, errorImageSize, false);

        Bitmap result = ErrorType.getTypeImage(context, errorType);

        assertTrue(expected.sameAs(result));
        assertEquals(errorImageSize, result.getWidth());
        assertEquals(errorImageSize, result.getHeight());
    }

    @Test
    public void getType() {
        ErrorType result = ErrorType.getType(responseCode);

        assertEquals(errorType, result);
    }
}
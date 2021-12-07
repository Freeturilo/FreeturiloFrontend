package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;

import javax.net.ssl.HttpsURLConnection;

public enum ErrorType {
    NETWORK, AUTH, STOPPED, SERVER;

    @NonNull
    public static String getTypeText(@NonNull Context context, @NonNull ErrorType errorType) {
        switch (errorType) {
            case NETWORK:
                return context.getString(R.string.error_network_text);
            case AUTH:
                return context.getString(R.string.error_auth_text);
            case STOPPED:
                return context.getString(R.string.error_stopped_text);
            default:
                return context.getString(R.string.error_server_text);
        }
    }

    public static int getTypeImageId(@NonNull ErrorType errorType) {
        switch (errorType) {
            case NETWORK:
                return R.drawable.image_error_network;
            case AUTH:
                return R.drawable.image_error_auth;
            case STOPPED:
                return R.drawable.image_error_stopped;
            default:
                return R.drawable.image_error_server;
        }
    }

    @NonNull
    public static Bitmap getTypeImage(@NonNull Context context, @NonNull ErrorType errorType) {
        Bitmap errorImage = BitmapFactory.decodeResource(context.getResources(), getTypeImageId(errorType));
        int errorImageSize = context.getResources().getDimensionPixelSize(R.dimen.error_image_size);
        return Bitmap.createScaledBitmap(errorImage, errorImageSize, errorImageSize, false);
    }

    @NonNull
    public static ErrorType getType(int responseCode) {
        switch (responseCode) {
            case -1:
                return NETWORK;
            case HttpsURLConnection.HTTP_UNAUTHORIZED:
                return AUTH;
            case HttpsURLConnection.HTTP_UNAVAILABLE:
                return STOPPED;
            default:
                return SERVER;
        }
    }
}

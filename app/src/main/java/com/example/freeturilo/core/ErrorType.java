package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;

import javax.net.ssl.HttpsURLConnection;

/**
 * Type of an error in communication with Freeturilo API.
 * Values of this enum represent different types of errors that may occur
 * when communicating with Freeturilo API. There are multiple static methods
 * declared within the enum that help represent an error with text or an
 * image.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #NETWORK
 * @see #AUTH
 * @see #STOPPED
 * @see #SERVER
 * @see #getTypeTextId
 * @see #getTypeText
 * @see #getTypeImageId
 * @see #getTypeImage
 * @see #getType
 */
public enum ErrorType {
    /**
     * Occurs when application encounters problems when trying to establish
     * connection with Freeturilo API. Should usually be caused by no internet.
     */
    NETWORK,
    /**
     * Occurs when Freeturilo API refuses access to an endpoint because of bad
     * or no authorization.
     */
    AUTH,
    /**
     * Occurs when Freeturilo system has been stopped and an unauthorized
     * (non-admin) user tries to send requests to Freeturilo API.
     * @see SystemState#STOPPED
     */
    STOPPED,
    /**
     * Occurs when Freeturilo API responds with an unexpected HTTP code.
     */
    SERVER;

    /**
     * Gets the resource identifier of the text description of
     * an {@code ErrorType}.
     * @param errorType     an error type
     * @return              an integer identifying the text describing
     *                      the error type
     */
    private static int getTypeTextId(@NonNull ErrorType errorType) {
        switch (errorType) {
            case NETWORK:
                return R.string.error_network_text;
            case AUTH:
                return R.string.error_auth_text;
            case STOPPED:
                return R.string.error_stopped_text;
            default:
                return R.string.error_server_text;
        }
    }

    /**
     * Gets the text description of an {@code ErrorType}.
     * @param context       the context of the application providing all
     *                      global information
     * @param errorType     an error type
     * @return              a string describing the error type
     * @see Context
     */
    @NonNull
    public static String getTypeText(@NonNull Context context, @NonNull ErrorType errorType) {
        return context.getString(getTypeTextId(errorType));
    }

    /**
     * Gets the resource identifier of the image representing an
     * {@code ErrorType}.
     * @param errorType     an error type
     * @return              an integer identifying the image representing
     *                      the error type
     */
    private static int getTypeImageId(@NonNull ErrorType errorType) {
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

    /**
     * Gets the image representing an {@code ErrorType}.
     * @param context       the context of the application providing all
     *                      global information
     * @param errorType     an error type
     * @return              a bitmap containing the image
     * @see Context
     */
    @NonNull
    public static Bitmap getTypeImage(@NonNull Context context, @NonNull ErrorType errorType) {
        Bitmap errorImage = BitmapFactory.decodeResource(context.getResources(), getTypeImageId(errorType));
        int errorImageSize = context.getResources().getDimensionPixelSize(R.dimen.error_image_size);
        return Bitmap.createScaledBitmap(errorImage, errorImageSize, errorImageSize, false);
    }

    /**
     * Gets the error type defined by a response code.
     * @param responseCode      an integer equal to HTTP response code if
     *                          connection has been properly established,
     *                          and to -1 if not
     * @return                  an error type defined by the response code
     */
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

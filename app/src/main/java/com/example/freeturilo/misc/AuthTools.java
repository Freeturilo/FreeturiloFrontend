package com.example.freeturilo.misc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A collection of static methods and variables used for authorization.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #token
 * @see #setToken
 * @see #getToken
 * @see #isLoggedIn
 */
public class AuthTools {
    /**
     * Stores an administrator authorization token used as a header value in
     * data transaction with API.
     */
    private static String token;

    /**
     * Sets the administrator authorization {@link #token}.
     * @param token     a non-null string equal to a valid authorization token
     */
    public static void setToken(@NonNull String token) {
        AuthTools.token = token;
    }

    /**
     * Gets the administrator authorization {@link #token}.
     * @return          a string equal to an authorization token or null if
     *                  user is not logged in
     * @see #isLoggedIn
     */
    @Nullable
    public static String getToken() {
        return token;
    }

    /**
     * Checks if user is logged in as an administrator.
     * @return          a boolean indicating whether the user is logged in
     */
    public static boolean isLoggedIn() {
        return token != null;
    }
}

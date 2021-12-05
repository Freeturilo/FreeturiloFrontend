package com.example.freeturilo.misc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AuthTools {
    private static String token;

    public static void setToken(@NonNull String token) {
        AuthTools.token = token;
    }

    @Nullable
    public static String getToken() {
        return token;
    }

    public static boolean isLoggedIn() {
        return token != null;
    }
}

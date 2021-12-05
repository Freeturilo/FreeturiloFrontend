package com.example.freeturilo.misc;

public class AuthTools {
    private static String token;

    public static void setToken(String token) {
        AuthTools.token = token;
    }

    public static boolean isLoggedIn() {
        return token != null;
    }

    public static String getToken() {
        return token;
    }
}

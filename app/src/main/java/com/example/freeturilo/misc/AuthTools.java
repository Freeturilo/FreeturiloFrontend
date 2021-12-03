package com.example.freeturilo.misc;

public class AuthTools {
    private static String token;

    public static boolean login(String email, String password) {
        token = "TOKENEXAMPLE";
        return true;
    }

    public static boolean isLoggedIn() {
        return token != null;
    }

    public static String getToken() {
        return token;
    }
}

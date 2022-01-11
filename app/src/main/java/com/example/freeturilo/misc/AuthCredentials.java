package com.example.freeturilo.misc;

/**
 * A bundle of authentication credentials.
 * <p>
 * Object of this class represents a bundle of credentials used in
 * authentication of a system administrator. It encapsulates an {@link #email}
 * and a {@link #password}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 */
public class AuthCredentials {
    /**
     * Stores the email address used for authentication.
     */
    public final String email;
    /**
     * Stores the password used for authentication.
     */
    public final String password;

    /**
     * Class constructor.
     * @param email     a string equal to an email address
     * @param password  a string equal to a password
     */
    public AuthCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

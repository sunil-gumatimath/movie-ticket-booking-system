package com.example.movieticketbookingsystem.utility;

import java.util.Locale;

public final class Emails {

    private Emails() {
    }

    /** Canonical form used for storing and looking up account emails. */
    public static String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

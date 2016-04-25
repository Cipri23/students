package com.studios.lucian.students.util;

import android.util.Log;

/**
 * Created with Love by Lucian and Pi on 26.03.2016.
 */
public class Validator {

    private static final String NAME_FORMAT_REGEX = "[a-zA-z]+([ '-][a-zA-Z]+)*";
    private static final int MATRICOL_MAX_LENGTH = 7;

    public static boolean isValidMatricol(String s) {
        try {
            if (s.length() > MATRICOL_MAX_LENGTH) return false;
            Long.parseLong(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isValidName(String s) {
        return s.matches(NAME_FORMAT_REGEX);
    }

    public static boolean isValidGroupNumber(String mGroupNumber) {
        try {
            int x = Integer.parseInt(mGroupNumber);
            return x > 0 && x < 10000;
        } catch (Exception ex) {
            Log.i("Validator", "isValidGroupNumber: false");
            return false;
        }
    }
}

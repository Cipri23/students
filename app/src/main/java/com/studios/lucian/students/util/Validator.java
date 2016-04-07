package com.studios.lucian.students.util;

/**
 * Created with Love by Lucian and Pi on 26.03.2016.
 */
public class Validator {

    private static String NAME_FORMAT_REGEX = "[a-zA-z]+([ '-][a-zA-Z]+)*";
    private static int MATRICOL_MAX_LENGTH = 7;

    public boolean isValidMatricol(String s) {
        try {
            if (s.length() > MATRICOL_MAX_LENGTH) return false;
            long matricol = Long.parseLong(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isValidName(String s) {
        return s.matches(NAME_FORMAT_REGEX);
    }
}

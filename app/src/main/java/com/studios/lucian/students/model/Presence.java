package com.studios.lucian.students.model;

import android.content.ContentValues;

/**
 * Created with Love by Lucian and Pi on 20.06.2016.
 */
public class Presence {
    private final String matricol;
    private final int labNumber;
    private final String date;

    public Presence(String matricol, int value, String date) {
        this.matricol = matricol;
        this.labNumber = value;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Presence{" +
                "matricol='" + matricol + '\'' +
                ", labNumber=" + labNumber +
                ", date='" + date + '\'' +
                '}';
    }

    public String getMatricol() {
        return matricol;
    }

    public int getLabNumber() {
        return labNumber;
    }

    public String getDate() {
        return date;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        final String COLUMN_PRESENCE_MATRICOL = "matricol";
        final String COLUMN_PRESENCE_LAB_NR = "lab";
        final String COLUMN_PRESENCE_DATE = "date";

        contentValues.put(COLUMN_PRESENCE_MATRICOL, matricol);
        contentValues.put(COLUMN_PRESENCE_LAB_NR, labNumber);
        contentValues.put(COLUMN_PRESENCE_DATE, date);

        return contentValues;
    }

    public String getToastDisplay(Student student) {
        return student.toString() + " was marked as present.";
    }
}

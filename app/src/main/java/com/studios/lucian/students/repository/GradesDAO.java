package com.studios.lucian.students.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.studios.lucian.students.model.Grade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 09.04.2016.
 */
public class GradesDAO extends DataBaseHelper {
    private static final String TAG = GradesDAO.class.getSimpleName();

    private static final String TABLE_NAME_GRADE = "grade";
    private static final String COLUMN_GRADE_MATRICOL = "matricol";
    private static final String COLUMN_GRADE_NOTE = "note";
    private static final String COLUMN_GRADE_LAB_NR = "lab";
    private static final String COLUMN_GRADE_DATE = "date";

    public GradesDAO(Context context) {
        super(context);
    }

    public boolean add(Grade grade) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GRADE_MATRICOL, grade.getMatricol());
        contentValues.put(COLUMN_GRADE_NOTE, grade.getGrade());
        contentValues.put(COLUMN_GRADE_LAB_NR, grade.getLabNumber());
        contentValues.put(COLUMN_GRADE_DATE, grade.getDate());

        SQLiteDatabase database = getWritableDatabase();
        long result = database.insert(TABLE_NAME_GRADE, null, contentValues);
        database.close();
        if (result == -1) {
            Log.i(TAG, "Grade not Added: " + grade.toString());
            return false;
        }
        Log.i(TAG, "Grade Added: " + grade.toString());
        return true;
    }

    public List<Grade> getStudentGrades(String matricol) {
        List<Grade> grades = new ArrayList<>();
        try {
            SQLiteDatabase database = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_GRADE + " WHERE " + COLUMN_GRADE_MATRICOL + " =?";
            Cursor cursor = database.rawQuery(query, new String[]{matricol});

            while (cursor.moveToNext()) {
                int grade = cursor.getInt(cursor.getColumnIndex(COLUMN_GRADE_NOTE));
                int labNr = cursor.getInt(cursor.getColumnIndex(COLUMN_GRADE_LAB_NR));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_GRADE_DATE));
                grades.add(new Grade(matricol, grade, labNr, date));
            }
            if (!cursor.isClosed()) cursor.close();
            if (database.isOpen()) database.close();
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
        }
        return grades;
    }
}

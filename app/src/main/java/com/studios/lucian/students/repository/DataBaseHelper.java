package com.studios.lucian.students.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with Love by Lucian and Pi on 09.04.2016.
 */
class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DataBaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StudentManagement.db";

    private static final String TABLE_NAME_STUDENT = "student";
    private static final String TABLE_NAME_GRADE = "grade";

    // TABLE #STUDENT#
    private static final String COLUMN_STUDENT_MATRICOL = "matricol";
    private static final String COLUMN_STUDENT_GROUP_NUMBER = "groupnumber";
    private static final String COLUMN_STUDENT_NAME = "name";
    private static final String COLUMN_STUDENT_SURNAME = "surname";
    private static final String COLUMN_STUDENT_DRIVE_FILE_ID = "driveid";

    // TABLE #GRADE#
    private static final String COLUMN_GRADE_MATRICOL = "matricol";
    private static final String COLUMN_GRADE_NOTE = "note";
    private static final String COLUMN_GRADE_LAB_NR = "lab";
    private static final String COLUMN_GRADE_DATE = "date";

    private static final String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_NAME_STUDENT + " (" +
            COLUMN_STUDENT_MATRICOL + " TEXT PRIMARY KEY," +
            COLUMN_STUDENT_GROUP_NUMBER + " TEXT," +
            COLUMN_STUDENT_NAME + " TEXT," +
            COLUMN_STUDENT_SURNAME + " TEXT, " +
            COLUMN_STUDENT_DRIVE_FILE_ID + " TEXT" + ");";

    private static final String CREATE_TABLE_GRADE = "CREATE TABLE " + TABLE_NAME_GRADE + " (" +
            COLUMN_GRADE_MATRICOL + " TEXT, " +
            COLUMN_GRADE_NOTE + " INTEGER," +
            COLUMN_GRADE_LAB_NR + " INTEGER," +
            COLUMN_GRADE_DATE + " TEXT," +
            "FOREIGN KEY(" + COLUMN_GRADE_MATRICOL + ") " +
            "REFERENCES " + TABLE_NAME_STUDENT + "(" + COLUMN_STUDENT_MATRICOL + "));";

    private static final String DROP_TABLE_STUDENT = "DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT;
    private static final String DROP_TABLE_GRADE = "DROP TABLE IF EXISTS " + TABLE_NAME_GRADE;

    DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE_STUDENT);
            sqLiteDatabase.execSQL(CREATE_TABLE_GRADE);
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL(DROP_TABLE_GRADE);
            sqLiteDatabase.execSQL(DROP_TABLE_STUDENT);
            onCreate(sqLiteDatabase);
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
        }
    }
}

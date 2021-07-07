package com.studios.ciprian.students.repository;

// lucram direct cu baza de date..DAO- urile apeleaza metode din DataBaseHelper care lucreaza direct cu baza de date
// si de acolo se aud studentii si prezentele
// locul de unde aducem date din baza de date al tel
// facem un querry(cerere) catre baza de date


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DataBaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StudentManagement.db";

    private static final String TABLE_NAME_GROUP = "groupnr";
    private static final String TABLE_NAME_STUDENT = "student";
    private static final String TABLE_NAME_GRADE = "grade";
    private static final String TABLE_NAME_PRESENCE = "presence";

    // TABLE #GROUP#
    private static final String COLUMN_GROUP_NUMBER = "number";
    private static final String COLUMN_GROUP_DRIVE_ID = "driveid";
    private static final String COLUMN_GROUP_COUNT = "count";

    // TABLE #STUDENT#
    private static final String COLUMN_STUDENT_MATRICOL = "matricol";
    private static final String COLUMN_STUDENT_GROUP_NUMBER = "groupnumber";
    private static final String COLUMN_STUDENT_NAME = "name";
    private static final String COLUMN_STUDENT_SURNAME = "surname";

    // TABLE #GRADE#
    private static final String COLUMN_GRADE_MATRICOL = "matricol";
    private static final String COLUMN_GRADE_NOTE = "note";
    private static final String COLUMN_GRADE_LAB_NR = "lab";
    private static final String COLUMN_GRADE_DATE = "date";

    // TABLE #PRESENCE#
    private static final String COLUMN_PRESENCE_MATRICOL = "matricol";
    private static final String COLUMN_PRESENCE_LAB_NR = "lab";
    private static final String COLUMN_PRESENCE_DATE = "date";

    private static final String CREATE_TABLE_GROUP = "CREATE TABLE " + TABLE_NAME_GROUP + " (" +
            COLUMN_GROUP_NUMBER + " TEXT PRIMARY KEY," +
            COLUMN_GROUP_COUNT + " INTEGER, " +
            COLUMN_GROUP_DRIVE_ID + " TEXT);";

    private static final String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_NAME_STUDENT + " (" +
            COLUMN_STUDENT_MATRICOL + " TEXT PRIMARY KEY," +
            COLUMN_STUDENT_GROUP_NUMBER + " TEXT," +
            COLUMN_STUDENT_NAME + " TEXT," +
            COLUMN_STUDENT_SURNAME + " TEXT," +
            "FOREIGN KEY(" + COLUMN_STUDENT_GROUP_NUMBER + ") " +
            "REFERENCES " + TABLE_NAME_GROUP + "(" + COLUMN_GROUP_NUMBER + "));";

    private static final String CREATE_TABLE_GRADE = "CREATE TABLE " + TABLE_NAME_GRADE + " (" +
            COLUMN_GRADE_MATRICOL + " TEXT, " +
            COLUMN_GRADE_NOTE + " INTEGER," +
            COLUMN_GRADE_LAB_NR + " INTEGER," +
            COLUMN_GRADE_DATE + " TEXT," +
            "FOREIGN KEY(" + COLUMN_GRADE_MATRICOL + ") " +
            "REFERENCES " + TABLE_NAME_STUDENT + "(" + COLUMN_STUDENT_MATRICOL + "));";

    private static final String CREATE_TABLE_PRESENCE = "CREATE TABLE " + TABLE_NAME_PRESENCE + " (" +
            COLUMN_PRESENCE_MATRICOL + " TEXT, " +
            COLUMN_PRESENCE_LAB_NR + " INTEGER," +
            COLUMN_PRESENCE_DATE + " TEXT," +
            "FOREIGN KEY(" + COLUMN_PRESENCE_MATRICOL + ") " +
            "REFERENCES " + TABLE_NAME_STUDENT + "(" + COLUMN_STUDENT_MATRICOL + "));";

    private static final String DROP_TABLE_GROUP = "DROP TABLE IF EXISTS " + TABLE_NAME_GROUP;
    private static final String DROP_TABLE_STUDENT = "DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT;
    private static final String DROP_TABLE_GRADE = "DROP TABLE IF EXISTS " + TABLE_NAME_GRADE;
    private static final String DROP_TABLE_PRESENCE = "DROP TABLE IF EXISTS " + TABLE_NAME_PRESENCE;

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
            sqLiteDatabase.execSQL(CREATE_TABLE_GROUP);
            sqLiteDatabase.execSQL(CREATE_TABLE_STUDENT);
            sqLiteDatabase.execSQL(CREATE_TABLE_GRADE);
            sqLiteDatabase.execSQL(CREATE_TABLE_PRESENCE);
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL(DROP_TABLE_GRADE);
            sqLiteDatabase.execSQL(DROP_TABLE_PRESENCE);
            sqLiteDatabase.execSQL(DROP_TABLE_STUDENT);
            sqLiteDatabase.execSQL(DROP_TABLE_GROUP);
            onCreate(sqLiteDatabase);
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
        }
    }
}
package com.studios.lucian.students.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.studios.lucian.students.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 03.03.2016.
 */
public class StudentDAO extends SQLiteOpenHelper {

    private static final String TAG = StudentDAO.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StudentManagement.db";

    private static final String TABLE_NAME_STUDENT = "student";
    private static final String COLUMN_NAME_STUDENT_ID = "studentid";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_SURNAME = "surname";


    public StudentDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME_STUDENT + " (" +
                COLUMN_NAME_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_SURNAME + " TEXT" + ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    public void add(Student student) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_NAME, student.getName());
        contentValues.put(COLUMN_NAME_SURNAME, student.getSurname());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(TABLE_NAME_STUDENT, null, contentValues);
        database.close();
        Log.v(TAG, "Student Added: " + student.toString());
    }

    public void delete(Student student) {
        SQLiteDatabase database = getWritableDatabase();
        int affectedRows = database.delete(TABLE_NAME_STUDENT,
                COLUMN_NAME_NAME + " = ? AND " + COLUMN_NAME_SURNAME + " = ?",
                new String[]{student.getName(), student.getSurname()});
        Log.v(TAG, "Student Deleted: " + student.toString() + ". Affected Rows: " + affectedRows);
    }

    public List<Student> getAll() {
        List<Student> studentList = new ArrayList<>();
        String name, surname;
        int retrievedRows;
        SQLiteDatabase database = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_STUDENT;
        Cursor cursor = database.rawQuery(query, null);
        retrievedRows = cursor.getCount();
        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
            surname = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SURNAME));
            studentList.add(new Student(name, surname));
        }
        cursor.close();
        database.close();

        Log.v(TAG, "Number of retrieved rows: " + retrievedRows);
        Log.v(TAG, Arrays.toString(studentList.toArray()));
        return studentList;
    }
}

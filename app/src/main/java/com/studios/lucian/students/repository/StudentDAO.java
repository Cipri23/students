package com.studios.lucian.students.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 03.03.2016.
 */
public class StudentDAO extends SQLiteOpenHelper {
    private static final String TAG = StudentDAO.class.getSimpleName();
    private static final int ERROR_CODE = -1;
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "StudentManagement.db";
    private static final String TABLE_NAME_STUDENT = "student";
    private static final String COLUMN_NAME_STUDENT_ID = "studentid";
    private static final String COLUMN_NAME_GROUP_NUMBER = "groupnumber";
    private static final String COLUMN_NAME_MATRICOL = "matricol";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_SURNAME = "surname";

    public StudentDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME_STUDENT + " (" +
                COLUMN_NAME_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_GROUP_NUMBER + " TEXT," +
                COLUMN_NAME_MATRICOL + " TEXT," +
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
        contentValues.put(COLUMN_NAME_GROUP_NUMBER, student.getGroupNumber());
        contentValues.put(COLUMN_NAME_MATRICOL, student.getMatricol());
        contentValues.put(COLUMN_NAME_NAME, student.getName());
        contentValues.put(COLUMN_NAME_SURNAME, student.getSurname());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(TABLE_NAME_STUDENT, null, contentValues);
        database.close();
        Log.v(TAG, "Student Added: " + student.toString());
    }

    public int delete(Student student) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            int affectedRows = database.delete(TABLE_NAME_STUDENT, COLUMN_NAME_MATRICOL + " = ?",
                    new String[]{student.getMatricol()});
            if (database.isOpen()) {
                database.close();
            }
            Log.v(TAG, "delete result: " + affectedRows);
            return affectedRows;
        } catch (SQLiteException ex) {
            Log.v(TAG, ex.getMessage());
        }
        return ERROR_CODE;
    }

    public List<Student> getAll() {
        List<Student> studentList = new ArrayList<>();
        String groupNumber, matricol, name, surname;
        int retrievedRows;
        SQLiteDatabase database = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_STUDENT;
        Cursor cursor = database.rawQuery(query, null);
        retrievedRows = cursor.getCount();

        while (cursor.moveToNext()) {
            groupNumber = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GROUP_NUMBER));
            matricol = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MATRICOL));
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
            surname = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SURNAME));
            studentList.add(new Student(groupNumber, matricol, name, surname));
            Log.v(TAG, "Student: " + groupNumber + " " + matricol + " " + name + " " + surname);
        }
        cursor.close();
        database.close();

        Log.v(TAG, "Number of retrieved rows: " + retrievedRows);
        return studentList;
    }

    public boolean find(Student student) {
        try {
            SQLiteDatabase database = getReadableDatabase();
            long result = DatabaseUtils.queryNumEntries(
                    database,
                    TABLE_NAME_STUDENT,
                    COLUMN_NAME_MATRICOL + " = ?",
                    new String[]{student.getMatricol()}
            );

            if (result == 1) return true;
            if (database.isOpen()) database.close();
        } catch (SQLiteException ex) {
            Log.v(TAG, ex.getMessage());
        }
        return false;
    }

    public int update(Student student) {
        ContentValues contentValues = new ContentValues();
        String WHERE_CLAUSE = COLUMN_NAME_MATRICOL + "=" + student.getMatricol();
        try {
            SQLiteDatabase database = getWritableDatabase();

            contentValues.put(COLUMN_NAME_NAME, student.getName());
            contentValues.put(COLUMN_NAME_SURNAME, student.getSurname());
            int affectedRows = database.update(TABLE_NAME_STUDENT, contentValues,
                    COLUMN_NAME_MATRICOL + " = ?", new String[]{student.getMatricol()});
            if (database.isOpen()) database.close();
            return affectedRows;
        } catch (SQLiteException ex) {
            Log.v(TAG, ex.getMessage());
        }
        return ERROR_CODE;
    }

    public List<Group> getAllGroups() {
        List<Group> groupList = new ArrayList<>();
        String queryGroups = "SELECT DISTINCT " + COLUMN_NAME_GROUP_NUMBER + " FROM " + TABLE_NAME_STUDENT;
        String groupNumber;
        long studentsCount;

        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery(queryGroups, null);
            while (cursor.moveToNext()) {
                groupNumber = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GROUP_NUMBER));
                studentsCount = DatabaseUtils.queryNumEntries(
                        database,
                        TABLE_NAME_STUDENT,
                        COLUMN_NAME_GROUP_NUMBER + " = ?",
                        new String[]{groupNumber}
                );
                Group group = new Group(groupNumber, (int) studentsCount);
                groupList.add(group);
            }

            if (!cursor.isClosed()) cursor.close();
            if (database.isOpen()) database.close();
        } catch (SQLiteException ex) {
            Log.v(TAG, ex.getMessage());
        }
        return groupList;
    }

    public List<Student> getStudentsFromGroup(String groupNumber) {
        List<Student> studentList = new ArrayList<>();
        String matricol, name, surname;
        int retrievedRows;
        SQLiteDatabase database = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_STUDENT + " WHERE " + COLUMN_NAME_GROUP_NUMBER + "=" + groupNumber;
        Cursor cursor = database.rawQuery(query, null);
        retrievedRows = cursor.getCount();

        while (cursor.moveToNext()) {
            matricol = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MATRICOL));
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
            surname = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SURNAME));
            studentList.add(new Student(groupNumber, matricol, name, surname));
            Log.v(TAG, "Student: " + groupNumber + " " + matricol + " " + name + " " + surname);
        }
        cursor.close();
        database.close();

        Log.v(TAG, "Number of retrieved rows: " + retrievedRows);
        return studentList;
    }
}

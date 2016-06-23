package com.studios.lucian.students.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 03.03.2016.
 */
public class StudentDAO extends DataBaseHelper {
    private static final String TAG = StudentDAO.class.getSimpleName();
    private static final int ERROR_CODE = -1;

    private static final String TABLE_NAME_STUDENT = "student";
    private static final String COLUMN_NAME_STUDENT_ID = "studentid";
    private static final String COLUMN_NAME_GROUP_NUMBER = "groupnumber";
    private static final String COLUMN_NAME_MATRICOL = "matricol";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_SURNAME = "surname";

    public StudentDAO(Context context) {
        super(context);
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
        Log.i(TAG, "Student Added: " + student.toString());
    }

    public int delete(Student student) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            int affectedRows = database.delete(TABLE_NAME_STUDENT, COLUMN_NAME_MATRICOL + " = ?",
                    new String[]{student.getMatricol()});
            if (database.isOpen()) {
                database.close();
            }
            Log.i(TAG, "delete result: " + affectedRows);
            return affectedRows;
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
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
            Log.i(TAG, "Student: " + groupNumber + " " + matricol + " " + name + " " + surname);
        }
        cursor.close();
        database.close();

        Log.i(TAG, "Number of retrieved rows: " + retrievedRows);
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
            Log.i(TAG, ex.getMessage());
        }
        return false;
    }

    public Student find(String matricol) {
        Student student;
        try {
            SQLiteDatabase database = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_STUDENT + " WHERE " + COLUMN_NAME_MATRICOL + " =?";
            Cursor cursor = database.rawQuery(query, new String[]{matricol});

            if (cursor.getCount() != 1) {
                Log.i(TAG, "find student with matricol as parameter not returned exactly one student." +
                        "Matricol: " + matricol);
                return null;
            }
            cursor.moveToFirst();
            String groupNumber = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GROUP_NUMBER));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME));
            String surname = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SURNAME));
            student = new Student(groupNumber, matricol, name, surname);

            if (!cursor.isClosed()) cursor.close();
            if (database.isOpen()) database.close();
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
            return null;
        }
        return student;
    }

    public int update(Student student) {
        ContentValues contentValues = new ContentValues();
        try {
            SQLiteDatabase database = getWritableDatabase();

            contentValues.put(COLUMN_NAME_NAME, student.getName());
            contentValues.put(COLUMN_NAME_SURNAME, student.getSurname());
            int affectedRows = database.update(TABLE_NAME_STUDENT, contentValues,
                    COLUMN_NAME_MATRICOL + " = ?", new String[]{student.getMatricol()});
            if (database.isOpen()) database.close();
            return affectedRows;
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
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
            Log.i(TAG, ex.getMessage());
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
            Log.i(TAG, "Student: " + groupNumber + " " + matricol + " " + name + " " + surname);
        }
        cursor.close();
        database.close();

        Log.i(TAG, "Number of retrieved rows: " + retrievedRows);
        return studentList;
    }

//    public String getGroupDriveFileId(String mGroupNumber) {
//        String query = "SELECT " + COLUMN_NAME_DRIVE_FILE_ID + " FROM " + TABLE_NAME_STUDENT + " WHERE " + COLUMN_NAME_GROUP_NUMBER + "=" + mGroupNumber;
//        Cursor cursor = getReadableDatabase().rawQuery(query, null);
//        cursor.moveToFirst();
//        String fileDriveId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DRIVE_FILE_ID));
//
//        if (!cursor.isClosed()) cursor.close();
//        return fileDriveId;
//    }

//    public void setGroupDriveId(String groupDriveId, String groupNumber) {
//        ContentValues contentValues = new ContentValues();
//        try {
//            SQLiteDatabase database = getWritableDatabase();
//            contentValues.put(COLUMN_NAME_DRIVE_FILE_ID, groupDriveId);
//            int affectedRows = database.update(TABLE_NAME_STUDENT, contentValues,
//                    COLUMN_NAME_GROUP_NUMBER + " = ?", new String[]{groupNumber});
//            Log.i(TAG, "setGroupDriveId: affectedRows = " + affectedRows);
//        } catch (SQLiteException ex) {
//            Log.i(TAG, "setGroupDriveId: " + ex.getMessage());
//        }
//    }
}

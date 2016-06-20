package com.studios.lucian.students.model;

import android.content.ContentValues;

/**
 * Created with Love by Lucian and Pi on 04.04.2016.
 */
public class Group {
    private String number;
    private String driveFileId;
    private int studentCount;

    public Group(String number, String driveFileId, int studentCount) {
        this.number = number;
        this.driveFileId = driveFileId;
        this.studentCount = studentCount;
    }

    public Group(String number, int studentCount) {
        this.number = number;
        this.studentCount = studentCount;
        this.driveFileId = "";
    }

    @Override
    public String toString() {
        return "Group: " + number + "\nStudents: " + studentCount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDriveFileId() {
        return driveFileId;
    }

    public void setDriveFileId(String driveFileId) {
        this.driveFileId = driveFileId;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        final String COLUMN_GROUP_NUMBER = "number";
        final String COLUMN_GROUP_DRIVE_ID = "driveid";
        final String COLUMN_GROUP_COUNT = "count";

        contentValues.put(COLUMN_GROUP_NUMBER, number);
        contentValues.put(COLUMN_GROUP_DRIVE_ID, driveFileId);
        contentValues.put(COLUMN_GROUP_COUNT, studentCount);
        return contentValues;
    }

    public String toStringggg() {
        return "Group: " + number + " Students: " + studentCount + " Drive id = " + driveFileId;
    }
}

package com.studios.ciprian.students.model;

import java.io.Serializable;

public class Group implements Serializable {
    private String number;
    private String driveFileId;
    private int studentCount;

    public Group() {}

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

    public String toStringggg() {
        return "Group: " + number + " Students: " + studentCount + " Drive id = " + driveFileId;
    }
}

package com.studios.lucian.students.model;

/**
 * Created with Love by Lucian and Pi on 04.04.2016.
 */
public class Group {
    private String number;
    private int studentCount;

    public Group(String number, int studentCount) {
        this.number = number;
        this.studentCount = studentCount;
    }

    @Override
    public String toString() {
        return "Group: " + number + "\nCount: " + studentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return studentCount == group.studentCount && (number != null ? number.equals(group.number) : group.number == null);

    }

    @Override
    public int hashCode() {
        int result = number != null ? number.hashCode() : 0;
        result = 31 * result + studentCount;
        return result;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
}

package com.studios.lucian.students.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with Love by Lucian and Pi on 09.04.2016.
 */
public class Grade {
    private String matricol;
    private int grade;
    private int labNumber;
    private SimpleDateFormat date;

    public String getDate() {
        return date.format(new Date());
    }

    public void setDate(SimpleDateFormat date) {
        this.date = date;
    }

    public Grade(String matricol, int grade, int labNumber, SimpleDateFormat date) {

        this.matricol = matricol;
        this.grade = grade;
        this.labNumber = labNumber;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "matricol='" + matricol + '\'' +
                ", grade=" + grade +
                ", labNumber=" + labNumber +
                ", date=" + date.format(new Date()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade1 = (Grade) o;

        if (grade != grade1.grade) return false;
        if (labNumber != grade1.labNumber) return false;
        if (matricol != null ? !matricol.equals(grade1.matricol) : grade1.matricol != null)
            return false;
        return date != null ? date.equals(grade1.date) : grade1.date == null;

    }

    @Override
    public int hashCode() {
        int result = matricol != null ? matricol.hashCode() : 0;
        result = 31 * result + grade;
        result = 31 * result + labNumber;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public String getMatricol() {
        return matricol;
    }

    public void setMatricol(String matricol) {
        this.matricol = matricol;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getLabNumber() {
        return labNumber;
    }

    public void setLabNumber(int labNumber) {
        this.labNumber = labNumber;
    }

}

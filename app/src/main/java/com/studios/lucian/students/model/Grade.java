package com.studios.lucian.students.model;

/**
 * Created with Love by Lucian and Pi on 09.04.2016.
 */
public class Grade {
    private String matricol;
    private int grade;
    private int labNumber;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Grade(String matricol, int grade, int labNumber, String date) {

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
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade1 = (Grade) o;

        return grade == grade1.grade && labNumber == grade1.labNumber && (matricol != null ? matricol.equals(grade1.matricol) : grade1.matricol == null && (date != null ? date.equals(grade1.date) : grade1.date == null));

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

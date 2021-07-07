package com.studios.ciprian.students.model;


//obiectele care repr datele:note pt un student,,grup,prezenta,student


public class Grade {
    private String matricol;
    private int grade;
    private int labNumber;
    private String date;

    public Grade() {
    }

    public Grade(String matricol, int grade, int labNumber, String date) {
        this.matricol = matricol;
        this.grade = grade;
        this.labNumber = labNumber;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getToastDisplay(Student student) {
        return student.toString() + " was graded with " + grade;
    }
}

package com.studios.ciprian.students.model;

import com.google.firebase.auth.FirebaseAuth;

public class Presence {
    private String matricol;
    private int labNumber;
    private String date;
    private String owner;

    public Presence() {
    }

    public Presence(String matricol, int value, String date) {
        this.matricol = matricol;
        this.labNumber = value;
        this.date = date;
        this.owner = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    @Override
    public String toString() {
        return "Presence{" +
                "matricol='" + matricol + '\'' +
                ", labNumber=" + labNumber +
                ", date='" + date + '\'' +
                '}';
    }

    public String getMatricol() {
        return matricol;
    }

    public int getLabNumber() {
        return labNumber;
    }

    public String getDate() {
        return date;
    }

    public String getToastDisplay(Student student) {
        return student.toString() + " was marked as present.";
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }
}

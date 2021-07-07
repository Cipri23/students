package com.studios.ciprian.students.model;


import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class Student implements Serializable {
    private String matricol;
    private String groupNumber;
    private String name;
    private String surname;
    private String email;
    private String owner;

    public Student() {
    }

    public Student(String groupNumber, String matricol, String name, String surname, String email) {
        this.matricol = matricol;
        this.groupNumber = groupNumber;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.owner = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    public String getMatricol() {
        return matricol;
    }

    public void setMatricol(String matricol) {
        this.matricol = matricol;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

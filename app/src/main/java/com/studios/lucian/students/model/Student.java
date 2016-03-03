package com.studios.lucian.students.model;

/**
 * Created with Love by Lucian and Pi on 03.03.2016.
 */
public class Student {
    private String _surname;
    private String _name;

    @Override
    public String toString() {
        return "Student{" +
                "_surname='" + _surname + '\'' +
                ", _name='" + _name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (_surname != null ? !_surname.equals(student._surname) : student._surname != null)
            return false;
        return _name != null ? _name.equals(student._name) : student._name == null;

    }

    @Override
    public int hashCode() {
        int result = _surname != null ? _surname.hashCode() : 0;
        result = 31 * result + (_name != null ? _name.hashCode() : 0);
        return result;
    }

    public void set_surname(String _surname) {

        this._surname = _surname;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public Student() {

    }

    public Student(String name, String surname) {
        this._name = name;
        this._surname = surname;
    }

    public String getSurname() {
        return _surname;
    }

    public String getName() {
        return _name;
    }
}

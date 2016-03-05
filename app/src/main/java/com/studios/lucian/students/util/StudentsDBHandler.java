package com.studios.lucian.students.util;

import android.content.Context;
import android.support.v4.app.ListFragment;

import com.studios.lucian.students.fragment.ExcelFragment;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.repository.StudentDAO;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 05.03.2016.
 */
public class StudentsDBHandler {

    private StudentDAO studentDAO;
    private List<Student> studentList;

    public StudentsDBHandler(Context context) {
        studentDAO = new StudentDAO(context);
        studentList = studentDAO.getAll();
    }

    public void insertStudents(List<Student> studentsList) {
        for (Student student : studentsList) {
            studentDAO.add(student);
        }
    }

    public List<Student> getAll() {
        return studentList;
    }

    public void clearStudents() {
        for (Student student : studentList) {
            studentDAO.delete(student);
        }
    }
}

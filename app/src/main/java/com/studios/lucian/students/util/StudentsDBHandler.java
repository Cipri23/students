package com.studios.lucian.students.util;

import android.content.Context;

import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.repository.StudentDAO;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 05.03.2016.
 */
public class StudentsDBHandler {

    private StudentDAO mStudentDAO;
    private List<Student> mStudentList;

    public StudentsDBHandler(Context context) {
        mStudentDAO = new StudentDAO(context);
        mStudentList = mStudentDAO.getAll();
    }

    public void insertStudents(List<Student> studentsList) {
        for (Student student : studentsList) {
            if (!mStudentDAO.find(student)) {
                mStudentDAO.add(student);
            }
        }
    }

    public List<Student> getAll() {
        return mStudentList;
    }

    public void clearStudents() {
        for (Student student : mStudentList) {
            mStudentDAO.delete(student);
        }
    }
}

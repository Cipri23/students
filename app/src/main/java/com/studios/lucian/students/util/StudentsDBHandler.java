package com.studios.lucian.students.util;

import android.content.Context;
import android.os.AsyncTask;

import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.repository.StudentDAO;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 05.03.2016.
 */
public class StudentsDBHandler {
    private String TAG = StudentsDBHandler.class.getSimpleName();

    private StudentDAO mStudentDAO;

    public StudentsDBHandler(Context context) {
        mStudentDAO = new StudentDAO(context);
    }

    public void insertStudents(List<Student> studentsList) {
        new AsyncTask<List<Student>, Void, Void>() {
            @SafeVarargs
            @Override
            protected final Void doInBackground(List<Student>... lists) {
                for (Student student : lists[0]) {
                    if (!mStudentDAO.find(student)) {
                        mStudentDAO.add(student);
                    }
                }
                return null;
            }
        }.execute(studentsList);
    }

    public List<Student> getAll() {
        return mStudentDAO.getAll();
    }

    public void clearStudents() {
        for (Student student : mStudentDAO.getAll()) {
            mStudentDAO.delete(student);
        }
    }

    public boolean addStudent(Student student) {
        if (mStudentDAO.find(student)) {
            return false;
        }
        mStudentDAO.add(student);
        return true;
    }

    public boolean deleteStudent(Student student) {
        if (mStudentDAO.find(student)) {
            int affectedRows = mStudentDAO.delete(student);
            if (affectedRows == 1) return true;
        }
        return false;
    }

    public boolean updateStudent(Student student) {
        if (mStudentDAO.find(student)) {
            int affectedRows = mStudentDAO.update(student);
            if (affectedRows == 1) return true;
        }
        return false;
    }

    public List<Group> getUniqueGroups() {
        return mStudentDAO.getAllGroups();
    }

    public List<Student> getStudentsFromGroup(String mGroupNumber) {
        return mStudentDAO.getStudentsFromGroup(mGroupNumber);
    }
}

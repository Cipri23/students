package com.studios.lucian.students.util;

import android.content.Context;

import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.repository.GradesDAO;
import com.studios.lucian.students.repository.PresenceDAO;
import com.studios.lucian.students.repository.StudentDAO;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 05.03.2016.
 */
public class StudentsDbHandler {
    private final String TAG = StudentsDbHandler.class.getSimpleName();

    private final StudentDAO mStudentDAO;
    private final Context context;

    public StudentsDbHandler(Context context) {
        this.context = context;
        mStudentDAO = new StudentDAO(context);
    }

    public void insertStudents(List<Student> studentsList) {
        for (Student student : studentsList) {
            if (!mStudentDAO.find(student)) {
                mStudentDAO.add(student);
            }
        }
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
            GradesDAO mGradesDao = new GradesDAO(context);
            PresenceDAO presenceDAO = new PresenceDAO(context);

            mGradesDao.deleteRecords(student.getMatricol());
            presenceDAO.deleteRecords(student.getMatricol());

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

    public Student findStudent(String matricol) {
        return mStudentDAO.find(matricol);
    }

    public List<Group> getUniqueGroups() {
        return mStudentDAO.getAllGroups();
    }

    public List<Student> getStudentsFromGroup(String mGroupNumber) {
        return mStudentDAO.getStudentsFromGroup(mGroupNumber);
    }
}

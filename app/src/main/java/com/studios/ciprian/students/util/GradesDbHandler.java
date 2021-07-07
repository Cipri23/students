package com.studios.ciprian.students.util;

import android.content.Context;
import android.widget.Toast;

import com.studios.ciprian.students.model.Grade;
import com.studios.ciprian.students.repository.GradesDAO;
import com.studios.ciprian.students.repository.StudentDAO;

import java.util.List;

public class GradesDbHandler {
    private static String TAG = GradesDbHandler.class.getSimpleName();

    private final Context mContext;
    private final GradesDAO mGradesDAO;
    private final StudentDAO mStudentsDAO;

    public GradesDbHandler(Context context) {
        mContext = context;
        mGradesDAO = new GradesDAO(context);
        mStudentsDAO = new StudentDAO(context);
    }

    public boolean addGrade(Grade grade) {
        if (mGradesDAO.add(grade)) {
            Toast.makeText(mContext, "The student was graded with " + grade.getGrade(), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(mContext, "An error occurred when trying to add grade", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public List<Grade> getStudentGrades(String matricol) {
        return mGradesDAO.getStudentGrades(matricol);
    }
}

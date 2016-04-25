package com.studios.lucian.students.util;

import android.content.Context;
import android.widget.Toast;

import com.studios.lucian.students.model.Grade;
import com.studios.lucian.students.repository.GradesDAO;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 09.04.2016.
 */
public class GradesDbHandler {
    private static String TAG = GradesDbHandler.class.getSimpleName();

    private final Context mContext;
    private final GradesDAO mGradesDAO;

    public GradesDbHandler(Context context) {
        mContext = context;
        mGradesDAO = new GradesDAO(context);
    }

    public void addGrade(Grade grade) {
        if (mGradesDAO.add(grade)) {
            Toast.makeText(mContext, "The student was graded with " + grade.getGrade(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "An error occurred when trying to add grade", Toast.LENGTH_LONG).show();
        }
    }

    public List<Grade> getStudentGrades(String matricol) {
        return mGradesDAO.getStudentGrades(matricol);
    }
}

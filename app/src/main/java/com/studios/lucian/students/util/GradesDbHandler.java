package com.studios.lucian.students.util;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.studios.lucian.students.model.Grade;
import com.studios.lucian.students.repository.GradesDAO;
import com.studios.lucian.students.repository.StudentDAO;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 09.04.2016.
 */
public class GradesDbHandler {
    private static String TAG = GradesDbHandler.class.getSimpleName();

    private final Context mContext;
    private final GradesDAO mGradesDAO;
    private final StudentDAO mStudentsDAO;
    private GoogleApiClient mGoogleApiClient;

    public GradesDbHandler(Context context) {
        mContext = context;
        mGradesDAO = new GradesDAO(context);
        mStudentsDAO = new StudentDAO(context);

    }

    private GradesDbHandler(Context context, GoogleApiClient mGoogleApiClient) {
        this.mContext = context;
        mGradesDAO = new GradesDAO(context);
        mStudentsDAO = new StudentDAO(context);
        this.mGoogleApiClient = mGoogleApiClient;
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

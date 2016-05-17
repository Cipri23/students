package com.studios.lucian.students.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.adapter.GradesListAdapter;
import com.studios.lucian.students.model.Grade;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.GradesDbHandler;
import com.studios.lucian.students.util.StudentsDbHandler;

import java.util.List;

public class DisplayStudentActivity extends AppCompatActivity {
    private static final String TAG = DisplayStudentActivity.class.getSimpleName();
    private static final String KEY_MATRICOL = "matricol";

    private GradesDbHandler mGradesHandler;
    private StudentsDbHandler mStudentsHandler;
    private android.support.v7.app.ActionBar mAppBar;
    private ListView mListView;
    private TextView mEmptyText;
    private Student mCurrentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selected_student);

        mListView = (ListView) findViewById(android.R.id.list);
        mEmptyText = (TextView) findViewById(android.R.id.empty);
        mGradesHandler = new GradesDbHandler(getApplicationContext());
        mStudentsHandler = new StudentsDbHandler(getApplicationContext());
        mAppBar = getSupportActionBar();
        getExtras();
        if (mCurrentStudent != null) {
            setupToolbar();
            setupMainContent();
        }
    }

    private void setupMainContent() {
        List<Grade> grades = mGradesHandler.getStudentGrades(mCurrentStudent.getMatricol());
        GradesListAdapter adapter = new GradesListAdapter(getApplicationContext(), grades);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyText);
    }

    private void setupToolbar() {
        mAppBar.setTitle(mCurrentStudent.toString());
        mAppBar.setSubtitle(mCurrentStudent.getMatricol());
    }

    private void getExtras() {
        Bundle bundle = getIntent().getExtras();
        String matricol = bundle.getString(KEY_MATRICOL);
        mCurrentStudent = mStudentsHandler.findStudent(matricol);
    }
}
package com.studios.lucian.students.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.adapter.PagerAdapter;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.repository.PresenceDAO;
import com.studios.lucian.students.util.GradesDbHandler;
import com.studios.lucian.students.util.StudentsDbHandler;

public class DisplayStudentActivity extends AppCompatActivity {
    private static final String TAG = DisplayStudentActivity.class.getSimpleName();
    private static final String KEY_MATRICOL = "matricol";

    private GradesDbHandler mGradesHandler;
    private StudentsDbHandler mStudentsHandler;
    private android.support.v7.app.ActionBar mAppBar;
    private ListView mListViewGrades;
    private ListView mListViewPresences;
    private TextView mEmptyText;
    private Student mCurrentStudent;
    private PresenceDAO mPresenceDao;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mAppBar = getSupportActionBar();
        getExtras();
        if (mCurrentStudent != null) {
            setupToolbar();
        }

        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(manager, mCurrentStudent);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void setupToolbar() {
        mAppBar.setTitle(mCurrentStudent.toString());
        mAppBar.setSubtitle(mCurrentStudent.getMatricol());
    }

    private void getExtras() {
        mStudentsHandler = new StudentsDbHandler(this);
        Bundle bundle = getIntent().getExtras();
        String matricol = bundle.getString(KEY_MATRICOL);
        mCurrentStudent = mStudentsHandler.findStudent(matricol);
    }
}
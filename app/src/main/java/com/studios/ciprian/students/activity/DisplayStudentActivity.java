package com.studios.ciprian.students.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.studios.ciprian.students.R;
import com.studios.ciprian.students.adapter.PagerAdapter;
import com.studios.ciprian.students.fragment.GroupFragment;
import com.studios.ciprian.students.model.Student;

public class DisplayStudentActivity extends AppCompatActivity {

    private ActionBar mAppBar;
    private Student mCurrentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mAppBar = getSupportActionBar();
        mCurrentStudent = (Student) getIntent().getExtras().getSerializable(GroupFragment.KEY_STUDENT);

        if (mCurrentStudent != null) {
            setupToolbar();
        }

        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(manager, mCurrentStudent);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        mAppBar.setTitle(mCurrentStudent.toString());
        mAppBar.setSubtitle(mCurrentStudent.getMatricol());
    }
}
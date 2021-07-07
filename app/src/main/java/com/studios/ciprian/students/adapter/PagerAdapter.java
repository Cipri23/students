package com.studios.ciprian.students.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.studios.ciprian.students.fragment.GradesFragment;
import com.studios.ciprian.students.fragment.PresencesFragment;
import com.studios.ciprian.students.model.Student;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final Student currentStudent;

    public PagerAdapter(FragmentManager fm, Student student) {
        super(fm);
        this.currentStudent = student;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = GradesFragment.newInstance(currentStudent.getMatricol());
                break;
            case 1:
                frag = PresencesFragment.newInstance(currentStudent.getMatricol());
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = " ";
        switch (position) {
            case 0:
                title = "Grades";
                break;
            case 1:
                title = "Presences";
                break;
        }

        return title;
    }
}

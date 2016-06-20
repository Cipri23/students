package com.studios.lucian.students.util.listener;

import com.studios.lucian.students.model.Student;

/**
 * Created with Love by Lucian and Pi on 20.06.2016.
 */
public interface StudentActionsListener {
    void onPresenceClick(Student student);

    void onGradeClick(Student student);
}

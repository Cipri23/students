package com.studios.lucian.students.util;

import com.studios.lucian.students.model.Grade;
import com.studios.lucian.students.model.Presence;
import com.studios.lucian.students.model.Student;

/**
 * Created with Love by Lucian and Pi on 20.06.2016.
 */
public interface StudentButtonsListener {
    void addGrade(Student student, Grade grade);

    void addPresence(Student student, Presence presence);
}

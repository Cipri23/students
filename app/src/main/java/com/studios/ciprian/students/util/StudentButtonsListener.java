package com.studios.ciprian.students.util;

import com.studios.ciprian.students.model.Grade;
import com.studios.ciprian.students.model.Presence;
import com.studios.ciprian.students.model.Student;


public interface StudentButtonsListener {
    void addGrade(Student student, Grade grade);

    void addPresence(Student student, Presence presence);
}

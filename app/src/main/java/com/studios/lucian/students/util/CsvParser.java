package com.studios.lucian.students.util;

import android.util.Log;

import com.studios.lucian.students.model.Student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 22.03.2016.
 */
public class CsvParser {

    private File mFile;
    private static String TAG = CsvParser.class.getSimpleName();

    public CsvParser(File filePath)
    {
        Log.v(TAG, "CsvParser");
        mFile = filePath;
    }

    public List<Student> getStudentsList(String groupNumber) {
        Log.v(TAG, "getStudentsList");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String line;
        List<Student> studentList = new ArrayList<>();

        try {
            fileReader = new FileReader(mFile);
            bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                studentList.add(getStudentFromLine(groupNumber, line));
            }
        } catch (FileNotFoundException e) {
            Log.v(TAG, e.getMessage());
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
            try {
                fileReader.close();
            } catch (IOException e1) {
                Log.v(TAG, e1.getMessage());
            }
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                Log.v(TAG, e.getMessage());
            }
        }
        return studentList;
    }

    private Student getStudentFromLine(String groupNumber, String line) {
        Log.v(TAG, "getStudentFromLine");
        String[] components = line.split(Constants.CSV_DEFAULT_SEPARATOR);
        return new Student(groupNumber, components[0], components[1], components[2]);
    }
}

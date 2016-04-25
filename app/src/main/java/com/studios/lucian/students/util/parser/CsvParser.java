package com.studios.lucian.students.util.parser;

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
    private static final String TAG = CsvParser.class.getSimpleName();
    private static final String CSV_DEFAULT_SEPARATOR = ";";

    private final File mFile;
    private final String mGroupNumber;

    public CsvParser(String groupNumber, File filePath) {
        mGroupNumber = groupNumber;
        mFile = filePath;
    }

    public List<Student> parseFile() {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String line;
        List<Student> studentList = new ArrayList<>();

        try {
            fileReader = new FileReader(mFile);
            bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                String[] components = line.split(CSV_DEFAULT_SEPARATOR);
                studentList.add(new Student(mGroupNumber, components[0], components[1], components[2]));
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
            try {
                fileReader.close();
            } catch (IOException e1) {
                Log.i(TAG, e1.getMessage());
            }
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                Log.i(TAG, e.getMessage());
            }
        }
        return studentList;
    }
}

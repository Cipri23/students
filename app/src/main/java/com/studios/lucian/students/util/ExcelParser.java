package com.studios.lucian.students.util;

import android.util.Log;

import com.studios.lucian.students.model.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created with Love by Lucian and Pi on 04.03.2016.
 */
public class ExcelParser {

    private String _absolutePath;
    private List<Student> studentsList;
    private final static String TAG = ExcelParser.class.getSimpleName();

    public ExcelParser(String absolutePath) {
        this._absolutePath = absolutePath;
        studentsList = new ArrayList<>();
        parseFile();
    }

    public void parseFile() {
        try {
            File file = new File(_absolutePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = Workbook.getWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(0);

            for (int i = 8; i < sheet.getRows(); i++) {
                parseRow(sheet.getRow(i));
            }
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
    }

    private void parseRow(Cell[] row) {
        studentsList.add(new Student(row[2].getContents(), row[4].getContents()));
    }

    public List<Student> getStudentsList() {
//        Log.v(TAG, Arrays.toString(studentsList.toArray()));
        return studentsList;
    }
}

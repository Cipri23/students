package com.studios.lucian.students.util;

import android.util.Log;

import com.studios.lucian.students.model.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created with Love by Lucian and Pi on 04.03.2016.
 */
public class ExcelParser {

    private final static String TAG = ExcelParser.class.getSimpleName();

    private static final int mSkipRows = 8;
    private String mGroupNumber;
    private List<Student> mStudentsList;

    public ExcelParser(String number) {
        Log.v(TAG, "ExcelParser");
        mStudentsList = new ArrayList<>();
        mGroupNumber = number;
    }

    public void parseFile(String absolutePath) {
        Log.v(TAG, "parseFile");
        try {
            File file = new File(absolutePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = Workbook.getWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(0);
            for (int i = mSkipRows; i < sheet.getRows(); i++) {
                parseRow(sheet.getRow(i));
            }
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
    }

    private void parseRow(Cell[] row) {
        Log.v(TAG, "parseRow");
        Student student = new Student(mGroupNumber, row[1].getContents(), row[2].getContents(), row[4].getContents());
        mStudentsList.add(student);
    }

    public List<Student> getStudentsList(String absolutePath) {
        Log.v(TAG, "getStudentsList");
        parseFile(absolutePath);
        return mStudentsList;
    }
}
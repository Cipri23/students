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

    private final static String TAG = ExcelParser.class.getSimpleName();

    private String groupNumber;
    private List<Student> studentsList;

    public ExcelParser(String number) {
        studentsList = new ArrayList<>();
        this.groupNumber = number;
    }

    public void parseFile(String absolutePath) {
        try {
            File file = new File(absolutePath);
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
        studentsList.add(new Student(groupNumber, row[1].getContents(), row[2].getContents(), row[4].getContents()));
    }

    public List<Student> getStudentsList() {
        return studentsList;
    }

    public List<Student> getStudentsList(String absolutePath) {
        parseFile(absolutePath);
        return studentsList;
    }
}

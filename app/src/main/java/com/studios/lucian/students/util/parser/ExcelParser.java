package com.studios.lucian.students.util.parser;

import android.util.Log;

import com.studios.lucian.students.model.Student;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

/**
 * Created with Love by Lucian and Pi on 04.03.2016.
 */
public class ExcelParser {
    private final static String TAG = ExcelParser.class.getSimpleName();

    private static final int SKIP_ROWS = 8;
    private static final int INDEX_COLUMN_MATRICOL = 1;
    private static final int INDEX_COLUMN_NAME = 2;
    private static final int INDEX_COLUMN_SURNAME = 4;
    private static final String XLS_EXTENSION = ".xls";
    private static final String XLSX_EXTENSION = ".xlsx";

    private final String mGroupNumber;
    private final String mFilePath;

    public ExcelParser(String number, String absolutePath) {
        mGroupNumber = number;
        mFilePath = absolutePath;
    }

    public List<Student> parseFile() {
        // TODO use apache poi library for parsing Excel files. It's already downloaded.
        List<Student> studentsList = new ArrayList<>();

        try {
            File file = new File(mFilePath);
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(file, ws);
            Sheet sheet = workbook.getSheet(0);

            for (int i = SKIP_ROWS; i < sheet.getRows(); i++) {
                Student student = new Student(
                        mGroupNumber,
                        sheet.getCell(INDEX_COLUMN_MATRICOL, i).getContents(),
                        sheet.getCell(INDEX_COLUMN_NAME, i).getContents(),
                        sheet.getCell(INDEX_COLUMN_SURNAME, i).getContents());
                studentsList.add(student);
            }
            return studentsList;
        } catch (Exception ex) {
            Log.i(TAG, "parseFile: " + ex.getMessage());
        }
        return studentsList;
    }

//    private LoadOptions getFileOptions() {
//        //CellsHelper.detectFileFormat(absolutePath);
//        String extension = mFilePath.substring(mFilePath.lastIndexOf("."));
//        LoadOptions loadOptions;
//        if (extension.equals(XLS_EXTENSION)) {
//            loadOptions = new LoadOptions(FileFormatType.EXCEL_97_TO_2003);
//        } else {
//            loadOptions = new LoadOptions(FileFormatType.XLSX);
//        }
//        return loadOptions;
//    }
}
package com.studios.lucian.students.util;

import android.util.Log;

import com.aspose.cells.Cells;
import com.aspose.cells.FileFormatType;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.studios.lucian.students.model.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

    private String mGroupNumber;
    private String mFilePath;

    public ExcelParser(String number, String absolutePath) {
        Log.v(TAG, "ExcelParser");
        mGroupNumber = number;
        mFilePath = absolutePath;
    }

    public List<Student> parseFile() {
        Log.v(TAG, "parseFile");
        List<Student> studentsList = new ArrayList<>();
        try {
            File file = new File(mFilePath);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new Workbook(inputStream, getFileOptions());
            Worksheet sheet = workbook.getWorksheets().get(0);
            Cells cells = sheet.getCells();

            for (int i = SKIP_ROWS; i <= sheet.getCells().getMaxRow(); i++) {
                Student student = new Student(
                        mGroupNumber,
                        sheet.getCells().get(i, INDEX_COLUMN_MATRICOL).getDisplayStringValue(),
                        sheet.getCells().get(i, INDEX_COLUMN_NAME).getStringValueWithoutFormat(),
                        sheet.getCells().get(i, INDEX_COLUMN_SURNAME).getStringValueWithoutFormat());
                studentsList.add(student);
            }
            return studentsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private LoadOptions getFileOptions() {
        //CellsHelper.detectFileFormat(absolutePath);
        String extension = mFilePath.substring(mFilePath.lastIndexOf("."));
        LoadOptions loadOptions;
        if (extension.equals(XLS_EXTENSION)) {
            loadOptions = new LoadOptions(FileFormatType.EXCEL_97_TO_2003);
        } else {
            loadOptions = new LoadOptions(FileFormatType.XLSX);
        }
        return loadOptions;
    }
}
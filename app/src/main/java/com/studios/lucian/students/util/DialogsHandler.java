package com.studios.lucian.students.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Grade;
import com.studios.lucian.students.model.Student;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created with Love by Lucian and Pi on 09.04.2016.
 */
public class DialogsHandler {
    private static String TAG = DialogsHandler.class.getSimpleName();

    private Context mContext;
    private GradesDbHandler mGradesDbHandler;

    public DialogsHandler(Context context) {
        mContext = context;
        mGradesDbHandler = new GradesDbHandler(context);
    }

    public void showAddGradeDialog(final Student student) {
        final Grade[] grade = new Grade[1];
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View alertView = inflater.inflate(R.layout.dialog_add_grade, null);

        TextView dialogStudentName = (TextView) alertView.findViewById(R.id.dialog_grade_studname);
        dialogStudentName.setText(student.toString());

        final NumberPicker numberPickerGrade = (NumberPicker) alertView.findViewById(R.id.numberPickerGrade);
        numberPickerGrade.setMinValue(1);
        numberPickerGrade.setMaxValue(10);
        numberPickerGrade.setValue(10);

        final NumberPicker numberPickerLab = (NumberPicker) alertView.findViewById(R.id.numberPickerLab);
        numberPickerLab.setMinValue(1);
        numberPickerLab.setMaxValue(14);

        dialog.setView(alertView)
                .setPositiveButton(R.string.button_add_grade, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ITALY);
                        grade[0] = new Grade(
                                student.getMatricol(),
                                numberPickerGrade.getValue(),
                                numberPickerLab.getValue(),
                                format);
                        mGradesDbHandler.addGrade(grade[0]);
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }
}

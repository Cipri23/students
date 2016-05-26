package com.studios.lucian.students.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Grade;
import com.studios.lucian.students.model.Student;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created with Love by Lucian and Pi on 09.04.2016.
 */
public class DialogsHandler {
    private static final String ERROR = "Error";
    private static final String DIALOG_TITLE = "Invalid Group Number";
    private static final String DIALOG_MESSAGE = "The number ";
    private static final String DIALOG_MESSAGE_CONT = " seems to be invalid.\nPlease give a number between 0 and 10000.";
    private static String TAG = DialogsHandler.class.getSimpleName();
    private final Context mContext;
    private final GradesDbHandler mGradesDbHandler;
    private final StudentsDbHandler mStudentsDbHandler;
    private final GoogleApiClient mGoogleApiClient;

    public DialogsHandler(Context context, GoogleApiClient googleApiClient) {
        mContext = context;
        mGradesDbHandler = new GradesDbHandler(context);
        mStudentsDbHandler = new StudentsDbHandler(context);
        mGoogleApiClient = googleApiClient;
    }

    public static void showWrongGroupNumber(String mGroupNumber, Context context) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(DIALOG_TITLE)
                .setMessage(DIALOG_MESSAGE + mGroupNumber + DIALOG_MESSAGE_CONT)
                .setPositiveButton("Got it", null)
                .create()
                .show();
    }

    public static void showErrorMessage(String message, Context context) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(ERROR)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
                .show();
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
                                format.format(new Date()));
                        if (mGradesDbHandler.addGrade(grade[0])) {
                            addGradeInDrive(grade[0]);
                        }
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

    private void addGradeInDrive(final Grade grade) {
        if (mGoogleApiClient == null) {
            Log.i(TAG, "addGrade: googleApiClient is null");
            return;
        }
        final Student student = mStudentsDbHandler.findStudent(grade.getMatricol());
        if (student == null) {
            Log.i(TAG, "addGrade: findStudent returned null!!");
            return;
        }

        DriveId driveId = DriveId.decodeFromString(student.getDriveFileId());
        DriveFile driveFile = driveId.asDriveFile();

        driveFile.open(mGoogleApiClient, DriveFile.MODE_READ_WRITE, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if (!driveContentsResult.getStatus().isSuccess()) {
                    Log.i(TAG, "onResult: opening file returned an error");
                    return;
                }
                DriveContents driveContents = driveContentsResult.getDriveContents();
                try {
                    ParcelFileDescriptor parcelFileDescriptor = driveContents.getParcelFileDescriptor();

                    FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                    fileInputStream.read(new byte[fileInputStream.available()]);

                    Log.i(TAG, "onResult: number of bytes available: " + fileInputStream.available());

                    FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                    Writer writer = new OutputStreamWriter(fileOutputStream);
                    writer.write(student.toString() + ". On " + grade.getDate() + " was graded with " +
                            grade.getGrade() + " at lab number " + grade.getLabNumber());
                    // TODO consider adding end line before flushing
                    writer.close();

                    driveContents.commit(mGoogleApiClient, null).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status result) {
                            Log.i(TAG, "onResult: result = " + result.getStatus());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

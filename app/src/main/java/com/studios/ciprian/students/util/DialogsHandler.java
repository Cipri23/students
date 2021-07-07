package com.studios.ciprian.students.util;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.studios.ciprian.students.R;
import com.studios.ciprian.students.model.Grade;
import com.studios.ciprian.students.model.Presence;
import com.studios.ciprian.students.model.Student;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DialogsHandler {
    private static final String ERROR = "Error";
    private static final String DIALOG_TITLE = "Invalid Group Number";
    private static final String DIALOG_MESSAGE = "The number ";
    private static final String DIALOG_MESSAGE_CONT = " seems to be invalid.\nPlease give a number between 0 and 10000.";
    private final Context mContext;
    private final StudentButtonsListener listener;

    public DialogsHandler(Context context, StudentButtonsListener listener) {
        mContext = context;
        this.listener = listener;
    }

    public static void showWrongGroupNumber(String mGroupNumber, Context context) {
        new AlertDialog.Builder(context)
                .setTitle(DIALOG_TITLE)
                .setMessage(DIALOG_MESSAGE + mGroupNumber + DIALOG_MESSAGE_CONT)
                .setPositiveButton("Got it", null)
                .create()
                .show();
    }

    public static void showErrorMessage(String message, Context context) {
        new AlertDialog.Builder(context)
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

        TextView dialogStudentName = alertView.findViewById(R.id.dialog_grade_studname);
        dialogStudentName.setText(student.toString());

        final NumberPicker numberPickerGrade = alertView.findViewById(R.id.numberPickerGrade);
        numberPickerGrade.setMinValue(1);
        numberPickerGrade.setMaxValue(10);
        numberPickerGrade.setValue(10);

        final NumberPicker numberPickerLab = alertView.findViewById(R.id.numberPickerLab);
        numberPickerLab.setMinValue(1);
        numberPickerLab.setMaxValue(14);

        dialog.setView(alertView)
                .setPositiveButton(R.string.button_add_grade, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ITALY);
                        grade[0] = new Grade(student.getMatricol(),
                                numberPickerGrade.getValue(),
                                numberPickerLab.getValue(),
                                format.format(new Date()));

                        listener.addGrade(student, grade[0]);
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

    public void showAddPresenceDialog(final Student student) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View alertView = inflater.inflate(R.layout.dialog_add_presence, null);

        TextView dialogStudentName = alertView.findViewById(R.id.dialog_grade_studname);
        dialogStudentName.setText(student.toString());

        final NumberPicker numberPickerLab = alertView.findViewById(R.id.numberPickerLab);
        numberPickerLab.setMinValue(1);
        numberPickerLab.setMaxValue(14);

        dialog.setView(alertView)
                .setPositiveButton(R.string.button_add_presence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ITALY);
                        Presence presence = new Presence(student.getMatricol(),
                                numberPickerLab.getValue(), format.format(new Date()));

                        listener.addPresence(student, presence);
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

//    private void addGradeInDrive(final Grade grade) {
//        if (mGoogleApiClient == null) {
//            Log.i(TAG, "addGrade: googleApiClient is null");
//            return;
//        }
//        final Student student = mStudentsDbHandler.findStudent(grade.getMatricol());
//        if (student == null) {
//            Log.i(TAG, "addGrade: findStudent returned null!!");
//            return;
//        }
//
//        DriveId driveId = DriveId.decodeFromString(student.getDriveFileId());
//        DriveFile driveFile = driveId.asDriveFile();
//
//        driveFile.open(mGoogleApiClient, DriveFile.MODE_READ_WRITE, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
//            @Override
//            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
//                if (!driveContentsResult.getStatus().isSuccess()) {
//                    Log.i(TAG, "onResult: opening file returned an error");
//                    return;
//                }
//                DriveContents driveContents = driveContentsResult.getDriveContents();
//                try {
//                    ParcelFileDescriptor parcelFileDescriptor = driveContents.getParcelFileDescriptor();
//
//                    FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
//                    fileInputStream.read(new byte[fileInputStream.available()]);
//
//                    Log.i(TAG, "onResult: number of bytes available: " + fileInputStream.available());
//
//                    FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
//                    Writer writer = new OutputStreamWriter(fileOutputStream);
//                    writer.write(student.toString() + ". On " + grade.getDate() + " was graded with " +
//                            grade.getGrade() + " at lab number " + grade.getLabNumber());
//                    // TODO consider adding end line before flushing
//                    writer.close();
//
//                    driveContents.commit(mGoogleApiClient, null).setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(@NonNull Status result) {
//                            Log.i(TAG, "onResult: result = " + result.getStatus());
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}

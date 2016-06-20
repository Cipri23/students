package com.studios.lucian.students.sync;

import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.studios.lucian.students.model.Grade;
import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.model.Presence;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.DriveFileCallBackListener;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created with Love by Lucian and Pi on 20.06.2016.
 */
public class DriveSyncHandler {
    private static final String TAG = DriveSyncHandler.class.getSimpleName();
    private final GoogleApiClient mGoogleApiClient;
    private DriveFileCallBackListener listener;
    private ResultCallback<Status> commitChangesCallBack = new ResultCallback<Status>() {
        @Override
        public void onResult(@NonNull Status status) {
            Log.i(TAG, "onResult: result = " + status.getStatus());
        }
    };

    public DriveSyncHandler(GoogleApiClient googleApiClient, DriveFileCallBackListener listener) {
        this.mGoogleApiClient = googleApiClient;
        this.listener = listener;
    }

    public boolean syncGrade(final Grade grade, final Group group, final Student student) {
        if (mGoogleApiClient == null) {
            Log.i(TAG, "addGrade: googleApiClient is null");
            return false;
        }

        DriveFile driveFile = getDriveFile(group);
        if (driveFile != null) {
            driveFile.open(mGoogleApiClient, DriveFile.MODE_READ_WRITE, null)
                    .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                            if (!driveContentsResult.getStatus().isSuccess()) {
                                Log.i(TAG, "onResult: opening file returned an error");
                                return;
                            }
                            DriveContents driveContents = driveContentsResult.getDriveContents();
                            try {
                                ParcelFileDescriptor parcelFileDescriptor = driveContents.getParcelFileDescriptor();
                                FileDescriptor fd = parcelFileDescriptor.getFileDescriptor();

                                FileInputStream fileInputStream = new FileInputStream(fd);
                                fileInputStream.read(new byte[fileInputStream.available()]);

                                Log.i(TAG, "onResult: number of bytes available: " + fileInputStream.available());

                                FileOutputStream fileOutputStream = new FileOutputStream(fd);
                                Writer writer = new OutputStreamWriter(fileOutputStream);
                                writer.write(buildOutputGrade(grade, student));
                                writer.close();

                                driveContents.commit(mGoogleApiClient, null)
                                        .setResultCallback(commitChangesCallBack);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        return false;
    }

    public boolean syncPresence(final Presence presence, final Group group, final Student student) {
        if (mGoogleApiClient == null) {
            Log.i(TAG, "addGrade: googleApiClient is null");
            return false;
        }
        DriveFile driveFile = getDriveFile(group);
        if (driveFile != null) {
            driveFile.open(mGoogleApiClient, DriveFile.MODE_READ_WRITE, null)
                    .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                            if (!driveContentsResult.getStatus().isSuccess()) {
                                Log.i(TAG, "onResult: opening file returned an error");
                                return;
                            }
                            DriveContents driveContents = driveContentsResult.getDriveContents();
                            try {
                                ParcelFileDescriptor parcelFileDescriptor = driveContents.getParcelFileDescriptor();
                                FileDescriptor fd = parcelFileDescriptor.getFileDescriptor();

                                FileInputStream fileInputStream = new FileInputStream(fd);
                                fileInputStream.read(new byte[fileInputStream.available()]);

                                Log.i(TAG, "onResult: number of bytes available: " + fileInputStream.available());

                                FileOutputStream fileOutputStream = new FileOutputStream(fd);
                                Writer writer = new OutputStreamWriter(fileOutputStream);
                                writer.write(buildOutputPresence(presence, student));
                                writer.close();

                                driveContents.commit(mGoogleApiClient, null)
                                        .setResultCallback(commitChangesCallBack);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private DriveFile getDriveFile(Group group) {
        try {
            DriveId driveId = DriveId.decodeFromString(group.getDriveFileId());
            return driveId.asDriveFile();
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private String buildOutputPresence(Presence presence, Student student) {
        return student.toString() + " was marked as present " +
                " at lab number " + presence.getLabNumber() + " on " + presence.getDate() + "\n";
    }

    private String buildOutputGrade(Grade grade, Student student) {
        return student.toString() + " was graded with " + grade.getGrade() +
                " at lab number " + grade.getLabNumber() + " on " + grade.getDate() + "\n";
    }

    public void syncNewFile(final String groupNumber) {
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if (!driveContentsResult.getStatus().isSuccess()) {
                    return;
                }
                final DriveContents driveContents = driveContentsResult.getDriveContents();

                new Thread() {
                    @Override
                    public void run() {
                        OutputStream outputStream = driveContents.getOutputStream();
                        Writer writer = new OutputStreamWriter(outputStream);
                        try {
                            writer.write("Activity for group " + groupNumber + "\n\n");
                            writer.close();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }

                        MetadataChangeSet changeSet = getMetadataChangeSet(groupNumber);

                        Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                .createFile(mGoogleApiClient, changeSet, driveContents)
                                .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                    @Override
                                    public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                                        if (!driveFileResult.getStatus().isSuccess()) {
                                            return;
                                        }
                                        listener.setDriveFileId(groupNumber, driveFileResult.getDriveFile().getDriveId());
                                    }
                                });
                    }
                }.start();
            }
        });
    }

    private MetadataChangeSet getMetadataChangeSet(String groupNumber) {
        return new MetadataChangeSet.Builder()
                .setTitle("Group " + groupNumber)
                .setMimeType("application/vnd.oasis.opendocument.spreadsheet")
                .setStarred(true)
                .build();
    }
}
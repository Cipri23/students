package com.studios.ciprian.students.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.studios.ciprian.students.model.Presence;

import java.util.ArrayList;
import java.util.List;

public class PresenceDAO extends DataBaseHelper {

    private static final String TAG = PresenceDAO.class.getSimpleName();
    private static final String TABLE_NAME_PRESENCE = "presence";

    private static final String COLUMN_PRESENCE_MATRICOL = "matricol";
    private static final String COLUMN_PRESENCE_LAB_NR = "lab";
    private static final String COLUMN_PRESENCE_DATE = "date";

    public PresenceDAO(Context context) {
        super(context);
    }


    public boolean add(Presence presence) {
//        SQLiteDatabase database = getWritableDatabase();
//        long result = database.insert(TABLE_NAME_PRESENCE, null, presence.getContentValues());
//        database.close();
//        if (result == -1) {
//            Log.i(TAG, "Presence not Added: " + presence.toString());
//            return false;
//        }
//        Log.i(TAG, "Presence Added: " + presence.toString());
        return true;
    }

    public List<Presence> getStudentPresences(String matricol) {
        List<Presence> presences = new ArrayList<>();
        try {
            SQLiteDatabase database = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_PRESENCE + " WHERE " + COLUMN_PRESENCE_MATRICOL + " =?";
            Cursor cursor = database.rawQuery(query, new String[]{matricol});

            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_PRESENCE_DATE));
                int labNr = cursor.getInt(cursor.getColumnIndex(COLUMN_PRESENCE_LAB_NR));
                presences.add(new Presence(matricol, labNr, date));
            }
            if (!cursor.isClosed()) cursor.close();
            if (database.isOpen()) database.close();
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
        }
        return presences;
    }

    public void deleteRecords(String matricol) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            int affectedRows = database.delete(TABLE_NAME_PRESENCE, COLUMN_PRESENCE_MATRICOL + " = ?",
                    new String[]{matricol});
            if (database.isOpen()) {
                database.close();
            }
            Log.i(TAG, "delete result: " + affectedRows);
        } catch (SQLiteException ex) {
            Log.i(TAG, ex.getMessage());
        }
    }
}
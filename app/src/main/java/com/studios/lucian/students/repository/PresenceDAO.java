package com.studios.lucian.students.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.studios.lucian.students.model.Presence;

/**
 * Created with Love by Lucian and Pi on 20.06.2016.
 */
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
        SQLiteDatabase database = getWritableDatabase();
        long result = database.insert(TABLE_NAME_PRESENCE, null, presence.getContentValues());
        database.close();
        if (result == -1) {
            Log.i(TAG, "Grade not Added: " + presence.toString());
            return false;
        }
        Log.i(TAG, "Grade Added: " + presence.toString());
        return true;
    }
}
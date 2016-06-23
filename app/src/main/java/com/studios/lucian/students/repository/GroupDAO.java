package com.studios.lucian.students.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.studios.lucian.students.model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 20.06.2016.
 */
public class GroupDAO extends DataBaseHelper {

    private static final String TABLE_NAME_GROUP = "groupnr";
    private static final String COLUMN_GROUP_NUMBER = "number";
    private static final String COLUMN_GROUP_DRIVE_ID = "driveid";
    private static final String COLUMN_GROUP_COUNT = "count";

    public GroupDAO(Context context) {
        super(context);
    }

    public boolean add(Group group) {
        SQLiteDatabase database = getWritableDatabase();
        long result = database.insert(TABLE_NAME_GROUP, null, group.getContentValues());
        database.close();
        return result != -1;
    }

    public boolean find(String group) {
        try {
            SQLiteDatabase database = getReadableDatabase();
            long result = DatabaseUtils.queryNumEntries(
                    database,
                    TABLE_NAME_GROUP,
                    COLUMN_GROUP_NUMBER + " = ?",
                    new String[]{group});

            if (result == 1) return true;
            if (database.isOpen()) database.close();
        } catch (SQLiteException ignored) {
        }
        return false;
    }

    public List<Group> getAll() {
        List<Group> groupList = new ArrayList<>();
        String queryGroups = "SELECT * FROM " + TABLE_NAME_GROUP;

        Cursor cursor = getReadableDatabase().rawQuery(queryGroups, null);

        while (cursor.moveToNext()) {
            String groupNr = cursor.getString(cursor.getColumnIndex(COLUMN_GROUP_NUMBER));
            int count = cursor.getInt(cursor.getColumnIndex(COLUMN_GROUP_COUNT));
            String driveId = cursor.getString(cursor.getColumnIndex(COLUMN_GROUP_DRIVE_ID));
            Group group = new Group(groupNr, driveId, count);
            Log.i("GroupDAO", "getAll: " + group.toStringggg());
            groupList.add(group);
        }
        cursor.close();
        return groupList;
    }

    public Group get(String mGroupNumber) {
        String queryGroups = "SELECT * FROM " + TABLE_NAME_GROUP + " " +
                "WHERE " + COLUMN_GROUP_NUMBER + " = " + mGroupNumber;

        Cursor cursor = getReadableDatabase().rawQuery(queryGroups, null);
        if (cursor.moveToFirst()) {
            String groupNr = cursor.getString(cursor.getColumnIndex(COLUMN_GROUP_NUMBER));
            int count = cursor.getInt(cursor.getColumnIndex(COLUMN_GROUP_COUNT));
            String driveId = cursor.getString(cursor.getColumnIndex(COLUMN_GROUP_DRIVE_ID));
            return new Group(groupNr, driveId, count);
        }
        cursor.close();
        return null;
    }

    public void setDriveId(String groupNumber, String s) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_GROUP_DRIVE_ID, s);
        int affectedRows = database.update(TABLE_NAME_GROUP, contentValues,
                COLUMN_GROUP_NUMBER + " = ?", new String[]{groupNumber});
        if (database.isOpen()) database.close();
        if (affectedRows != 1)
            Log.i("GroupDAO", "setDriveId: affected rows != 1!! aff = " + affectedRows);
    }

    public void decreaseCount(Group mCurrentGroup) {
        int count = getGroupCount(mCurrentGroup);
        if (count == -1) return;
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_GROUP_COUNT, count - 1);
        int affectedRows = database.update(TABLE_NAME_GROUP, contentValues,
                COLUMN_GROUP_NUMBER + " = ?", new String[]{mCurrentGroup.getNumber()});
        if (database.isOpen()) database.close();
        if (affectedRows != 1)
            Log.i("GroupDAO", "setDriveId: affected rows != 1!! aff = " + affectedRows);
    }

    public void increaseCount(Group mCurrentGroup) {
        int count = getGroupCount(mCurrentGroup);
        if (count == -1) return;
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_GROUP_COUNT, count + 1);
        int affectedRows = database.update(TABLE_NAME_GROUP, contentValues,
                COLUMN_GROUP_NUMBER + " = ?", new String[]{mCurrentGroup.getNumber()});
        if (database.isOpen()) database.close();
        if (affectedRows != 1)
            Log.i("GroupDAO", "setDriveId: affected rows != 1!! aff = " + affectedRows);
    }

    private int getGroupCount(Group mCurrentGroup) {
        String queryGroups = "SELECT * FROM " + TABLE_NAME_GROUP + " " +
                "WHERE " + COLUMN_GROUP_NUMBER + " = " + mCurrentGroup.getNumber();

        Cursor cursor = getReadableDatabase().rawQuery(queryGroups, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(COLUMN_GROUP_COUNT));
        }
        cursor.close();
        return -1;
    }
}
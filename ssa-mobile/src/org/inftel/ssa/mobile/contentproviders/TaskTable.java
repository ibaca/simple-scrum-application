
package org.inftel.ssa.mobile.contentproviders;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskTable {

    // Database table
    public static final String TABLE_TASK = "task";

    // Column Names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_ESTIMATED = "estimated";
    public static final String COLUMN_BURNED = "burned";
    public static final String COLUMN_REMAINING = "remaining";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_BEGINDATE = "beginDate";
    public static final String COLUMN_ENDDATE = "enddate";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_SPRINT = "sprint";
    public static final String COLUMN_PROJECT = "project";
    public static final String COLUMN_COMMENTS = "comments";
    public static final String COLUMN_CREATED = "created";

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_TASK + " ("
                    + COLUMN_ID + " INTEGER primary KEY autoincrement, "
                    + COLUMN_DESCRIPTION + " TEXT, "
                    + COLUMN_SUMMARY + " TEXT, "
                    + COLUMN_ESTIMATED + " INTEGER, "
                    + COLUMN_BURNED + " INTEGER, "
                    + COLUMN_REMAINING + " INTEGER, "
                    + COLUMN_PRIORITY + " INTEGER, "
                    + COLUMN_BEGINDATE + " DATE, "
                    + COLUMN_ENDDATE + " DATE, "
                    + COLUMN_STATUS + " INTEGER, "
                    + COLUMN_USER + " INTEGER, "
                    + COLUMN_SPRINT + " INTEGER, "
                    + COLUMN_PROJECT + " INTEGER, "
                    + COLUMN_CREATED + " DATE, "
                    + COLUMN_COMMENTS + " TEXT); ";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TaskDatabaseHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }
}


package org.inftel.ssa.mobile.contentproviders;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProjectTable {
    private static final String TAG = "ProjectsContentProvider";
    public static final String PROJECTS_TABLE = "projects";

    // Database table
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SUMMARY = "summary";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_OPENED = "opened";
    public static final String KEY_STARTED = "started";
    public static final String KEY_CLOSE = "close";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_LINKS = "links";
    public static final String KEY_LABELS = "labels";
    public static final String KEY_LICENSE = "license";

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + PROJECTS_TABLE + " ("
                    + KEY_ID + " INTEGER primary key autoincrement, "
                    + KEY_NAME + " TEXT, "
                    + KEY_SUMMARY + " TEXT, "
                    + KEY_DESCRIPTION + " TEXT, "
                    + KEY_OPENED + " TEXT, "
                    + KEY_STARTED + " TEXT, "
                    + KEY_CLOSE + " TEXT, "
                    + KEY_COMPANY + " TEXT, "
                    + KEY_LINKS + " TEXT, "
                    + KEY_LABELS + " TEXT, "
                    + KEY_LICENSE + " TEXT); ";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + PROJECTS_TABLE);
        onCreate(db);
    }

}

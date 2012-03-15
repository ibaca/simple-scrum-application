
package org.inftel.ssa.mobile.contentproviders;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SprintTable {

    private static final String TAG = "SprintTable";
    public static final String SPRINT_TABLE = "sprints";

    // Database table
    public static final String KEY_ID = "_id";
    public static final String KEY_SUMMARY = "summary";
    public static final String KEY_STABLE_ID = "stable_id";

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + SPRINT_TABLE + " ("
                    + KEY_ID + " INTEGER primary key autoincrement, "
                    + KEY_SUMMARY + " TEXT, "
                    + KEY_STABLE_ID + " TEXT); ";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + SPRINT_TABLE);
        onCreate(db);
    }
}

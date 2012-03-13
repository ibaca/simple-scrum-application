
package org.inftel.ssa.mobile.contentproviders;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserTable {

    // Database table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EMAIL = "email";

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_USERS + "( "
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_EMAIL + " text not null);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(UserDatabaseHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}

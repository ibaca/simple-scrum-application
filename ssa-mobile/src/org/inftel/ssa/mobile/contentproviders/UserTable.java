
package org.inftel.ssa.mobile.contentproviders;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserTable {

    private static final String TAG = "UsersContentProvider";
    public static final String USERS_TABLE = "users";

    // Database table
    public static final String KEY_ID = "_id";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_PASS = "password";
    public static final String KEY_ROLE = "userRole";
    public static final String KEY_NUMBER = "number";

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + USERS_TABLE + " ("
                    + KEY_ID + " INTEGER primary key autoincrement, "
                    + KEY_FULLNAME + " TEXT, "
                    + KEY_NICKNAME + " TEXT, "
                    + KEY_EMAIL + " TEXT, "
                    + KEY_NUMBER + " TEXT, "
                    + KEY_COMPANY + " TEXT, "
                    + KEY_PASS + " TEXT, "
                    + KEY_ROLE + " TEXT); ";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        onCreate(db);
    }
}

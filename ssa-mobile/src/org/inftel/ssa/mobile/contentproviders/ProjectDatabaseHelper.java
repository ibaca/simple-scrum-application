
package org.inftel.ssa.mobile.contentproviders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProjectDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "projects.db";
    private static final int DATABASE_VERSION = 1;

    public ProjectDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserTable.onUpgrade(db, oldVersion, newVersion);
    }
}


package org.inftel.ssa.mobile.contentproviders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * Facilita la gestion de la base de datos User. Define las columnas y las
 * sentencias de creaci√≥n y destrucci√≥n de la base de datos SQLite.
 * </p>
 * <p>
 * Basada en <a href=
 * "http://www.vogella.de/articles/AndroidSQLite/article.html#tutorialusecp_overview"
 * >Android SQLite Database and ContentProvider</a>.
 * </p>
 * 
 * @author ibaca
 */
public class SprintDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sprints.db";
    private static final int DATABASE_VERSION = 2;

    public SprintDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SprintTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SprintTable.onUpgrade(db, oldVersion, newVersion);
    }
}

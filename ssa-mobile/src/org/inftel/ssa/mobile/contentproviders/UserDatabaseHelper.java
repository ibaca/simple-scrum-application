
package org.inftel.ssa.mobile.contentproviders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * Facilita la gestion de la base de datos User. Define las columnas y las
 * sentencias de creación y destrucción de la base de datos SQLite.
 * </p>
 * <p>
 * Basada en <a href=
 * "http://www.vogella.de/articles/AndroidSQLite/article.html#tutorialusecp_overview"
 * >Android SQLite Database and ContentProvider</a>.
 * </p>
 * 
 * @author ibaca
 */
public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Method is called during creation of the database */
    @Override
    public void onCreate(SQLiteDatabase database) {
        UserTable.onCreate(database);
    }

    /*
     * Method is called during an upgrade of the database, e.g. if you increase
     * the database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserTable.onUpgrade(db, oldVersion, newVersion);
    }

}


package org.inftel.ssa.mobile.contentproviders;

import static android.text.TextUtils.isEmpty;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class UserContentProvider extends ContentProvider {

    // database
    private UserDatabaseHelper database;

    public static final Uri CONTENT_URI = Uri
            .parse("content://org.inftel.ssa.mobile.contentproviders.users/users");

    // Create the constants used to differentiate between the different URI
    // requests.
    private static final int USERS = 1;
    private static final int USERS_ID = 2;

    // Allocate the UriMatcher object, where a URI ending in 'places' will
    // correspond to a request for all places, and 'places' with a trailing
    // '/[Unique ID]' will
    // represent a single place details row.
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("org.inftel.ssa.mobile.contentproviders.users", "users", USERS);
        uriMatcher.addURI("org.inftel.ssa.mobile.contentproviders.users", "users/*", USERS_ID);
    }

    public UserContentProvider() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase usersDB = database.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case USERS:
                count = usersDB.delete(UserTable.USERS_TABLE, selection, selectionArgs);
                break;

            case USERS_ID:
                String segment = uri.getPathSegments().get(1);
                count = usersDB.delete(UserTable.USERS_TABLE, UserTable.KEY_ID + "="
                        + segment
                        + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case USERS:
                return "vnd.android.cursor.dir/vnd.inftel.ssa.users";
            case USERS_ID:
                return "vnd.android.cursor.item/vnd.inftel.ssa.users";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Insert the new row, will return the row number if successful.
        SQLiteDatabase usersDB = database.getWritableDatabase();
        long rowID = usersDB.insert(UserTable.USERS_TABLE, "nullhack", values);

        // Return a URI to the newly inserted row on success.
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {

        database = new UserDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(UserTable.USERS_TABLE);

        // If this is a row query, limit the result set to the passed in row.
        switch (uriMatcher.match(uri)) {
            case USERS_ID:
                qb.appendWhere(UserTable.KEY_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                break;
        }

        String orderBy = null;
        if (!TextUtils.isEmpty(sortOrder)) {
            orderBy = sortOrder;
        }

        // Apply the query to the underlying database.
        SQLiteDatabase usersDB = database.getWritableDatabase();
        Cursor c = qb.query(usersDB, projection, selection, selectionArgs, null, null, orderBy);

        // Register the contexts ContentResolver to be notified if
        // the cursor result set changes.
        c.setNotificationUri(getContext().getContentResolver(), uri);

        // Return a cursor to the query result.
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase usersDB = database.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case USERS:
                count = usersDB.update(UserTable.USERS_TABLE, values, selection, selectionArgs);
                break;

            case USERS_ID:
                String segment = uri.getLastPathSegment();
                count = usersDB.update(UserTable.USERS_TABLE, values, UserTable.KEY_ID + "="
                        + segment
                        + (!isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}

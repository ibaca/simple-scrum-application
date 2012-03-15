
package org.inftel.ssa.mobile.contentproviders;

import static android.net.Uri.parse;
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

public class SprintContentProvider extends ContentProvider {

    // database
    private SprintDatabaseHelper database;

    static final String PROVIDER = "org.inftel.ssa.mobile.provider";

    public static final Uri CONTENT_URI = parse("content://" + PROVIDER + "/sprints");

    // Create the constants used to differentiate between the different URI
    // requests.
    private static final int SPRINTS = 1;
    private static final int SPRINTS_ID = 2;

    // Allocate the UriMatcher object, where a URI ending in 'places' will
    // correspond to a request for all places, and 'places' with a trailing
    // '/[Unique ID]' will
    // represent a single place details row.
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "sprints", SPRINTS);
        uriMatcher.addURI(PROVIDER, "sprints/*", SPRINTS_ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase sprintDB = database.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SPRINTS:
                count = sprintDB.delete(SprintTable.SPRINT_TABLE, selection, selectionArgs);
                break;

            case SPRINTS_ID:
                String segment = uri.getPathSegments().get(1);
                count = sprintDB.delete(SprintTable.SPRINT_TABLE, SprintTable.KEY_ID + "="
                        + segment + (!isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs);
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
            case SPRINTS:
                return "vnd.android.cursor.dir/vnd.inftel.ssa.sprints";
            case SPRINTS_ID:
                return "vnd.android.cursor.item/vnd.inftel.ssa.sprints";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Insert the new row, will return the row number if successful.
        SQLiteDatabase sprintDB = database.getWritableDatabase();
        long rowID = sprintDB.insert(SprintTable.SPRINT_TABLE, "nullhack", values);

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
        database = new SprintDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SprintTable.SPRINT_TABLE);

        // If this is a row query, limit the result set to the passed in row.
        switch (uriMatcher.match(uri)) {
            case SPRINTS_ID:
                qb.appendWhere(SprintTable.KEY_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                break;
        }

        String orderBy = null;
        if (!TextUtils.isEmpty(sortOrder)) {
            orderBy = sortOrder;
        }

        // Apply the query to the underlying database.
        SQLiteDatabase sprintDB = database.getWritableDatabase();
        Cursor c = qb.query(sprintDB, projection, selection, selectionArgs, null, null, orderBy);

        // Register the contexts ContentResolver to be notified if
        // the cursor result set changes.
        c.setNotificationUri(getContext().getContentResolver(), uri);

        // Return a cursor to the query result.
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase sprintDB = database.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case SPRINTS:
                count = sprintDB.update(SprintTable.SPRINT_TABLE, values, selection, selectionArgs);
                break;

            case SPRINTS_ID:
                String segment = uri.getLastPathSegment();
                count = sprintDB.update(SprintTable.SPRINT_TABLE, values, SprintTable.KEY_ID + "="
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

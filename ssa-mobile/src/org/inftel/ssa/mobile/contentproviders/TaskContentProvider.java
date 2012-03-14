
package org.inftel.ssa.mobile.contentproviders;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class TaskContentProvider extends ContentProvider {

    // database
    private TaskDatabaseHelper database;

    // Create the constants used to differentiate between the different URI
    // requests.Used for the UriMacher
    private static final int TASK = 10;
    private static final int TASK_ID = 20;

    private static final String AUTHORITY = "org.inftel.ssa.mobile.provider.task";

    private static final String BASE_PATH = "task";

    public static final Uri CONTENT_URI = Uri
            .parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/task";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/task";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, TASK);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TASK_ID);
    }

    public TaskContentProvider() {
        // TODO Auto-generated constructor stub
    }

    // The default sort order for this table
    public static final String DEFAULT_SORT_ORDER = "priority DESC, beginDate DESC";

    @Override
    public boolean onCreate() {
        database = new TaskDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        // Check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        qb.setTables(TaskTable.TABLE_TASK);

        // If this is a row query, limit the result set to the passed in row.
        switch (sURIMatcher.match(uri)) {
            case TASK:
                break;
            case TASK_ID:
                qb.appendWhere(TaskTable.COLUMN_ID + "=" + uri.getPathSegments());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        String orderBy = null;
        if (!TextUtils.isEmpty(sortOrder)) {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // Register the contexts ContentResolver to be notified if
        // the cursor result set changes.
        c.setNotificationUri(getContext().getContentResolver(), uri);

        // Return a cursor to the query result.
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case TASK:
                id = sqlDB.insert(TaskTable.TABLE_TASK, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case TASK:
                rowsDeleted = sqlDB.delete(TaskTable.TABLE_TASK, selection,
                        selectionArgs);
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            TaskTable.TABLE_TASK,
                            TaskTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            TaskTable.TABLE_TASK,
                            TaskTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case TASK:
                rowsUpdated = sqlDB.update(TaskTable.TABLE_TASK,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TaskTable.TABLE_TASK,
                            values,
                            TaskTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TaskTable.TABLE_TASK,
                            values,
                            TaskTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {
                TaskTable.COLUMN_DESCRIPTION,
                TaskTable.COLUMN_SUMMARY, TaskTable.COLUMN_ESTIMATED,
                TaskTable.COLUMN_BURNED, TaskTable.COLUMN_REMAINING,
                TaskTable.COLUMN_PRIORITY, TaskTable.COLUMN_BEGINDATE,
                TaskTable.COLUMN_ENDDATE, TaskTable.COLUMN_STATUS,
                TaskTable.COLUMN_USER, TaskTable.COLUMN_SPRINT,
                TaskTable.COLUMN_PROJECT, TaskTable.COLUMN_COMMENTS,
                TaskTable.COLUMN_CREATED, TaskTable.COLUMN_ID
        };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // Check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
}

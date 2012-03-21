
package org.inftel.ssa.mobile.provider;

import java.util.ArrayList;
import java.util.Arrays;

import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.provider.SsaContract.Sprints;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;
import org.inftel.ssa.mobile.provider.SsaContract.Users;
import org.inftel.ssa.mobile.provider.SsaDatabase.ProjectsUsers;
import org.inftel.ssa.mobile.provider.SsaDatabase.Tables;
import org.inftel.ssa.mobile.util.SelectionBuilder;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class SsaProvider extends ContentProvider {
    private static final String TAG = "SsaProvider";

    private SsaDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Constants used to differentiate between the different URI requests
    private static final int PROJECTS = 100;
    private static final int PROJECTS_ID = 101;
    private static final int PROJECTS_ID_TASKS = 102;
    private static final int PROJECTS_ID_SPRINTS = 103;
    private static final int PROJECTS_ID_USERS = 104;

    private static final int TASKS = 200;
    private static final int TASKS_ID = 201;

    private static final int SPRINTS = 300;
    private static final int SPRINTS_ID = 301;

    private static final int USERS = 400;
    private static final int USERS_ID = 401;
    private static final int USERS_ID_TASKS = 402;

    // Allocate the UriMatcher object, where a URI ending in 'places' will
    // correspond to a request for all places, and 'places' with a trailing
    // '/[Unique ID]' will
    // represent a single place details row.

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SsaContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "projects", PROJECTS);
        matcher.addURI(authority, "projects/*", PROJECTS_ID);
        matcher.addURI(authority, "projects/*/tasks", PROJECTS_ID_TASKS);
        matcher.addURI(authority, "projects/*/sprints", PROJECTS_ID_SPRINTS);
        matcher.addURI(authority, "projects/*/users", PROJECTS_ID_USERS);

        matcher.addURI(authority, "tasks", TASKS);
        matcher.addURI(authority, "tasks/*", TASKS_ID);

        matcher.addURI(authority, "sprints", SPRINTS);
        matcher.addURI(authority, "sprints/*", SPRINTS_ID);

        matcher.addURI(authority, "users", USERS);
        matcher.addURI(authority, "users/*", USERS_ID);
        matcher.addURI(authority, "users/*/tasks", USERS_ID_TASKS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        final Context context = getContext();
        mOpenHelper = new SsaDatabase(context);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROJECTS:
                return Projects.CONTENT_TYPE;
            case PROJECTS_ID:
                return Projects.CONTENT_ITEM_TYPE;
            case PROJECTS_ID_TASKS:
                return Tasks.CONTENT_TYPE;
            case PROJECTS_ID_SPRINTS:
                return Sprints.CONTENT_TYPE;
            case PROJECTS_ID_USERS:
                return Users.CONTENT_TYPE;
            case TASKS:
                return Tasks.CONTENT_TYPE;
            case TASKS_ID:
                return Tasks.CONTENT_ITEM_TYPE;
            case SPRINTS:
                return Sprints.CONTENT_TYPE;
            case SPRINTS_ID:
                return Sprints.CONTENT_ITEM_TYPE;
            case USERS:
                return Users.CONTENT_TYPE;
            case USERS_ID:
                return Users.CONTENT_ITEM_TYPE;
            case USERS_ID_TASKS:
                return Tasks.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        Log.v(TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ")");
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        final SelectionBuilder builder = buildExpandedSelection(uri, match);
        return builder.where(selection, selectionArgs).query(db, projection, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROJECTS: {
                db.insertOrThrow(Tables.PROJECTS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Projects.buildProjectUri(values.getAsString(Projects._ID));
            }
            case PROJECTS_ID_USERS: {
                db.insertOrThrow(Tables.PROJECTS_USERS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Users.buildUserUri(values.getAsString(ProjectsUsers.USER_ID));
            }
            case TASKS: {
                db.insertOrThrow(Tables.TASKS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Tasks.buildTasktUri(values.getAsString(Tasks._ID));
            }
            case SPRINTS: {
                db.insertOrThrow(Tables.SPRINTS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Sprints.buildSprintUri(values.getAsString(Sprints._ID));
            }
            case USERS: {
                db.insertOrThrow(Tables.USERS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Users.buildUserUri(values.getAsString(Users._ID));
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int retVal = builder.where(selection, selectionArgs).update(db, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    /** {@inheritDoc} */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete(uri=" + uri + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int retVal = builder.where(selection, selectionArgs).delete(db);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    /**
     * Apply the given set of {@link ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROJECTS: {
                return builder.table(Tables.PROJECTS);
            }
            case PROJECTS_ID: {
                final String projectId = Projects.getProjectId(uri);
                return builder.table(Tables.PROJECTS).where(Projects._ID + "=?", projectId);
            }
            case PROJECTS_ID_USERS: {
                final String projectId = Projects.getProjectId(uri);
                return builder.table(Tables.PROJECTS_USERS)
                        .where(ProjectsUsers.PROJECT_ID, projectId);
            }
            case TASKS: {
                return builder.table(Tables.TASKS);
            }
            case TASKS_ID: {
                final String taskId = Tasks.getTaskId(uri);
                return builder.table(Tables.TASKS).where(Tasks._ID + "=?", taskId);
            }
            case SPRINTS: {
                return builder.table(Tables.SPRINTS);
            }
            case SPRINTS_ID: {
                final String sprintId = Sprints.getSprintId(uri);
                return builder.table(Tables.SPRINTS).where(Sprints._ID + "=?", sprintId);
            }
            case USERS: {
                return builder.table(Tables.USERS);
            }
            case USERS_ID: {
                final String userId = Users.getUserId(uri);
                return builder.table(Tables.USERS).where(Users._ID + "=?", userId);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case PROJECTS: {
                return builder.table(Tables.PROJECTS);
            }
            case PROJECTS_ID: {
                final String projectId = Projects.getProjectId(uri);
                return builder.table(Tables.PROJECTS)
                        .map(Projects.TASKS_COUNT, Subquery.PROJECT_TASKS_COUNT)
                        .map(Projects.SPRINTS_COUNT, Subquery.PROJECT_SPRINTS_COUNT)
                        .where(Projects._ID + "=?", projectId);
            }
            case PROJECTS_ID_TASKS: {
                final String projectId = Projects.getProjectId(uri);
                return builder.table(Tables.TASKS)
                        .where(Tasks.TASK_PROJECT_ID + "=?", projectId);
            }
            case PROJECTS_ID_SPRINTS: {
                final String projectId = Projects.getProjectId(uri);
                return builder.table(Tables.SPRINTS)
                        .where(Sprints.SPRINT_PROJECT_ID + "=?", projectId);
            }
            case TASKS: {
                return builder.table(Tables.TASKS);
            }
            case TASKS_ID: {
                final String taskId = Tasks.getTaskId(uri);
                return builder.table(Tables.TASKS)
                        .where(Tasks._ID + "=?", taskId);
            }
            case SPRINTS: {
                return builder.table(Tables.SPRINTS);
            }
            case SPRINTS_ID: {
                final String sprintId = Sprints.getSprintId(uri);
                return builder.table(Tables.SPRINTS)
                        .where(Sprints._ID + "=?", sprintId);
            }
            case USERS: {
                return builder.table(Tables.USERS);
            }
            case USERS_ID: {
                final String userId = Users.getUserId(uri);
                return builder.table(Tables.USERS)
                        .where(Users._ID + "=?", userId);
            }
            case USERS_ID_TASKS: {
                final String userId = Users.getUserId(uri);
                return builder.table(Tables.TASKS)
                        .where(Tasks.TASK_USER_ID + "=?", userId);
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private interface Subquery {
        String PROJECT_TASKS_COUNT = "(SELECT COUNT(" + Qualified.TASKS_BASE_ID + ") FROM "
                + Tables.TASKS + " WHERE " + Qualified.TASKS_PROJECT_ID + "="
                + Qualified.PROJECTS_BASE_ID + ")";
        String PROJECT_SPRINTS_COUNT = "(SELECT COUNT(" + Qualified.SPRINTS_BASE_ID + ") FROM "
                + Tables.SPRINTS + " WHERE " + Qualified.SPRINTS_PROJECT_ID + "="
                + Qualified.PROJECTS_BASE_ID + ")";
    }

    /**
     * {@link ScheduleContract} fields that are fully qualified with a specific
     * parent {@link Tables}. Used when needed to work around SQL ambiguity.
     */
    private interface Qualified {
        String TASKS_PROJECT_ID = Tables.TASKS + "." + Tasks.TASK_PROJECT_ID;
        String SPRINTS_PROJECT_ID = Tables.SPRINTS + "." + Sprints.SPRINT_PROJECT_ID;

        String PROJECTS_BASE_ID = Tables.PROJECTS + "." + Projects._ID;
        String TASKS_BASE_ID = Tables.TASKS + "." + Tasks._ID;
        String SPRINTS_BASE_ID = Tables.SPRINTS + "." + Sprints._ID;
        // String USERS_BASE_ID = Tables.USERS + "." + Users._ID;

    }

}

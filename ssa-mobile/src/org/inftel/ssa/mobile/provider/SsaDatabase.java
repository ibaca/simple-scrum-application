
package org.inftel.ssa.mobile.provider;

import org.inftel.ssa.mobile.provider.SsaContract.ProjectsColumns;
import org.inftel.ssa.mobile.provider.SsaContract.SprintsColumns;
import org.inftel.ssa.mobile.provider.SsaContract.SyncColumns;
import org.inftel.ssa.mobile.provider.SsaContract.TasksColumns;
import org.inftel.ssa.mobile.provider.SsaContract.UsersColumns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class SsaDatabase extends SQLiteOpenHelper {

    private static final String TAG = "SsaDatabase";

    private static final String DATABASE_NAME = "ssa.db";

    private static final int DATABASE_VERSION = 1;

    interface Tables {
        String PROJECTS = "projects";
        String TASKS = "tasks";
        String SPRINTS = "sprints";
        String USERS = "users";
        String PROJECTS_USERS = "projects_users";

        String PROJECTS_USERS_JOIN_USERS = "projects_users " +
                "LEFT OUTER JOIN users ON projects_users.user_id=users._id";
    }

    public interface ProjectsUsers {
        String PROJECT_ID = "project_id";
        String USER_ID = "user_id";
    }

    public SsaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.PROJECTS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ProjectsColumns.PROJECT_NAME + " TEXT,"
                + ProjectsColumns.PROJECT_SUMMARY + " TEXT,"
                + ProjectsColumns.PROJECT_DESCRIPTION + " TEXT,"
                + ProjectsColumns.PROJECT_OPENED + " TEXT,"
                + ProjectsColumns.PROJECT_STARTED + " TEXT,"
                + ProjectsColumns.PROJECT_CLOSE + " TEXT,"
                + ProjectsColumns.PROJECT_COMPANY + " TEXT,"
                + ProjectsColumns.PROJECT_LINKS + " TEXT,"
                + ProjectsColumns.PROJECT_LABELS + " TEXT,"
                + ProjectsColumns.PROJECT_LICENSE + " TEXT,"
                + SyncColumns.STABLE_ID + " TEXT,"
                + SyncColumns.SYNC_STATUS + " INTEGER)");

        db.execSQL("CREATE TABLE " + Tables.PROJECTS_USERS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ProjectsUsers.PROJECT_ID + " INTEGER NOT NULL,"
                + ProjectsUsers.USER_ID + " INTEGER NOT NULL,"
                + "UNIQUE ( " + ProjectsUsers.PROJECT_ID + "," + ProjectsUsers.USER_ID + ")"
                + " ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.TASKS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TasksColumns.TASK_DESCRIPTION + " TEXT,"
                + TasksColumns.TASK_SUMMARY + " TEXT,"
                + TasksColumns.TASK_ESTIMATED + " INTEGER,"
                + TasksColumns.TASK_BURNED + " INTEGER,"
                + TasksColumns.TASK_REMAINING + " INTEGER,"
                + TasksColumns.TASK_PRIORITY + " INTEGER,"
                + TasksColumns.TASK_BEGINDATE + " DATE,"
                + TasksColumns.TASK_ENDDATE + " DATE,"
                + TasksColumns.TASK_STATUS + " INTEGER,"
                + TasksColumns.TASK_CREATED + " DATE,"
                + TasksColumns.TASK_COMMENTS + " TEXT,"
                + TasksColumns.TASK_SPRINT_ID + " INTEGER,"
                + TasksColumns.TASK_PROJECT_ID + " INTEGER,"
                + TasksColumns.TASK_USER_ID + " INTEGER,"
                + SyncColumns.STABLE_ID + " TEXT,"
                + SyncColumns.SYNC_STATUS + " INTEGER)");

        db.execSQL("CREATE TABLE " + Tables.SPRINTS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SprintsColumns.SPRINT_SUMMARY + " TEXT,"
                + SprintsColumns.SPRINT_PROJECT_ID + " INTEGER,"
                + SyncColumns.STABLE_ID + " TEXT,"
                + SyncColumns.SYNC_STATUS + " INTEGER)");

        db.execSQL("CREATE TABLE " + Tables.USERS + " ("
                + BaseColumns._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UsersColumns.USER_FULLNAME + " TEXT,"
                + UsersColumns.USER_NICKNAME + " TEXT,"
                + UsersColumns.USER_EMAIL + " TEXT,"
                + UsersColumns.USER_NUMBER + " TEXT,"
                + UsersColumns.USER_COMPANY + " TEXT,"
                + UsersColumns.USER_PASS + " TEXT,"
                + UsersColumns.USER_ROLE + " TEXT,"
                + SyncColumns.STABLE_ID + " TEXT,"
                + SyncColumns.SYNC_STATUS + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Destroying old data during upgrade");
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PROJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PROJECTS_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.SPRINTS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.USERS);
    }
}

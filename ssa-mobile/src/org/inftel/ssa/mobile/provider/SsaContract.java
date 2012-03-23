
package org.inftel.ssa.mobile.provider;

import static android.content.ContentResolver.CURSOR_DIR_BASE_TYPE;
import static android.content.ContentResolver.CURSOR_ITEM_BASE_TYPE;
import android.net.Uri;
import android.provider.BaseColumns;

final public class SsaContract {

    /**
     * Value for {@link SyncColumns#SYNC_STATUS} indicating that an entry has
     * been locally deleted.
     */
    public static final int STATUS_DELETED = -1;
    /**
     * Value for {@link SyncColumns#SYNC_STATUS} indicating that an entry is
     * syncqued.
     */
    public static final int STATUS_SYNC = 0;
    /**
     * Value for {@link SyncColumns#SYNC_STATUS} indicating that an entry has
     * been locally created.
     */
    public static final int STATUS_CREATED = 1;
    /**
     * Value for {@link SyncColumns#SYNC_STATUS} indicating that an entry has
     * some pending changes.
     */
    public static final int STATUS_DIRTY = 2;

    public interface SyncColumns {
        /** Id que relaciona la entidad local con la remota. */
        String STABLE_ID = "stable_id";
        /** Estado de sincronizacion. */
        String SYNC_STATUS = "sync_status";
    }

    interface ProjectsColumns {
        static final String PROJECT_NAME = "name";
        static final String PROJECT_SUMMARY = "summary";
        static final String PROJECT_DESCRIPTION = "description";
        static final String PROJECT_OPENED = "opened";
        static final String PROJECT_STARTED = "started";
        static final String PROJECT_CLOSE = "close";
        static final String PROJECT_COMPANY = "company";
        static final String PROJECT_LINKS = "links";
        static final String PROJECT_LABELS = "labels";
        static final String PROJECT_LICENSE = "license";
    }

    interface TasksColumns {
        static final String TASK_DESCRIPTION = "description";
        static final String TASK_SUMMARY = "summary";
        static final String TASK_ESTIMATED = "estimated";
        static final String TASK_BURNED = "burned";
        static final String TASK_REMAINING = "remaining";
        static final String TASK_PRIORITY = "priority";
        static final String TASK_BEGINDATE = "begin_date";
        static final String TASK_ENDDATE = "enddate";
        static final String TASK_STATUS = "status";
        static final String TASK_USER_ID = "user";
        static final String TASK_SPRINT_ID = "sprint";
        static final String TASK_PROJECT_ID = "project_id";
        static final String TASK_COMMENTS = "comments";
        static final String TASK_CREATED = "created";
    }

    interface SprintsColumns {
        static final String SPRINT_SUMMARY = "summary";
        static final String SPRINT_PROJECT_ID = "project_id";
    }

    interface UsersColumns {
        static final String USER_FULLNAME = "fullname";
        static final String USER_NICKNAME = "nickname";
        static final String USER_EMAIL = "email";
        static final String USER_COMPANY = "company";
        static final String USER_PASS = "password";
        static final String USER_ROLE = "user_role";
        static final String USER_NUMBER = "number";
    }

    public static final String CONTENT_AUTHORITY = "org.inftel.ssa.mobile";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_PROJECTS = "projects";
    private static final String PATH_TASKS = "tasks";
    private static final String PATH_SPRINTS = "sprints";
    private static final String PATH_USERS = "users";

    private static final String ASC = "ASC";

    final public static class Projects implements ProjectsColumns, SyncColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon().appendPath(PATH_PROJECTS).build();

        public static final String CONTENT_TYPE =
                CURSOR_DIR_BASE_TYPE + "/vnd.inftel.ssa.project";
        public static final String CONTENT_ITEM_TYPE =
                CURSOR_ITEM_BASE_TYPE + "/vnd.inftel.ssa.project";

        /** Count of {@link Tasks} inside given project. */
        public static final String TASKS_COUNT = "tasks_count";
        /** Count of {@link Sprints} inside given project. */
        public static final String SPRINTS_COUNT = "sprints_count";
        /** Count of {@link Users} inside given project. */
        public static final String USERS_COUNT = "users_count";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = ProjectsColumns.PROJECT_NAME + ASC;

        public static Uri buildProjectUri(String projectId) {
            return CONTENT_URI.buildUpon().appendPath(projectId).build();
        }

        public static Uri buildTasksDirUri(String projectId) {
            return CONTENT_URI.buildUpon().appendPath(projectId).appendPath(PATH_TASKS).build();
        }

        public static Uri buildSprintsDirUri(String projectId) {
            return CONTENT_URI.buildUpon().appendPath(projectId).appendPath(PATH_SPRINTS).build();
        }

        public static Uri buildUsersDirUri(String projectId) {
            return CONTENT_URI.buildUpon().appendPath(projectId).appendPath(PATH_USERS).build();
        }

        public static String getProjectId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        private Projects() {
        }
    }

    final public static class Tasks implements TasksColumns, SyncColumns, BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon().appendPath(PATH_TASKS).build();

        public static final String CONTENT_TYPE =
                CURSOR_DIR_BASE_TYPE + "/vnd.inftel.ssa.task";
        public static final String CONTENT_ITEM_TYPE =
                CURSOR_ITEM_BASE_TYPE + "/vnd.inftel.ssa.task";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = TasksColumns.TASK_STATUS + ASC;

        public static Uri buildTasktUri(String taskId) {
            return CONTENT_URI.buildUpon().appendPath(taskId).build();
        }

        public static String getTaskId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        private Tasks() {
        }
    }

    final public static class Sprints implements SprintsColumns, SyncColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon().appendPath(PATH_SPRINTS).build();

        public static final String CONTENT_TYPE =
                CURSOR_DIR_BASE_TYPE + "/vnd.inftel.ssa.sprint";
        public static final String CONTENT_ITEM_TYPE =
                CURSOR_ITEM_BASE_TYPE + "/vnd.inftel.ssa.sprint";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = SprintsColumns.SPRINT_SUMMARY + ASC;

        public static Uri buildSprintUri(String sprintId) {
            return CONTENT_URI.buildUpon().appendPath(sprintId).build();
        }

        public static String getSprintId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        private Sprints() {
        }
    }

    final public static class Users implements UsersColumns, SyncColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon().appendPath(PATH_USERS).build();

        public static final String CONTENT_TYPE =
                CURSOR_DIR_BASE_TYPE + "/vnd.inftel.ssa.user";
        public static final String CONTENT_ITEM_TYPE =
                CURSOR_ITEM_BASE_TYPE + "/vnd.inftel.ssa.user";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = UsersColumns.USER_FULLNAME + ASC;

        public static Uri buildUserUri(String userId) {
            return CONTENT_URI.buildUpon().appendPath(userId).build();
        }

        public static Uri buildTasksDirUri(String userId) {
            return CONTENT_URI.buildUpon().appendPath(userId).appendPath(PATH_TASKS).build();
        }

        public static String getUserId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        private Users() {
        }
    }

    private SsaContract() {
    }

}

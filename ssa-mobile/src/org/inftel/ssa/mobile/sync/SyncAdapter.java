
package org.inftel.ssa.mobile.sync;

import static android.provider.BaseColumns._ID;
import static org.inftel.ssa.mobile.util.Lists.strings;
import static org.inftel.ssa.mobile.util.Util.getRequestFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.inftel.ssa.domain.ProjectProxy;
import org.inftel.ssa.domain.TaskProxy;
import org.inftel.ssa.domain.TaskStatus;
import org.inftel.ssa.domain.UserProxy;
import org.inftel.ssa.mobile.provider.SsaContract;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.provider.SsaContract.SyncColumns;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;
import org.inftel.ssa.mobile.provider.SsaContract.Users;
import org.inftel.ssa.mobile.provider.SsaDatabase.ProjectsUsers;
import org.inftel.ssa.mobile.util.Util;
import org.inftel.ssa.services.SsaRequestContext;
import org.inftel.ssa.services.SsaRequestFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String SYNC_TASKS_LASTUPDATE = "sync_project_lastupdate";
    private static final String TAG = "SyncAdapter";
    // private static final boolean NOTIFY_AUTH_FAILURE = true;

    private final AccountManager mAccountManager;
    private final Context mContext;

    // Variables compartidas durante el proceso de sincronizacion
    private SsaRequestFactory mRequestFactory;
    private long mLastUpdateCalculator;
    private Account mAccount;

    private final SharedPreferences mPreferences;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
        mPreferences = Util.getSharedPreferences(context);
    }

    @Override
    public void onPerformSync(final Account account, final Bundle extras, final String authority,
            final ContentProviderClient provider, final SyncResult syncResult) {
        Log.d(TAG, "onPerformSync: account=" + account);
        Log.d(TAG, "onPerformSync: extras=" + extras);
        Log.d(TAG, "onPerformSync: authority=" + authority);
        Log.d(TAG, "onPerformSync: provider=" + provider);

        boolean upload = extras.containsKey(ContentResolver.SYNC_EXTRAS_UPLOAD);

        mAccount = account;
        mRequestFactory = getRequestFactory(mContext, SsaRequestFactory.class);
        final SsaRequestFactory rf = mRequestFactory;

        // Perform UPLOADS
        if (upload) {
            try {
                Log.i(TAG, "uploading data to server...");
                uploadProjects(provider, rf);
                uploadTasks(provider, rf);
            } catch (Exception e) {
                Log.w(TAG, "problema durante la subida de actualizaciones: " + e.getMessage(), e);
                syncResult.stats.numIoExceptions++;
            }
            Log.i(TAG, "just uploading data... skipping full sync");
            return;
        }

        // Perform DOWNLOADS
        try {
            Log.i(TAG, "downloading data to server...");
            downloadFromServer(account, provider, syncResult, rf);
        } catch (Exception e) {
            Log.w(TAG, "problema durante la descarga de actualizaciones: " + e.getMessage(), e);
            syncResult.stats.numIoExceptions++;
        }

        // Performs DELETES
    }

    private void downloadFromServer(final Account account, final ContentProviderClient provider,
            final SyncResult syncResult, final SsaRequestFactory rf) {
        // Projects and Users
        final SsaRequestContext projectsAndUsersRequest = rf.ssaRequestContext();
        projectsAndUsersRequest.findUserByEmail(account.name).with("projects.users").to(
                new Receiver<UserProxy>() {
                    @Override
                    public void onSuccess(UserProxy response) {
                        updateProjectsAndUsers(provider, response);
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        Log.w(TAG, "problema al obtener datos " +
                                "de usuario: " + error.getMessage());
                        syncResult.stats.numIoExceptions++;
                    }
                });
        projectsAndUsersRequest.fire();

        // Tasks
        Date lastTasksUpdate = new Date(mPreferences.getLong(SYNC_TASKS_LASTUPDATE, 0));
        mLastUpdateCalculator = lastTasksUpdate.getTime();
        final SsaRequestContext tasksRequest = rf.ssaRequestContext();
        Map<String, Long> projectsLocalRemoteIds = getProjects(provider, tasksRequest);
        for (final String projectLocalId : projectsLocalRemoteIds.keySet()) {

            Log.d(TAG, "Processing tasks from project: " + projectLocalId);
            Long projectRemoteId = projectsLocalRemoteIds.get(projectLocalId);

            // Prepare request
            tasksRequest.findTasksByProjectSince(projectRemoteId, lastTasksUpdate).with("user").to(
                    new Receiver<List<TaskProxy>>() {
                        @Override
                        public void onSuccess(List<TaskProxy> response) {
                            updateTasks(provider, rf, projectLocalId, response);
                        }

                        @Override
                        public void onFailure(ServerFailure error) {
                            Log.w(TAG, "problema al obtener tareas del proyecto " +
                                    "[" + projectLocalId + "]: " + error.getMessage());
                            syncResult.stats.numIoExceptions++;
                        }
                    });

        }
        tasksRequest.fire();
        // Fire se ejecuta de forma sincrona, la siguiente linea es segura
        if (mLastUpdateCalculator != lastTasksUpdate.getTime()) {
            mPreferences.edit().putLong(SYNC_TASKS_LASTUPDATE, mLastUpdateCalculator).apply();
        }
    }

    /** Upload DIRTY projects to the server. */
    private void uploadProjects(final ContentProviderClient provider, final SsaRequestFactory rf)
            throws RemoteException {
        Cursor cursor = provider.query(Projects.CONTENT_URI, strings(
                Projects._ID,
                Projects.PROJECT_CLOSE,
                Projects.PROJECT_COMPANY,
                Projects.PROJECT_DESCRIPTION,
                // Projects.PROJECT_LABELS,
                Projects.PROJECT_LICENSE,
                // Projects.PROJECT_LINKS,
                Projects.PROJECT_NAME,
                Projects.PROJECT_OPENED,
                Projects.PROJECT_OPENED,
                Projects.PROJECT_STARTED,
                Projects.PROJECT_SUMMARY,
                SyncColumns.REMOTE_ID,
                SyncColumns.STABLE_ID,
                SyncColumns.SYNC_STATUS
                ),
                Projects.SYNC_STATUS + "=? "
                        + "OR " + Projects.SYNC_STATUS + "=?"
                        + "OR " + Projects.SYNC_STATUS + " IS NULL",
                strings(SsaContract.STATUS_DIRTY + "", SsaContract.STATUS_CREATED + ""), null);
        SsaRequestContext remoteUpdateRequest = rf.ssaRequestContext();
        while (cursor.moveToNext()) {
            Log.d(TAG, "Sending project to remote server: "
                    + cursor.getString(cursor.getColumnIndex(Projects._ID)));

            // Acumulate changes here
            ProjectProxy edit;

            // Prepare ProjectProxy for editing
            final String projectId = cursor.getString(cursor.getColumnIndex(Projects._ID));
            String syncStat = cursor.getString(cursor.getColumnIndex(Projects.SYNC_STATUS));
            String stableId = cursor.getString(cursor.getColumnIndex(SyncColumns.STABLE_ID));
            if (syncStat == null || Long.parseLong(syncStat) == SsaContract.STATUS_CREATED) {
                // Se comprueba si ya esta configurado StableId del account
                String accountStableId = getAccountStableId(provider, mAccount);
                if (TextUtils.isEmpty(accountStableId)) {
                    // Si no esta definido se espera ya que en el primer update
                    // debería
                    // actualizarse este valor
                    continue; // se pueden realizar updates, pero no crear
                              // nuevos proyectos
                }
                edit = remoteUpdateRequest.create(ProjectProxy.class);
                remoteUpdateRequest.persistProject(edit).to(new Receiver<ProjectProxy>() {
                    @Override
                    public void onSuccess(ProjectProxy response) {
                        Log.d(TAG, "Upload new project persited received: " + response);
                        ContentValues values = new ContentValues();

                        // Sync data
                        final String remoteId = response.getId().toString();
                        final String stableId = mRequestFactory.getHistoryToken(response.stableId());
                        values.put(SyncColumns.REMOTE_ID, remoteId);
                        values.put(SyncColumns.STABLE_ID, stableId);
                        values.put(SyncColumns.SYNC_STATUS, SsaContract.STATUS_SYNC);
                        try {
                            int count = provider.update(Projects.buildProjectUri(projectId),
                                    values, null, null);
                            if (count == 0) {
                                Log.w(TAG, "Updating " + values + " to " + Projects.CONTENT_URI
                                        + " dont affect any row");
                            }
                        } catch (RemoteException e) {
                            Log.e(TAG, "Updating " + values + " to " + Projects.CONTENT_URI
                                    + " failed", e);
                        }

                    }
                });
                Set<UserProxy> owner = new HashSet<UserProxy>(1);
                owner.add(getRemoteByStableId(UserProxy.class, accountStableId));
                edit.setUsers(owner);
            } else { // STATUS_DIRTY
                edit = remoteUpdateRequest.edit(getRemoteByStableId(ProjectProxy.class, stableId));
                remoteUpdateRequest.save(edit).to(new Receiver<EntityProxy>() {
                    @Override
                    public void onSuccess(EntityProxy response) {
                        Log.d(TAG, "Upload updated project persited received: " + response);
                    }
                });
            }

            // Edit ProjectProxy
            String closed = cursor.getString(cursor.getColumnIndex(Projects.PROJECT_CLOSE));
            edit.setClosed((closed == null) ? null : new Date(Long.parseLong(closed)));
            edit.setCompany(cursor.getString(cursor.getColumnIndex(Projects.PROJECT_COMPANY)));
            edit.setDescription(
                    cursor.getString(cursor.getColumnIndex(Projects.PROJECT_DESCRIPTION)));
            // projectProxy.setLabels
            edit.setLicense(cursor.getString(cursor.getColumnIndex(Projects.PROJECT_LICENSE)));
            edit.setName(cursor.getString(cursor.getColumnIndex(Projects.PROJECT_NAME)));
            String opened = cursor.getString(cursor.getColumnIndex(Projects.PROJECT_OPENED));
            edit.setOpened((opened == null) ? null : new Date(Long.parseLong(opened)));
            String started = cursor.getString(cursor.getColumnIndex(Projects.PROJECT_STARTED));
            edit.setStarted((started == null) ? null : new Date(Long.parseLong(started)));
            edit.setSummary(cursor.getString(cursor.getColumnIndex(Projects.PROJECT_SUMMARY)));
        }
        remoteUpdateRequest.fire();
    }

    /** Upload DIRTY tasks to the server. */
    private void uploadTasks(final ContentProviderClient provider, final SsaRequestFactory rf)
            throws RemoteException {
        Cursor cursor = provider.query(Tasks.CONTENT_URI, strings(
                Tasks._ID,
                Tasks.TASK_BEGINDATE,
                Tasks.TASK_BURNED,
                Tasks.TASK_COMMENTS,
                Tasks.TASK_CREATED,
                Tasks.TASK_DESCRIPTION,
                Tasks.TASK_ENDDATE,
                Tasks.TASK_ESTIMATED,
                Tasks.TASK_PRIORITY,
                // Tasks.TASK_PROJECT_ID,
                Tasks.TASK_REMAINING,
                // Tasks.TASK_SPRINT_ID,
                Tasks.TASK_STATUS,
                Tasks.TASK_SUMMARY,
                Tasks.TASK_USER_ID,
                SyncColumns.REMOTE_ID,
                SyncColumns.STABLE_ID,
                SyncColumns.SYNC_STATUS
                ),
                SyncColumns.SYNC_STATUS + "=? "
                        + "OR " + SyncColumns.SYNC_STATUS + "=?"
                        + "OR " + SyncColumns.SYNC_STATUS + " IS NULL",
                strings(SsaContract.STATUS_DIRTY + "", SsaContract.STATUS_CREATED + ""), null);
        SsaRequestContext remoteUpdateRequest = rf.ssaRequestContext();
        while (cursor.moveToNext()) {
            Log.d(TAG, "Sending task to remote server: "
                    + cursor.getString(cursor.getColumnIndex(Tasks._ID)));

            // Acumulate changes here
            TaskProxy edit;

            // Prepare for editing
            final String taskId = cursor.getString(cursor.getColumnIndex(Tasks._ID));
            String syncStat = cursor.getString(cursor.getColumnIndex(Tasks.SYNC_STATUS));
            String stableId = cursor.getString(cursor.getColumnIndex(SyncColumns.STABLE_ID));
            if (syncStat == null || Long.parseLong(syncStat) == SsaContract.STATUS_CREATED) {
                // Se comprueba si ya esta configurado StableId del account
                String accountStableId = getAccountStableId(provider, mAccount);
                if (TextUtils.isEmpty(accountStableId)) {
                    // Si no esta definido se espera ya que en el primer update
                    // debería actualizarse este valor
                    continue; // se pueden realizar updates, pero no crear
                              // nuevos tasks
                }
                edit = remoteUpdateRequest.create(TaskProxy.class);
                remoteUpdateRequest.persistTask(edit).to(new Receiver<TaskProxy>() {
                    @Override
                    public void onSuccess(TaskProxy response) {
                        Log.d(TAG, "Upload new task persited received: " + response);
                        ContentValues values = new ContentValues();

                        // Sync data
                        final String remoteId = response.getId().toString();
                        final String stableId = mRequestFactory.getHistoryToken(response.stableId());
                        values.put(SyncColumns.REMOTE_ID, remoteId);
                        values.put(SyncColumns.STABLE_ID, stableId);
                        values.put(SyncColumns.SYNC_STATUS, SsaContract.STATUS_SYNC);
                        try {
                            int count = provider.update(Tasks.buildTasktUri(taskId),
                                    values, null, null);
                            if (count == 0) {
                                Log.w(TAG, "Updating " + values + " to " + Tasks.CONTENT_URI
                                        + " dont affect any row");
                            }
                        } catch (RemoteException e) {
                            Log.e(TAG, "Updating " + values + " to " + Tasks.CONTENT_URI
                                    + " failed", e);
                        }
                    }
                });
            } else { // STATUS_DIRTY
                edit = remoteUpdateRequest.edit(getRemoteByStableId(TaskProxy.class, stableId));
                remoteUpdateRequest.save(edit).to(new Receiver<EntityProxy>() {
                    @Override
                    public void onSuccess(EntityProxy response) {
                        Log.d(TAG, "Upload updated task persited received: " + response);
                    }
                });
            }

            // Edit ProjectProxy
            String begin = cursor.getString(cursor.getColumnIndex(Tasks.TASK_BEGINDATE));
            edit.setBeginDate((begin == null) ? null : new Date(Long.parseLong(begin)));

            String burned = cursor.getString(cursor.getColumnIndex(Tasks.TASK_BURNED));
            edit.setBurned((burned == null) ? null : Integer.parseInt(burned));

            edit.setDescription(
                    cursor.getString(cursor.getColumnIndex(Tasks.TASK_DESCRIPTION)));

            String end = cursor.getString(cursor.getColumnIndex(Tasks.TASK_ENDDATE));
            edit.setEndDate((end == null) ? null : new Date(Long.parseLong(end)));

            String estimated = cursor.getString(cursor.getColumnIndex(Tasks.TASK_ESTIMATED));
            edit.setEstimated((estimated == null) ? null : Integer.valueOf(estimated));

            String priority = cursor.getString(cursor.getColumnIndex(Tasks.TASK_PRIORITY));
            edit.setPriority((priority == null) ? null : Integer.valueOf(priority));

            String projectId = cursor.getString(cursor.getColumnIndex(Tasks.TASK_PROJECT_ID));
            if (!TextUtils.isEmpty(projectId)) {
                edit.setProject(getRemoteByStableId(ProjectProxy.class,
                        getProjectStableId(provider, projectId)));
            }

            String remaining = cursor.getString(cursor.getColumnIndex(Tasks.TASK_REMAINING));
            edit.setRemaining((remaining == null) ? null : Integer.valueOf(remaining));

            String status = cursor.getString(cursor.getColumnIndex(Tasks.TASK_STATUS));
            edit.setStatus((status == null) ? null : TaskStatus.valueOf(status));

            edit.setSummary(cursor.getString(cursor.getColumnIndex(Tasks.TASK_SUMMARY)));

            String userId = cursor.getString(cursor.getColumnIndex(Tasks.TASK_USER_ID));
            if (!TextUtils.isEmpty(userId)) {
                edit.setUser(getRemoteByStableId(UserProxy.class,
                        getUserStableId(provider, projectId)));
            }
        }
        remoteUpdateRequest.fire();
    }

    protected void updateProjectsAndUsers(ContentProviderClient provider, UserProxy accountData) {
        Set<ProjectProxy> projects = accountData.getProjects();
        Set<UserProxy> users = new HashSet<UserProxy>();

        // For Each Project
        for (ProjectProxy project : projects) {
            updateOrInsertProject(provider, project);
            users.addAll(project.getUsers());
        }

        // For Each Project
        for (UserProxy user : users) {
            updateOrInsertUser(provider, user);
        }

        // Todos los usuarios y proyectos estan insertados y actualizados
        // se procede a actualizar la relacion entre ellos
        for (ProjectProxy project : projects) {
            updateProjectUsersAssociation(provider, project);
        }

    }

    /** Server to mobile update/insert. */
    private void updateOrInsertProject(ContentProviderClient provider, ProjectProxy project) {
        Log.d(TAG, "Updating project from remote server: " + project);

        ContentValues values = new ContentValues();

        // Sync data
        final String remoteId = project.getId().toString();
        final String stableId = mRequestFactory.getHistoryToken(project.stableId());
        values.put(SyncColumns.REMOTE_ID, remoteId);
        values.put(SyncColumns.STABLE_ID, stableId);
        values.put(SyncColumns.SYNC_STATUS, SsaContract.STATUS_SYNC);

        // Project data
        if (project.getClosed() != null) {
            values.put(Projects.PROJECT_CLOSE, project.getClosed().getTime());
        }
        values.put(Projects.PROJECT_COMPANY, project.getCompany());
        values.put(Projects.PROJECT_DESCRIPTION, project.getDescription());
        if (project.getLabels() != null) {
            values.put(Projects.PROJECT_LABELS, project.getLabels().toString());
        }
        values.put(Projects.PROJECT_LICENSE, project.getLicense());
        // values.put(Projects.PROJECT_LINKS, );
        values.put(Projects.PROJECT_NAME, project.getName());
        if (project.getCreated() != null) {// FIXME created? opened? wtf?
            values.put(Projects.PROJECT_OPENED, project.getCreated().getTime());
        }
        if (project.getStarted() != null) {
            values.put(Projects.PROJECT_STARTED, project.getStarted().getTime());
        }
        values.put(Projects.PROJECT_SUMMARY, project.getSummary());

        // Update or insert the project
        try {
            int count = provider.update(Projects.CONTENT_URI, values,
                    Projects.REMOTE_ID + "=?", strings(remoteId));
            if (count == 0) {
                provider.insert(Projects.CONTENT_URI, values);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Adding " + values + " to " + Projects.CONTENT_URI + " failed", ex);
        }
    }

    private void updateOrInsertUser(ContentProviderClient provider, UserProxy user) {
        Log.d(TAG, "Updating user from remote server: " + user);

        ContentValues values = new ContentValues();

        // Sync data
        final String remoteId = user.getId().toString();
        final String stableId = mRequestFactory.getHistoryToken(user.stableId());
        values.put(SyncColumns.REMOTE_ID, remoteId);
        values.put(SyncColumns.STABLE_ID, stableId);
        values.put(SyncColumns.SYNC_STATUS, SsaContract.STATUS_SYNC);

        // User data
        values.put(Users.USER_COMPANY, user.getCompany());
        values.put(Users.USER_EMAIL, user.getEmail());
        values.put(Users.USER_FULLNAME, user.getFullName());
        values.put(Users.USER_NICKNAME, user.getNickname());
        values.put(Users.USER_NUMBER, user.getNickname());
        values.put(Users.USER_PASS, user.getPassword());
        values.put(Users.USER_ROLE, user.getUserRole());

        // Update or insert the user
        try {
            int count = provider.update(Users.CONTENT_URI, values,
                    Users.REMOTE_ID + "=?", strings(remoteId));
            if (count == 0) {
                provider.insert(Users.CONTENT_URI, values);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Adding " + values + " to " + Users.CONTENT_URI + " failed", ex);
        }
    }

    private void updateProjectUsersAssociation(ContentProviderClient provider, ProjectProxy project) {
        String projectStableId = project.getId().toString();
        String projectId = getId(provider, Projects.CONTENT_URI, projectStableId);
        // Insert project user association
        for (UserProxy user : project.getUsers()) {
            Log.d(TAG, "Processing project-user association: " + projectId + " --> " + user);
            String userStableId = user.getId().toString();
            String userId = getId(provider, Users.CONTENT_URI, userStableId);

            ContentValues values = new ContentValues();
            values.put(ProjectsUsers.PROJECT_ID, projectId);
            values.put(ProjectsUsers.USER_ID, userId);
            Uri projectIdUsersUri = Projects.buildUsersDirUri(projectId);
            try {
                provider.insert(projectIdUsersUri, values);
            } catch (RemoteException e) {
                Log.w(TAG, "Adding " + values + " to " + projectIdUsersUri + " failed", e);
            }
        }
    }

    protected void updateTasks(ContentProviderClient provider, SsaRequestFactory rf,
            String projectId, List<TaskProxy> response) {
        for (TaskProxy task : response) {
            Log.d(TAG, "Updating task from remote server: " + task);

            ContentValues values = new ContentValues();

            // Sync data
            final String remoteId = task.getId().toString();
            final String stableId = mRequestFactory.getHistoryToken(task.stableId());
            values.put(SyncColumns.REMOTE_ID, remoteId);
            values.put(SyncColumns.STABLE_ID, stableId);
            values.put(SyncColumns.SYNC_STATUS, SsaContract.STATUS_SYNC);

            // Task data
            if (task.getBeginDate() != null) {
                values.put(Tasks.TASK_BEGINDATE, task.getBeginDate().toString());
            }
            values.put(Tasks.TASK_BURNED, task.getBurned());
            // values.put(Tasks.TASK_COMMENTS, task.get);
            if (task.getCreated() != null) {
                values.put(Tasks.TASK_CREATED, task.getCreated().toString());
            }
            values.put(Tasks.TASK_DESCRIPTION, task.getDescription());
            if (task.getEndDate() != null) {
                values.put(Tasks.TASK_ENDDATE, task.getEndDate().toString());
            }
            values.put(Tasks.TASK_ESTIMATED, task.getEstimated());
            values.put(Tasks.TASK_PRIORITY, task.getPriority());
            values.put(Tasks.TASK_PROJECT_ID, projectId);
            values.put(Tasks.TASK_REMAINING, task.getRemaining());
            // values.put(Tasks.TASK_SPRINT_ID, );
            if (task.getStatus() != null) {
                values.put(Tasks.TASK_STATUS, task.getStatus().toString());
            }
            values.put(Tasks.TASK_SUMMARY, task.getSummary());
            if (task.getUser() != null) {
                values.put(Tasks.TASK_USER_ID, getId(provider, Users.CONTENT_URI,
                        task.getUser().getId().toString()));
            }

            // Update lastUpdateTime calculator
            if (task.getUpdated() != null && mLastUpdateCalculator < task.getUpdated().getTime()) {
                mLastUpdateCalculator = task.getUpdated().getTime();
            }

            // Update or insert the user
            try {
                int count = provider.update(Tasks.CONTENT_URI, values,
                        Tasks.REMOTE_ID + "=?", strings(remoteId));
                if (count == 0) {
                    provider.insert(Tasks.CONTENT_URI, values);
                }
            } catch (Exception ex) {
                Log.e(TAG, "Adding " + values + " to " + Tasks.CONTENT_URI + " failed", ex);
            }
        }

    }

    private String getId(ContentProviderClient provider, Uri uri, String stableId) {
        try {
            Cursor cursor = provider.query(
                    uri, strings(_ID), SyncColumns.REMOTE_ID + "=?", strings(stableId), null);
            if (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(_ID));
            } else {
                Log.w(TAG, "No existe id asociado a " + stableId);
                return null;
            }
        } catch (RemoteException e) {
            Log.w(TAG, "Problema obteniendo id asociado a " + stableId + ": " + e.getMessage(), e);
            return null;
        }
    }

    /** Proyectos en bbdd local, mapa tipo localId => remoteId. */
    private Map<String, Long> getProjects(ContentProviderClient provider,
            SsaRequestContext requestContext) {
        try {
            Cursor cursor = provider.query(Projects.CONTENT_URI,
                    strings(Projects._ID, Projects.REMOTE_ID), null, null, null);
            Map<String, Long> projects = new HashMap<String, Long>();
            while (cursor.moveToNext()) {
                String localId = cursor.getString(cursor.getColumnIndex(Projects._ID));

                if (cursor.isNull(cursor.getColumnIndex(Projects.REMOTE_ID))) {
                    // Esta situacion no debería darse, si se da seguramente se
                    // daba a que se ha creado localmente y fallo la subida
                    // remota, o se creo localmente y no se establecio
                    // correctamente el flag de sincronizacion
                    Log.w(TAG, "Proyecto " + localId + " no tiene definido id remoto");
                    continue;
                }

                long remoteId = cursor.getLong(cursor.getColumnIndex(Projects.REMOTE_ID));

                projects.put(localId, remoteId);
            }
            return projects;
        } catch (RemoteException e) {
            Log.w(TAG, "Problema obteniendo proyectos: " + e.getMessage(), e);
            return Collections.emptyMap();
        }

    }

    private <T extends EntityProxy> T getRemoteByStableId(final Class<T> clazz,
            final String stableId) {
        SsaRequestContext getProjectRequest = mRequestFactory.ssaRequestContext();
        final List<T> getResult = new ArrayList<T>(1);
        getProjectRequest.find(mRequestFactory.getProxyId(stableId)).fire(
                new Receiver<EntityProxy>() {
                    @Override
                    public void onSuccess(EntityProxy response) {
                        getResult.add(clazz.cast(response));
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        Log.w(TAG, "Problema al obtener la entidad remota " +
                                "[" + stableId + "]: " + error.getMessage());
                    }
                });
        if (getResult.isEmpty()) {
            return null;
        } else {
            return getResult.iterator().next();
        }
    }

    private String getAccountStableId(ContentProviderClient provider, Account account) {
        try {
            Cursor cursor = provider.query(Users.CONTENT_URI, strings(Users.STABLE_ID),
                    Users.USER_EMAIL + "=?", strings(account.name), null);
            if (cursor.moveToNext()) {
                String stableId = cursor.getString(cursor.getColumnIndex(Users.STABLE_ID));
                if (!TextUtils.isEmpty(stableId)) {
                    return stableId;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error inesperado obteniendo stableId del account", e);
        }
        return null;
    }

    private String getUserStableId(ContentProviderClient provider, String userId) {
        try {
            Cursor cursor = provider.query(Users.buildUserUri(userId),
                    strings(Users.STABLE_ID), null, null, null);
            if (cursor.moveToNext()) {
                String stableId = cursor.getString(cursor.getColumnIndex(Users.STABLE_ID));
                if (!TextUtils.isEmpty(stableId)) {
                    return stableId;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error inesperado obteniendo stableId del usuario " + userId, e);
        }
        return null;
    }

    private String getProjectStableId(ContentProviderClient provider, String projectId) {
        try {
            Cursor cursor = provider.query(Projects.buildProjectUri(projectId),
                    strings(Projects.STABLE_ID), null, null, null);
            if (cursor.moveToNext()) {
                String stableId = cursor.getString(cursor.getColumnIndex(Users.STABLE_ID));
                if (!TextUtils.isEmpty(stableId)) {
                    return stableId;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error inesperado obteniendo stableId del proyecto " + projectId, e);
        }
        return null;
    }

}

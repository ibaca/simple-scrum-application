
package org.inftel.ssa.mobile.sync;

import static android.provider.BaseColumns._ID;
import static org.inftel.ssa.mobile.provider.SsaContract.SyncColumns.STABLE_ID;
import static org.inftel.ssa.mobile.util.Lists.strings;
import static org.inftel.ssa.mobile.util.Util.getRequestFactory;

import java.util.HashSet;
import java.util.Set;

import org.inftel.ssa.domain.ProjectProxy;
import org.inftel.ssa.domain.UserProxy;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.provider.SsaContract.Users;
import org.inftel.ssa.mobile.provider.SsaDatabase.ProjectsUsers;
import org.inftel.ssa.services.SsaRequestContext;
import org.inftel.ssa.services.SsaRequestFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
    private static final String SYNC_MARKER_KEY = "org.inftel.ssa.mobile.marker";
    // private static final boolean NOTIFY_AUTH_FAILURE = true;

    private final AccountManager mAccountManager;
    private final Context mContext;

    // private final SharedPreferences mPreferences;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
        // mPreferences = Util.getSharedPreferences(context);
    }

    @Override
    public void onPerformSync(final Account account, final Bundle extras, final String authority,
            final ContentProviderClient provider, final SyncResult syncResult) {
        Log.d(TAG, "onPerformSync: account=" + account);
        Log.d(TAG, "onPerformSync: extras=" + extras);
        Log.d(TAG, "onPerformSync: authority=" + authority);
        Log.d(TAG, "onPerformSync: provider=" + provider);

        // Perform UPLOADS
        if (extras.getBoolean("upload")) {
            Log.i(TAG, "just uploading data... skipping full sync");
            return;
        }

        // Perform DOWNLOADS

        // Date lastProjectUpdate = new
        // Date(mPreferences.getInt("sync_project_lastupdate", 0));
        final SsaRequestFactory rf = getRequestFactory(mContext, SsaRequestFactory.class);
        final SsaRequestContext request = rf.ssaRequestContext();
        request.findUserByEmail(account.name).with("projects.users").fire(
                new Receiver<UserProxy>() {
                    @Override
                    public void onSuccess(UserProxy response) {
                        updateProjectsAndUsers(provider, rf, response);
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        Log.w(TAG, "problema al obtener datos " +
                                "de usuario: " + error.getMessage());
                        syncResult.stats.numIoExceptions++;
                    }
                });

        // Performs DELETES
    }

    protected void updateProjectsAndUsers(
            final ContentProviderClient provider,
            final SsaRequestFactory requestFactory,
            final UserProxy accountData) {
        Set<ProjectProxy> projects = accountData.getProjects();
        Set<UserProxy> users = new HashSet<UserProxy>();

        // For Each Project
        for (ProjectProxy project : projects) {
            updateOrInsertProject(provider, requestFactory, project);
            users.addAll(project.getUsers());
        }

        // For Each Project
        for (UserProxy user : users) {
            updateOrInsertUser(provider, requestFactory, user);
        }

        // Todos los usuarios y proyectos estan insertados y actualizados
        // se procede a actualizar la relacion entre ellos
        for (ProjectProxy project : projects) {
            updateProjectUsers(provider, requestFactory, project);
        }

    }

    private void updateOrInsertProject(final ContentProviderClient provider,
            final SsaRequestFactory rf, ProjectProxy project) {
        Log.d(TAG, "Processing project: " + project);

        ContentValues values = new ContentValues();
        final String stableId = rf.getHistoryToken(project.stableId());
        values.put(Projects.STABLE_ID, stableId);
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
                    Projects.STABLE_ID + "=?", strings(stableId));
            if (count == 0) {
                provider.insert(Projects.CONTENT_URI, values);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Adding " + values + " to " + Projects.CONTENT_URI + " failed", ex);
        }
    }

    private void updateOrInsertUser(
            final ContentProviderClient provider,
            final SsaRequestFactory rf,
            final UserProxy user) {
        Log.d(TAG, "Processing user: " + user);

        ContentValues values = new ContentValues();
        final String stableId = rf.getHistoryToken(user.stableId());
        values.put(Users.STABLE_ID, stableId);
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
                    Users.STABLE_ID + "=?", strings(stableId));
            if (count == 0) {
                provider.insert(Users.CONTENT_URI, values);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Adding " + values + " to " + Users.CONTENT_URI + " failed", ex);
        }
    }

    private void updateProjectUsers(ContentProviderClient provider,
            SsaRequestFactory requestFactory, ProjectProxy project) {
        String projectStableId = requestFactory.getHistoryToken(project.stableId());
        String projectId = getId(provider, Projects.CONTENT_URI, projectStableId);
        // Insert project user association
        for (UserProxy user : project.getUsers()) {
            Log.d(TAG, "Processing project-user association: " + projectId + " --> " + user);
            String userStableId = requestFactory.getHistoryToken(user.stableId());
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

    private String getId(ContentProviderClient provider, Uri uri, String stableId) {
        try {
            Cursor cursor = provider.query(
                    uri, strings(_ID), STABLE_ID + "=?", strings(stableId), null);
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

    /**
     * This helper function fetches the last known high-water-mark we received
     * from the server - or 0 if we've never synced.
     * 
     * @param account the account we're syncing
     * @return the change high-water-mark
     */
    private long getServerSyncMarker(Account account) {
        String markerString = mAccountManager.getUserData(account, SYNC_MARKER_KEY);
        if (!TextUtils.isEmpty(markerString)) {
            return Long.parseLong(markerString);
        }
        return 0;
    }

    /**
     * Save off the high-water-mark we receive back from the server.
     * 
     * @param account The account we're syncing
     * @param marker The high-water-mark we want to save.
     */
    private void setServerSyncMarker(Account account, long marker) {
        mAccountManager.setUserData(account, SYNC_MARKER_KEY, Long.toString(marker));
    }
}

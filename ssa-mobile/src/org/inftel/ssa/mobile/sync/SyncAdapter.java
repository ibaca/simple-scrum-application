
package org.inftel.ssa.mobile.sync;

import static org.inftel.ssa.mobile.util.Util.getRequestFactory;

import java.util.Set;

import org.inftel.ssa.domain.ProjectProxy;
import org.inftel.ssa.domain.UserProxy;
import org.inftel.ssa.services.SsaRequestContext;
import org.inftel.ssa.services.SsaRequestFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
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
    public void onPerformSync(final Account account, Bundle extras, String authority,
            ContentProviderClient provider, final SyncResult syncResult) {
        Log.d(TAG, "onPerformSync: account=" + account);
        Log.d(TAG, "onPerformSync: extras=" + extras);
        Log.d(TAG, "onPerformSync: authority=" + authority);
        Log.d(TAG, "onPerformSync: provider=" + provider);

        // Date lastProjectUpdate = new
        // Date(mPreferences.getInt("sync_project_lastupdate", 0));
        SsaRequestFactory requestFactory = getRequestFactory(mContext,
                SsaRequestFactory.class);
        SsaRequestContext requestContext = requestFactory.ssaRequestContext();
        requestContext.findUserByEmail(account.name).with("projects.users").fire(
                new Receiver<UserProxy>() {
                    @Override
                    public void onSuccess(UserProxy response) {
                        updateProjectsAndUsers(response);
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        Log.w(TAG, "problema al obtener datos " +
                                "de usuario: " + error.getMessage());
                        syncResult.stats.numIoExceptions++;
                    }
                });
    }

    protected void updateProjectsAndUsers(UserProxy response) {
        Set<ProjectProxy> projects = response.getProjects();
        for (ProjectProxy project : projects) {
            Set<UserProxy> users = project.getUsers();
            Log.d(TAG, "usuarios del proyecto " + project.getName() + ": " + users);
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

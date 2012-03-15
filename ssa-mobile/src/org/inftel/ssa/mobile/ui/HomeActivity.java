
package org.inftel.ssa.mobile.ui;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.util.AnalyticsUtils;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnalyticsUtils.getInstance(this).trackPageView("/Home");

        setContentView(R.layout.activity_home);
        getActivityHelper().setupActionBar(null, 0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupHomeActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu_items, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            triggerRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void triggerRefresh() {
        // final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
        // SyncService.class);
        // intent.putExtra(SyncService.EXTRA_STATUS_RECEIVER,
        // mSyncStatusUpdaterFragment.mReceiver);
        // startService(intent);
        //
        // if (mTagStreamFragment != null) {
        // mTagStreamFragment.refresh();
        // }
    }

    private void updateRefreshStatus(boolean refreshing) {
        getActivityHelper().setRefreshActionButtonCompatState(refreshing);
    }

}


package org.inftel.ssa.mobile.ui.phone;

import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.ProjectListFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ProjectListActivity extends BaseSinglePaneActivity {
    private static final String TAG = "ProjectListActivity";

    @Override
    protected Fragment onCreatePane() {
        return new ProjectListFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupSubActivity();
    }

}


package org.inftel.ssa.mobile.ui.phone;

import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.TaskDetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TaskDetailActivity extends BaseSinglePaneActivity {
    @Override
    protected Fragment onCreatePane() {
        return new TaskDetailFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupSubActivity();
    }
}

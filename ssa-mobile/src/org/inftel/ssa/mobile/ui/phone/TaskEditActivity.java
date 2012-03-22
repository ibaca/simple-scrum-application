
package org.inftel.ssa.mobile.ui.phone;

import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.TaskEditFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TaskEditActivity extends BaseSinglePaneActivity {
    TaskEditFragment taskEditFragment;

    @Override
    protected Fragment onCreatePane() {
        return new TaskEditFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupSubActivity();
    }

}

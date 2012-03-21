
package org.inftel.ssa.mobile.ui.phone;

import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.SprintDetailFragment;
import org.inftel.ssa.mobile.ui.fragments.SprintDetailPagerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SprintDetailActivity extends BaseSinglePaneActivity {
    @Override
    protected Fragment onCreatePane() {
        return new SprintDetailPagerFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupSubActivity();
    }
}

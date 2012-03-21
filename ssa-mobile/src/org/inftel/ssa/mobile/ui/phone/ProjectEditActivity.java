
package org.inftel.ssa.mobile.ui.phone;

import static android.content.Intent.ACTION_VIEW;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.ProjectEditFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ProjectEditActivity extends BaseSinglePaneActivity {
    ProjectEditFragment projectEditFragment;

    @Override
    protected Fragment onCreatePane() {
        projectEditFragment = new ProjectEditFragment();
        return projectEditFragment;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupSubActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_project_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                projectEditFragment.saveProject();
                final Intent intent = new Intent(ACTION_VIEW,
                        ProjectContentProvider.CONTENT_URI);
                startActivity(intent);
                return true;
        }
        return onOptionsItemSelected(item);
    }

}

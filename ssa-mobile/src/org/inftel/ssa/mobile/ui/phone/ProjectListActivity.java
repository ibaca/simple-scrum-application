
package org.inftel.ssa.mobile.ui.phone;

import static android.content.Intent.ACTION_INSERT;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.ProjectListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_project_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Log.d(TAG, "Creando nuevo proyecto");
                startActivity(new Intent(ACTION_INSERT, Projects.CONTENT_URI));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

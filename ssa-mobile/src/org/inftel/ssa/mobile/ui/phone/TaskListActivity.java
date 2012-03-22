
package org.inftel.ssa.mobile.ui.phone;

import static android.content.Intent.ACTION_INSERT;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;
import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.TaskListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TaskListActivity extends BaseSinglePaneActivity {
	private static final String TAG = "TaskListActivity";
	
    @Override
    protected Fragment onCreatePane() {
        return new TaskListFragment();
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
        inflater.inflate(R.menu.ssa_task_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Log.d(TAG, "Creando nueva tarea");
                startActivity(new Intent(ACTION_INSERT, Tasks.CONTENT_URI));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

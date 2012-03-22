
package org.inftel.ssa.mobile.ui.phone;

import static android.content.Intent.ACTION_VIEW;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;
import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.TaskEditFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TaskEditActivity extends BaseSinglePaneActivity {
	TaskEditFragment taskEditFragment;
	
    @Override
    protected Fragment onCreatePane() {
        taskEditFragment = new TaskEditFragment();

        return taskEditFragment;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupSubActivity();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ssa_task_edit_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                taskEditFragment.saveTask();
                startActivity(new Intent(ACTION_VIEW, Tasks.CONTENT_URI));
                return true;
        }
        return onOptionsItemSelected(item);
    }
}

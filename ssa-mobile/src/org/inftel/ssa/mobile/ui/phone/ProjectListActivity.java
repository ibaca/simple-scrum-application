
package org.inftel.ssa.mobile.ui.phone;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;
import org.inftel.ssa.mobile.contentproviders.TaskContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskTable;
import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.ProjectListFragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ProjectListActivity extends BaseSinglePaneActivity {
    @Override
    protected Fragment onCreatePane() {
        insertProjectsDataTable();
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
                Log.d(getClass().getSimpleName(), "Creando nuevo proyecto");
                final Intent intent = new Intent(Intent.ACTION_INSERT,
                        ProjectContentProvider.CONTENT_URI);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertProjectsDataTable() {
        Log.d(getClass().getSimpleName(), "Insertando datos prueba");
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        cr.delete(ProjectContentProvider.CONTENT_URI,
                ProjectTable.KEY_NAME + " = 'Proyecto 1'", null);
        cr.delete(ProjectContentProvider.CONTENT_URI,
                ProjectTable.KEY_NAME + " = 'Proyecto 2'", null);
        cr.delete(ProjectContentProvider.CONTENT_URI,
                ProjectTable.KEY_NAME + " = 'Proyecto 3'", null);
        cr.delete(ProjectContentProvider.CONTENT_URI,
                ProjectTable.KEY_NAME + " = 'Proyecto 4'", null);
        cr.delete(TaskContentProvider.CONTENT_URI,
                TaskTable.COLUMN_PROJECT + " = '1'", null);

        values.put(TaskTable.COLUMN_PROJECT, "1");
        values.put(TaskTable.COLUMN_STATUS, "2");
        cr.insert(TaskContentProvider.CONTENT_URI, values);
        values.put(TaskTable.COLUMN_PROJECT, "1");
        values.put(TaskTable.COLUMN_STATUS, "1");
        cr.insert(TaskContentProvider.CONTENT_URI, values);
        values.clear();

        values.put(ProjectTable.KEY_NAME, "Proyecto 1");
        values.put(ProjectTable.KEY_SUMMARY, "Pasos");
        values.put(ProjectTable.KEY_DESCRIPTION, "Gestion de alarmas para ancianos");
        values.put(ProjectTable.KEY_OPENED, "21/3/2012");
        values.put(ProjectTable.KEY_STARTED, "21/3/2012");
        values.put(ProjectTable.KEY_CLOSE, "12/3/2012");
        values.put(ProjectTable.KEY_COMPANY, "Inftel");
        values.put(ProjectTable.KEY_LINKS, "www.inftel.com");
        values.put(ProjectTable.KEY_LABELS, "");
        values.put(ProjectTable.KEY_LICENSE, "GPL");

        cr.insert(ProjectContentProvider.CONTENT_URI, values);

        values.put(ProjectTable.KEY_NAME, "Proyecto 2");
        values.put(ProjectTable.KEY_SUMMARY, "Centro Medico");
        values.put(ProjectTable.KEY_DESCRIPTION, "Gestion del centro ambulatorio");
        values.put(ProjectTable.KEY_OPENED, "21/3/2012");
        values.put(ProjectTable.KEY_STARTED, "12/3/2012");
        values.put(ProjectTable.KEY_CLOSE, "12/3/2012");
        values.put(ProjectTable.KEY_COMPANY, "Inftel");
        values.put(ProjectTable.KEY_LINKS, "www.inftel.com");
        values.put(ProjectTable.KEY_LABELS, "");
        values.put(ProjectTable.KEY_LICENSE, "GPL");

        cr.insert(ProjectContentProvider.CONTENT_URI, values);

        values.put(ProjectTable.KEY_NAME, "Proyecto 3");
        values.put(ProjectTable.KEY_SUMMARY, "Centro de Reparaciones");
        values.put(ProjectTable.KEY_DESCRIPTION, "Control de inventario");
        values.put(ProjectTable.KEY_OPENED, "23/3/2012");
        values.put(ProjectTable.KEY_STARTED, "12/3/2012");
        values.put(ProjectTable.KEY_CLOSE, "12/3/2012");
        values.put(ProjectTable.KEY_COMPANY, "Inftel");
        values.put(ProjectTable.KEY_LINKS, "www.inftel.com");
        values.put(ProjectTable.KEY_LABELS, "");
        values.put(ProjectTable.KEY_LICENSE, "GPL");

        cr.insert(ProjectContentProvider.CONTENT_URI, values);

        values.put(ProjectTable.KEY_NAME, "Proyecto 4");
        values.put(ProjectTable.KEY_SUMMARY, "Central Eolica");
        values.put(ProjectTable.KEY_DESCRIPTION, "Gestion de recursos");
        values.put(ProjectTable.KEY_OPENED, "22/3/2012");
        values.put(ProjectTable.KEY_STARTED, "12/3/2012");
        values.put(ProjectTable.KEY_CLOSE, "12/3/2012");
        values.put(ProjectTable.KEY_COMPANY, "Inftel");
        values.put(ProjectTable.KEY_LINKS, "www.inftel.com");
        values.put(ProjectTable.KEY_LABELS, "");
        values.put(ProjectTable.KEY_LICENSE, "GPL");

        cr.insert(ProjectContentProvider.CONTENT_URI, values);
        Log.d(getClass().getSimpleName(), "Fin de insercion de datos");
    }

}

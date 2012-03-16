
package org.inftel.ssa.mobile.ui.phone;

import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;
import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.ProjectListFragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

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

        values.put(ProjectTable.KEY_NAME, "Proyecto 1");
        values.put(ProjectTable.KEY_SUMMARY, "Pasos");
        values.put(ProjectTable.KEY_DESCRIPTION, "Gestion de alarmas para ancianos");
        values.put(ProjectTable.KEY_OPENED, "12/3/2012");
        values.put(ProjectTable.KEY_STARTED, "12/3/2012");
        values.put(ProjectTable.KEY_CLOSE, "12/3/2012");
        values.put(ProjectTable.KEY_COMPANY, "Inftel");
        values.put(ProjectTable.KEY_LINKS, "www.inftel.com");
        values.put(ProjectTable.KEY_LABELS, "");
        values.put(ProjectTable.KEY_LICENSE, "GPL");

        cr.insert(ProjectContentProvider.CONTENT_URI, values);

        values.put(ProjectTable.KEY_NAME, "Proyecto 2");
        values.put(ProjectTable.KEY_SUMMARY, "Centro Medico");
        values.put(ProjectTable.KEY_DESCRIPTION, "Gestion del centro ambulatorio");
        values.put(ProjectTable.KEY_OPENED, "12/3/2012");
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
        values.put(ProjectTable.KEY_OPENED, "12/3/2012");
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
        values.put(ProjectTable.KEY_OPENED, "12/3/2012");
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

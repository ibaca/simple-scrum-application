
package org.inftel.ssa.mobile.ui.phone;

import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskTable;
import org.inftel.ssa.mobile.ui.BaseSinglePaneActivity;
import org.inftel.ssa.mobile.ui.fragments.TaskListFragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class TaskListActivity extends BaseSinglePaneActivity {
    @Override
    protected Fragment onCreatePane() {
        insertTaskDataTable();
        return new TaskListFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupSubActivity();
    }

    private void insertTaskDataTable() {

        Log.d(getClass().getSimpleName(), "Insertando datos prueba");
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        cr.delete(ProjectContentProvider.CONTENT_URI,
                TaskTable.COLUMN_USER + " = -100", null);
        cr.delete(ProjectContentProvider.CONTENT_URI,
                TaskTable.COLUMN_USER + " = -101", null);
        cr.delete(ProjectContentProvider.CONTENT_URI,
                TaskTable.COLUMN_USER + " = -102", null);

        values.put(TaskTable.COLUMN_USER, "-100");
        values.put(TaskTable.COLUMN_PROJECT, "-200");
        values.put(TaskTable.COLUMN_SPRINT, "-400");
        values.put(TaskTable.COLUMN_SUMMARY, "Definir modelo de datos");
        values.put(TaskTable.COLUMN_DESCRIPTION, "Descripcion sencilla.");
        values.put(TaskTable.COLUMN_ESTIMATED, "4");
        values.put(TaskTable.COLUMN_BURNED, "4");
        values.put(TaskTable.COLUMN_REMAINING, "0");
        values.put(TaskTable.COLUMN_PRIORITY, "2");
        values.put(TaskTable.COLUMN_BEGINDATE, "2011-12-01");
        values.put(TaskTable.COLUMN_ENDDATE, "2011-12-01");
        values.put(TaskTable.COLUMN_STATUS, "2");
        values.put(TaskTable.COLUMN_CREATED, "2012-03-07");
        values.put(TaskTable.COLUMN_COMMENTS, "comentario");

        cr.insert(TaskContentProvider.CONTENT_URI, values);

        ContentValues values2 = new ContentValues();
        values2.put(TaskTable.COLUMN_USER, "-101");
        values2.put(TaskTable.COLUMN_PROJECT, "-200");
        values2.put(TaskTable.COLUMN_SPRINT, "-400");
        values2.put(TaskTable.COLUMN_SUMMARY, "Definir casos de uso iniciales");
        values2.put(TaskTable.COLUMN_DESCRIPTION, "Descripcion sencilla.");
        values2.put(TaskTable.COLUMN_ESTIMATED, "2");
        values2.put(TaskTable.COLUMN_BURNED, "3");
        values2.put(TaskTable.COLUMN_REMAINING, "0");
        values2.put(TaskTable.COLUMN_PRIORITY, "10");
        values2.put(TaskTable.COLUMN_BEGINDATE, "2011-12-01");
        values2.put(TaskTable.COLUMN_ENDDATE, "2011-12-01");
        values2.put(TaskTable.COLUMN_STATUS, "2");
        values2.put(TaskTable.COLUMN_CREATED, "2012-03-07");
        values2.put(TaskTable.COLUMN_COMMENTS, "comentario");

        cr.insert(TaskContentProvider.CONTENT_URI, values2);

        ContentValues values3 = new ContentValues();
        values3.put(TaskTable.COLUMN_USER, "-102");
        values3.put(TaskTable.COLUMN_PROJECT, "-200");
        values3.put(TaskTable.COLUMN_SPRINT, "-400");
        values3.put(TaskTable.COLUMN_SUMMARY, "Creacion de estructura del proyecto");
        values3.put(TaskTable.COLUMN_DESCRIPTION, "Descripcion sencilla.");
        values3.put(TaskTable.COLUMN_ESTIMATED, "2");
        values3.put(TaskTable.COLUMN_BURNED, "2");
        values3.put(TaskTable.COLUMN_REMAINING, "0");
        values3.put(TaskTable.COLUMN_PRIORITY, "10");
        values3.put(TaskTable.COLUMN_BEGINDATE, "2011-12-01");
        values3.put(TaskTable.COLUMN_ENDDATE, "2011-12-01");
        values3.put(TaskTable.COLUMN_STATUS, "2");
        values3.put(TaskTable.COLUMN_CREATED, "2012-03-07");
        values3.put(TaskTable.COLUMN_COMMENTS, "comentario");

        cr.insert(TaskContentProvider.CONTENT_URI, values3);
    }
}

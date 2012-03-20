
package org.inftel.ssa.mobile;

import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;
import org.inftel.ssa.mobile.contentproviders.SprintContentProvider;
import org.inftel.ssa.mobile.contentproviders.SprintTable;
import org.inftel.ssa.mobile.contentproviders.TaskContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskTable;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;

public class SsaApplication extends Application {

    private static final String TAG = "SsaApplication";

    @Override
    public final void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate application.");

        // Algunos datos para hacer pruebas
        ContentResolver cr = getContentResolver();

        if (cr.query(SprintContentProvider.CONTENT_URI, new String[] {
                SprintTable.KEY_SUMMARY
        }, null, null, null).getCount() == 0) {

            ContentValues values = new ContentValues();

            // Sprints
            values.put(SprintTable.KEY_PROJECT_FK, "1");

            values.put(SprintTable.KEY_SUMMARY, "Primer sprint");
            cr.insert(SprintContentProvider.CONTENT_URI, values);
            values.put(SprintTable.KEY_SUMMARY, "Segundo sprint");
            cr.insert(SprintContentProvider.CONTENT_URI, values);
            values.put(SprintTable.KEY_SUMMARY, "Tercer sprint");
            cr.insert(SprintContentProvider.CONTENT_URI, values);

            // Projects
            values.clear();
            values.put(ProjectTable.KEY_NAME, "Manhatan");
            values.put(ProjectTable.KEY_SUMMARY, "Primer projecto");
            cr.insert(ProjectContentProvider.CONTENT_URI, values);

            values.clear();
            values.put(ProjectTable.KEY_NAME, "Increible");
            values.put(ProjectTable.KEY_SUMMARY, "Segundo projecto");
            cr.insert(ProjectContentProvider.CONTENT_URI, values);

            values.clear();
            values.put(ProjectTable.KEY_NAME, "Desastroso");
            values.put(ProjectTable.KEY_SUMMARY, "Tercer projecto");
            cr.insert(ProjectContentProvider.CONTENT_URI, values);

            // Tasks
            values.clear();
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

            values.clear();
            values.put(TaskTable.COLUMN_USER, "-101");
            values.put(TaskTable.COLUMN_PROJECT, "-200");
            values.put(TaskTable.COLUMN_SPRINT, "-400");
            values.put(TaskTable.COLUMN_SUMMARY, "Definir casos de uso iniciales");
            values.put(TaskTable.COLUMN_DESCRIPTION, "Descripcion sencilla.");
            values.put(TaskTable.COLUMN_ESTIMATED, "2");
            values.put(TaskTable.COLUMN_BURNED, "3");
            values.put(TaskTable.COLUMN_REMAINING, "0");
            values.put(TaskTable.COLUMN_PRIORITY, "10");
            values.put(TaskTable.COLUMN_BEGINDATE, "2011-12-01");
            values.put(TaskTable.COLUMN_ENDDATE, "2011-12-01");
            values.put(TaskTable.COLUMN_STATUS, "2");
            values.put(TaskTable.COLUMN_CREATED, "2012-03-07");
            values.put(TaskTable.COLUMN_COMMENTS, "comentario");
            cr.insert(TaskContentProvider.CONTENT_URI, values);

            values.clear();
            values.put(TaskTable.COLUMN_USER, "-102");
            values.put(TaskTable.COLUMN_PROJECT, "-200");
            values.put(TaskTable.COLUMN_SPRINT, "-400");
            values.put(TaskTable.COLUMN_SUMMARY, "Creacion de estructura del proyecto");
            values.put(TaskTable.COLUMN_DESCRIPTION, "Descripcion sencilla.");
            values.put(TaskTable.COLUMN_ESTIMATED, "2");
            values.put(TaskTable.COLUMN_BURNED, "2");
            values.put(TaskTable.COLUMN_REMAINING, "0");
            values.put(TaskTable.COLUMN_PRIORITY, "10");
            values.put(TaskTable.COLUMN_BEGINDATE, "2011-12-01");
            values.put(TaskTable.COLUMN_ENDDATE, "2011-12-01");
            values.put(TaskTable.COLUMN_STATUS, "2");
            values.put(TaskTable.COLUMN_CREATED, "2012-03-07");
            values.put(TaskTable.COLUMN_COMMENTS, "comentario");
            cr.insert(TaskContentProvider.CONTENT_URI, values);
        }

    }

}

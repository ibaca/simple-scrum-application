
package org.inftel.ssa.mobile.ui;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;
import org.inftel.ssa.mobile.ui.fragments.ProjectDetailFragment;
import org.inftel.ssa.mobile.ui.fragments.ProjectListFragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ProjectActivity extends FragmentActivity {

    private ProjectDetailFragment projectDetailFragment;
    private static ProjectListFragment projectListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        insertProjectsDataTable();

        setContentView(R.layout.ssa_project_container);

        projectListFragment = (ProjectListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.projects_list_fragment);

    }

    static public final class Information {

        public static final String[] TITLES =
        {
                "Titulo 1",
                "Titulo 2",
                "Titulo 3",
                "Titulo 4",
                "Titulo 5",
                "Titulo 6"
        };

        public static final String[] TEXT =
        {
                "Mostrando Titulo 1",
                "Mostrando Titulo 2",
                "Mostrando Titulo 3",
                "Mostrando Titulo 4",
                "Mostrando Titulo 5",
                "Mostrando Titulo 6"

        };
    }

    public void selectDetail(Uri projectUri) {
        projectDetailFragment = ProjectDetailFragment.newInstance(projectUri);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (findViewById(R.id.projects_container) != null) {
            ft.addToBackStack(null);
            ft.hide(projectListFragment);
            ft.replace(R.id.projects_container, projectDetailFragment);
            ft.show(projectDetailFragment);
            ft.commit();
        }
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
        values.put(ProjectTable.KEY_SUMMARY, "Proyecto 1");
        values.put(ProjectTable.KEY_DESCRIPTION, "Proyecto 1");
        values.put(ProjectTable.KEY_OPENED, "12-3-2012");
        values.put(ProjectTable.KEY_STARTED, "12-3-2012");
        values.put(ProjectTable.KEY_CLOSE, "12-3-2012");
        values.put(ProjectTable.KEY_COMPANY, "Inftel");
        values.put(ProjectTable.KEY_LINKS, "www.inftel.com");
        values.put(ProjectTable.KEY_LABELS, "");
        values.put(ProjectTable.KEY_LICENSE, "GPL");

        cr.insert(ProjectContentProvider.CONTENT_URI, values);

        values.put(ProjectTable.KEY_NAME, "Proyecto 2");
        values.put(ProjectTable.KEY_SUMMARY, "Proyecto 2");
        values.put(ProjectTable.KEY_DESCRIPTION, "Proyecto 2");
        values.put(ProjectTable.KEY_OPENED, "12-3-2012");
        values.put(ProjectTable.KEY_STARTED, "12-3-2012");
        values.put(ProjectTable.KEY_CLOSE, "12-3-2012");
        values.put(ProjectTable.KEY_COMPANY, "Inftel");
        values.put(ProjectTable.KEY_LINKS, "www.inftel.com");
        values.put(ProjectTable.KEY_LABELS, "");
        values.put(ProjectTable.KEY_LICENSE, "GPL");

        cr.insert(ProjectContentProvider.CONTENT_URI, values);

        values.put(ProjectTable.KEY_NAME, "Proyecto 3");
        values.put(ProjectTable.KEY_SUMMARY, "Proyecto 3");
        values.put(ProjectTable.KEY_DESCRIPTION, "Proyecto 3");
        values.put(ProjectTable.KEY_OPENED, "12-3-2012");
        values.put(ProjectTable.KEY_STARTED, "12-3-2012");
        values.put(ProjectTable.KEY_CLOSE, "12-3-2012");
        values.put(ProjectTable.KEY_COMPANY, "Inftel");
        values.put(ProjectTable.KEY_LINKS, "www.inftel.com");
        values.put(ProjectTable.KEY_LABELS, "");
        values.put(ProjectTable.KEY_LICENSE, "GPL");

        cr.insert(ProjectContentProvider.CONTENT_URI, values);

        values.put(ProjectTable.KEY_NAME, "Proyecto 4");
        values.put(ProjectTable.KEY_SUMMARY, "Proyecto 4");
        values.put(ProjectTable.KEY_DESCRIPTION, "Proyecto 4");
        values.put(ProjectTable.KEY_OPENED, "12-3-2012");
        values.put(ProjectTable.KEY_STARTED, "12-3-2012");
        values.put(ProjectTable.KEY_CLOSE, "12-3-2012");
        values.put(ProjectTable.KEY_COMPANY, "Inftel");
        values.put(ProjectTable.KEY_LINKS, "www.inftel.com");
        values.put(ProjectTable.KEY_LABELS, "");
        values.put(ProjectTable.KEY_LICENSE, "GPL");

        cr.insert(ProjectContentProvider.CONTENT_URI, values);
        Log.d(getClass().getSimpleName(), "Fin de insercion de datos");
    }

}

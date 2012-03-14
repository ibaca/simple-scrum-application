
package org.inftel.ssa.mobile.ui;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.ui.fragments.ProjectDetailFragment;
import org.inftel.ssa.mobile.ui.fragments.ProjectListFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ProjectActivity extends FragmentActivity {

    private ProjectDetailFragment projectDetailFragment;
    private static ProjectListFragment projectListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void selectDetail(int index) {
        projectDetailFragment = ProjectDetailFragment.newInstance(index);
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

}

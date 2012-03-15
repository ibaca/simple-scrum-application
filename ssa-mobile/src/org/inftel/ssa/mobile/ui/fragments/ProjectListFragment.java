
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;
import org.inftel.ssa.mobile.ui.ProjectActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ProjectListFragment extends ListFragment {

    int textoActual = 0;
    ProjectActivity projectActivity;
    private Cursor cursor;

    /**
     * The columns needed by the cursor adapter
     */
    private static final String[] PROJECTION = new String[] {
            ProjectTable.KEY_ID, // 0
            ProjectTable.KEY_NAME, // 1
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        projectActivity = (ProjectActivity) getActivity();

        // If no data was given in the intent (because we were started
        // as a MAIN activity), then use our default content provider.
        Intent intent = projectActivity.getIntent();
        if (intent.getData() == null) {
            intent.setData(ProjectContentProvider.CONTENT_URI);
        }

        // Perform a managed query. The Activity will handle closing and
        // requerying the cursor when needed.
        cursor = projectActivity.managedQuery(projectActivity.getIntent().getData(),
                PROJECTION, null, null,
                null);

        // Used to map tasks entries from the database to views
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.ssa_project_list, cursor,
                new String[] {
                        ProjectTable.KEY_NAME
                }, new int[] {
                        android.R.id.text1
                });
        setListAdapter(adapter);

        // setListAdapter(new ArrayAdapter<String>(getActivity(),
        // R.layout.ssa_project_list, android.R.id.text1,
        // Information.TITLES));
        //
        // if (savedInstanceState != null) {
        // textoActual = savedInstanceState.getInt("currentChoice", 0);
        // }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentChoice", textoActual);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Uri projectUri = ContentUris.withAppendedId(getActivity().getIntent().getData(), id);
        ((ProjectActivity) getActivity()).selectDetail(projectUri);
    }

}

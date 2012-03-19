
package org.inftel.ssa.mobile.ui.fragments;

import static android.content.Intent.ACTION_VIEW;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ProjectEditFragment extends Fragment implements LoaderCallbacks<Cursor> {
    private Activity mActivity;
    private Uri mContentUri;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        if (mContentUri != null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            // New item (set default values)
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ssa_project_edit, container, false);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get("_uri") != null) {
            mContentUri = (Uri) arguments.get("_uri");
        }

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_project_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                Log.d(getClass().getSimpleName(), "Salvando nuevo proyecto");

                EditText title = (EditText) getView().findViewById(R.id.project_edit_title);
                EditText summary = (EditText) getView().findViewById(R.id.project_edit_summary);

                String txtTitle = title.getText().toString();
                String txtSummary = summary.getText().toString();

                Log.d(getClass().getSimpleName(), "title: " + txtTitle);
                Log.d(getClass().getSimpleName(), "summary: " + txtSummary);

                ContentResolver cr = mActivity.getContentResolver();
                ContentValues values = new ContentValues();

                values.put(ProjectTable.KEY_NAME, txtTitle);
                values.put(ProjectTable.KEY_SUMMARY, txtSummary);
                cr.insert(ProjectContentProvider.CONTENT_URI, values);

                final Intent intent = new Intent(ACTION_VIEW,
                        ProjectContentProvider.CONTENT_URI);
                startActivity(intent);
                return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // TODO Auto-generated method stub
    }

}

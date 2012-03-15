
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProjectDetailFragment extends Fragment implements LoaderCallbacks<Cursor> {

    protected final static String TAG = "ProjectDetailFragment";
    protected Handler mHandler = new Handler();
    protected Activity mActivity;
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

        View view = inflater.inflate(R.layout.ssa_project_details, container, false);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get("_uri") != null) {
            mContentUri = (Uri) arguments.get("_uri");
        }

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                ProjectTable.KEY_ID, ProjectTable.KEY_NAME,
                ProjectTable.KEY_SUMMARY, ProjectTable.KEY_DESCRIPTION

        };
        return new CursorLoader(mActivity, mContentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            final String name = data.getString(data.getColumnIndex(ProjectTable.KEY_NAME));
            final String summary = data.getString(data.getColumnIndex(ProjectTable.KEY_SUMMARY));
            final String description = data.getString(data
                    .getColumnIndex(ProjectTable.KEY_DESCRIPTION));
            // Update UI
            mHandler.post(new Runnable() {
                public void run() {
                    ((TextView) getView().findViewById(R.id.detail_title)).setText(name);
                    ((TextView) getView().findViewById(R.id.detail_summary)).setText("summary "
                            + summary);
                    ((TextView) getView().findViewById(R.id.detail_description))
                            .setText("description "
                                    + description);
                }
            });
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mHandler.post(new Runnable() {
            public void run() {
                // ((TextView)
                // getView().findViewById(R.id.sprint_title)).setText("");
                // ((TextView)
                // getView().findViewById(R.id.sprint_subtitle)).setText("");
            }
        });

    }

    // public void fillProjectDetailsFragment(View view, Uri projectUri) {
    //
    // // The text view for task data, identified by its ID in the XML file.
    // mTxtName = (TextView) view.findViewById(R.id.lblProjectName);
    // mTxtSummary = (TextView) view.findViewById(R.id.lblProjectSummary);
    // mTxtDescription = (TextView)
    // view.findViewById(R.id.lblProjectDescription);
    //
    // // Get the note!
    // mCursor = getActivity().managedQuery(projectUri, PROJECTION, null, null,
    // null);
    // if (mCursor != null) {
    // // Requery in case something changed while paused (such as the
    // // title)
    //
    // mCursor.requery();
    //
    // int colName = mCursor.getColumnIndex(ProjectTable.KEY_NAME);
    // int colSummary = mCursor.getColumnIndex(ProjectTable.KEY_SUMMARY);
    // int colDescription =
    // mCursor.getColumnIndex(ProjectTable.KEY_DESCRIPTION);
    // String txt = "";
    // if (mCursor.moveToFirst()) {
    // txt = mCursor.getString(colName);
    // mTxtName.setText(txt);
    // txt = mCursor.getString(colSummary);
    // mTxtSummary.setText(txt);
    // txt = mCursor.getString(colDescription);
    // mTxtDescription.setText(txt);
    // }
    // mCursor.close();
    //
    // }
    // }
}

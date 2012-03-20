
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.TaskContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskTable;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TaskDetailFragment extends Fragment implements LoaderCallbacks<Cursor> {

    protected final static String TAG = "TaskDetailFragment";

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

        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get("_uri") != null) {
            mContentUri = (Uri) arguments.get("_uri");
        }

        setHasOptionsMenu(true);

        /*
         * // Handle sprints click
         * view.findViewById(R.id.project_btn_sprints).setOnClickListener(new
         * View.OnClickListener() {
         * @Override public void onClick(View v) { Uri sprintUri =
         * SprintContentProvider.CONTENT_URI.buildUpon()
         * .appendQueryParameter("task_id", mTaskId).build(); startActivity(new
         * Intent(Intent.ACTION_VIEW, sprintUri)); } }); TabHost tabHost =
         * (TabHost) view.findViewById(android.R.id.tabhost); tabHost.setup();
         * setupDescriptionTab(view); setupLinksTab(view);
         * setupInformationTab(view);
         */
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * {@inheritDoc} Query the {@link PlaceDetailsContentProvider} for the
     * Phone, Address, Rating, Reference, and Url of the selected venue. TODO
     * Expand the projection to include any other details you are recording in
     * the Place Detail Content Provider.
     */
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
         * String[] projection = new String[] { SprintTable.KEY_SUMMARY, };
         */
        String[] projection = new String[] {
                TaskTable.COLUMN_ID, // 0
                TaskTable.COLUMN_SUMMARY, // 1
                TaskTable.COLUMN_DESCRIPTION, // 2
                TaskTable.COLUMN_ESTIMATED, // 3
                TaskTable.COLUMN_PRIORITY, // 4
                TaskTable.COLUMN_SPRINT, // 5
                TaskTable.COLUMN_USER, // 6
                TaskTable.COLUMN_STATUS, // 7
                TaskTable.COLUMN_BEGINDATE, // 8
                TaskTable.COLUMN_ENDDATE, // 9
                TaskTable.COLUMN_BURNED, // 10
                TaskTable.COLUMN_REMAINING, // 11
                TaskTable.COLUMN_COMMENTS, // 12
        };

        return new CursorLoader(mActivity, mContentUri, projection, null, null, null);
    }

    /**
     * {@inheritDoc} When the Loader has completed, schedule an update of the
     * Fragment UI on the main application thread.
     */
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {

            // final String sumamry =
            // data.getString(data.getColumnIndex(SprintTable.KEY_SUMMARY));

            final String mTxtSummary = data
                    .getString(data.getColumnIndex(TaskTable.COLUMN_SUMMARY));
            final String mTxtDescription = data.getString(data
                    .getColumnIndex(TaskTable.COLUMN_DESCRIPTION));
            final String mTxtEstimated = data.getString(data
                    .getColumnIndex(TaskTable.COLUMN_ESTIMATED));
            final String mTxtPriority = data.getString(data
                    .getColumnIndex(TaskTable.COLUMN_PRIORITY));
            final String mTxtSprint = data.getString(data.getColumnIndex(TaskTable.COLUMN_SPRINT));
            final String mTxtStatus = data.getString(data.getColumnIndex(TaskTable.COLUMN_STATUS));
            final String mTxtBeginDate = data.getString(data
                    .getColumnIndex(TaskTable.COLUMN_BEGINDATE));
            final String mTxtEndDate = data
                    .getString(data.getColumnIndex(TaskTable.COLUMN_ENDDATE));
            final String mTxtBurned = data.getString(data.getColumnIndex(TaskTable.COLUMN_BURNED));
            final String mTxtRemaining = data.getString(data
                    .getColumnIndex(TaskTable.COLUMN_REMAINING));
            final String mTxtComments = data.getString(data
                    .getColumnIndex(TaskTable.COLUMN_COMMENTS));

            // Update UI
            mHandler.post(new Runnable() {
                public void run() {
                    ((TextView) getView().findViewById(R.id.lblSummary)).setText(mTxtSummary);
                    ((TextView) getView().findViewById(R.id.lblDescription))
                            .setText(mTxtDescription);
                    ((TextView) getView().findViewById(R.id.lblEstimated)).setText(mTxtEstimated);
                    ((TextView) getView().findViewById(R.id.lblPriority)).setText(mTxtPriority);
                    ((TextView) getView().findViewById(R.id.lblSprint)).setText(mTxtSprint);
                    ((TextView) getView().findViewById(R.id.lblStatus)).setText(mTxtStatus);
                    ((TextView) getView().findViewById(R.id.lblBeginDate)).setText(mTxtBeginDate);
                    ((TextView) getView().findViewById(R.id.lblEndDate)).setText(mTxtEndDate);
                    ((TextView) getView().findViewById(R.id.lblBurned)).setText(mTxtBurned);
                    ((TextView) getView().findViewById(R.id.lblRemaining)).setText(mTxtRemaining);
                    ((TextView) getView().findViewById(R.id.lblComments)).setText(mTxtComments);
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ssa_task_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                startActivity(new Intent(Intent.ACTION_VIEW, TaskContentProvider.CONTENT_URI));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


package org.inftel.ssa.mobile.ui.fragments;

import static android.content.Intent.ACTION_VIEW;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.TaskContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskTable;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class TaskEditFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final String TAG = "TaskEditFragment";

    private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;

    protected Handler mHandler = new Handler();
    protected Activity mActivity;
    private Uri mContentUri;
    private Intent mIntent;
    private String mAction;
    private int mState;
    private EditText mTxtSummary;
    private EditText mTxtDescription;
    private EditText mTxtEstimated;
    private EditText mTxtPriority;
    private EditText mTxtSprint;
    private EditText mTxtStatus;
    private EditText mTxtBeginDate;
    private EditText mTxtEndDate;
    private EditText mTxtBurned;
    private EditText mTxtRemaining;
    private EditText mTxtComments;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mIntent = mActivity.getIntent();
        mAction = mIntent.getAction();

        if (Intent.ACTION_EDIT.equals(mAction)) {
            mState = STATE_EDIT;
            getLoaderManager().initLoader(0, null, this);
        } else if (Intent.ACTION_INSERT.equals(mAction)) {
            mState = STATE_INSERT;
            // New item (set default values)
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);

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

    /**
     * {@inheritDoc} Query the {@link PlaceDetailsContentProvider} for the
     * Phone, Address, Rating, Reference, and Url of the selected venue. TODO
     * Expand the projection to include any other details you are recording in
     * the Place Detail Content Provider.
     */
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
            final String sumamry = data.getString(data.getColumnIndex(TaskTable.COLUMN_SUMMARY));
            final String description = data.getString(data
                    .getColumnIndex(TaskTable.COLUMN_DESCRIPTION));
            // Update UI
            mHandler.post(new Runnable() {
                public void run() {
                    ((TextView) getView().findViewById(R.id.txtSummary)).setText(sumamry);
                    ((TextView) getView().findViewById(R.id.txtDescription)).setText(description);
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
        inflater.inflate(R.menu.ssa_task_edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveTask();
                final Intent intent = new Intent(ACTION_VIEW,
                        TaskContentProvider.CONTENT_URI);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void saveTask() {

        Log.d(getClass().getSimpleName(), "Save Task");

        mTxtSummary = (EditText) getView().findViewById(R.id.txtSummary);
        mTxtDescription = (EditText) getView().findViewById(R.id.txtDescription);
        mTxtEstimated = (EditText) getView().findViewById(R.id.txtEstimated);
        mTxtPriority = (EditText) getView().findViewById(R.id.txtPriority);
        mTxtSprint = (EditText) getView().findViewById(R.id.txtSprint);
        mTxtStatus = (EditText) getView().findViewById(R.id.txtStatus);
        mTxtBeginDate = (EditText) getView().findViewById(R.id.txtBeginDate);
        mTxtEndDate = (EditText) getView().findViewById(R.id.txtEndDate);
        mTxtBurned = (EditText) getView().findViewById(R.id.txtBurned);
        mTxtRemaining = (EditText) getView().findViewById(R.id.txtRemaining);
        mTxtComments = (EditText) getView().findViewById(R.id.txtComments);

        ContentResolver cr = mActivity.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(TaskTable.COLUMN_SUMMARY, mTxtSummary.getText().toString());
        values.put(TaskTable.COLUMN_DESCRIPTION, mTxtDescription.getText().toString());
        values.put(TaskTable.COLUMN_ESTIMATED, mTxtEstimated.getText().toString());
        values.put(TaskTable.COLUMN_PRIORITY, mTxtPriority.getText().toString());
        values.put(TaskTable.COLUMN_SPRINT, mTxtSprint.getText().toString());
        values.put(TaskTable.COLUMN_STATUS, mTxtStatus.getText().toString());
        values.put(TaskTable.COLUMN_BEGINDATE, mTxtBeginDate.getText().toString());
        values.put(TaskTable.COLUMN_ENDDATE, mTxtEndDate.getText().toString());
        values.put(TaskTable.COLUMN_BURNED, mTxtBurned.getText().toString());
        values.put(TaskTable.COLUMN_REMAINING, mTxtRemaining.getText().toString());
        values.put(TaskTable.COLUMN_COMMENTS, mTxtComments.getText().toString());

        try {
            if (mState == STATE_INSERT) {
                values.put(TaskTable.COLUMN_CREATED, Long.toString(System.currentTimeMillis()));
                cr.insert(TaskContentProvider.CONTENT_URI, values);
            } else {
                cr.update(mContentUri, values, null, null);
            }
        } catch (NullPointerException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }

    }

}

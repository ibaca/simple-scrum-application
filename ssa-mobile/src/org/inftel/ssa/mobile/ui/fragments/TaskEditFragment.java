
package org.inftel.ssa.mobile.ui.fragments;

import static android.content.Intent.ACTION_VIEW;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;

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
                Tasks._ID, // 0
                Tasks.TASK_SUMMARY, // 1
                Tasks.TASK_DESCRIPTION, // 2
                Tasks.TASK_ESTIMATED, // 3
                Tasks.TASK_PRIORITY, // 4
                Tasks.TASK_SPRINT_ID, // 5
                Tasks.TASK_USER_ID, // 6
                Tasks.TASK_STATUS, // 7
                Tasks.TASK_BEGINDATE, // 8
                Tasks.TASK_ENDDATE, // 9
                Tasks.TASK_BURNED, // 10
                Tasks.TASK_REMAINING, // 11
                Tasks.TASK_COMMENTS, // 12
        };

        return new CursorLoader(mActivity, mContentUri, projection, null, null, null);
    }

    /**
     * {@inheritDoc} When the Loader has completed, schedule an update of the
     * Fragment UI on the main application thread.
     */
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            final String sumamry = data.getString(data.getColumnIndex(Tasks.TASK_SUMMARY));
            final String description = data.getString(data
                    .getColumnIndex(Tasks.TASK_DESCRIPTION));
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
                        Tasks.CONTENT_URI);
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

        values.put(Tasks.TASK_SUMMARY, mTxtSummary.getText().toString());
        values.put(Tasks.TASK_DESCRIPTION, mTxtDescription.getText().toString());
        values.put(Tasks.TASK_ESTIMATED, mTxtEstimated.getText().toString());
        values.put(Tasks.TASK_PRIORITY, mTxtPriority.getText().toString());
        values.put(Tasks.TASK_SPRINT_ID, mTxtSprint.getText().toString());
        values.put(Tasks.TASK_STATUS, mTxtStatus.getText().toString());
        values.put(Tasks.TASK_BEGINDATE, mTxtBeginDate.getText().toString());
        values.put(Tasks.TASK_ENDDATE, mTxtEndDate.getText().toString());
        values.put(Tasks.TASK_BURNED, mTxtBurned.getText().toString());
        values.put(Tasks.TASK_REMAINING, mTxtRemaining.getText().toString());
        values.put(Tasks.TASK_COMMENTS, mTxtComments.getText().toString());

        try {
            if (mState == STATE_INSERT) {
                values.put(Tasks.TASK_CREATED, Long.toString(System.currentTimeMillis()));
                cr.insert(Tasks.CONTENT_URI, values);
            } else {
                cr.update(mContentUri, values, null, null);
            }
        } catch (NullPointerException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }

    }

}

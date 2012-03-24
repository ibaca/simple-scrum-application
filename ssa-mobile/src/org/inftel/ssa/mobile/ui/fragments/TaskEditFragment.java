
package org.inftel.ssa.mobile.ui.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;
import org.inftel.ssa.mobile.provider.SsaContract.Users;
import org.inftel.ssa.mobile.ui.phone.TaskListActivity;

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

    private static final int PICK_SPRINT_REQUEST = 1;
    private static final int PICK_USER_REQUEST = 2;
    private static final int PICK_PROJECT_REQUEST = 3;

    protected Handler mHandler = new Handler();
    protected Activity mActivity;
    private Uri mContentUri;
    private Uri mUserUri;
    private Intent mIntent;
    private String mAction;
    private int mState;
    private EditText mTxtSummary;
    private EditText mTxtDescription;
    private EditText mTxtEstimated;
    private EditText mTxtPriority;
    private EditText mTxtSprint;
    private EditText mTxtUser;
    private EditText mTxtProject;
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ssa_task_edit, container, false);

        mTxtSummary = (EditText) view.findViewById(R.id.txtSummary);
        mTxtDescription = (EditText) view.findViewById(R.id.txtDescription);
        mTxtEstimated = (EditText) view.findViewById(R.id.txtEstimated);
        mTxtPriority = (EditText) view.findViewById(R.id.txtPriority);
        mTxtSprint = (EditText) view.findViewById(R.id.txtSprint);
        mTxtUser = (EditText) view.findViewById(R.id.txtUser);
        mTxtStatus = (EditText) view.findViewById(R.id.txtStatus);
        mTxtBeginDate = (EditText) view.findViewById(R.id.txtBeginDate);
        mTxtEndDate = (EditText) view.findViewById(R.id.txtEndDate);

        mTxtBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment.newInstance(v.getContext(), mTxtBeginDate).show(
                        getActivity().getSupportFragmentManager(),
                        "Date Picker Dialog");
            }

        });

        mTxtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment.newInstance(v.getContext(), mTxtEndDate).show(
                        getActivity().getSupportFragmentManager(),
                        "Date Picker Dialog");
            }

        });

        mTxtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, Users.CONTENT_URI);
                startActivityForResult(intent, PICK_USER_REQUEST);
            }

        });

        mTxtBurned = (EditText) view.findViewById(R.id.txtBurned);
        mTxtRemaining = (EditText) view.findViewById(R.id.txtRemaining);
        mTxtComments = (EditText) view.findViewById(R.id.txtComments);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get("_uri") != null) {
            mContentUri = (Uri) arguments.get("_uri");
        }

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
            final String summary = data.getString(data.getColumnIndex(Tasks.TASK_SUMMARY));
            final String description = data.getString(data.getColumnIndex(Tasks.TASK_DESCRIPTION));
            final String estimated = data.getString(data.getColumnIndex(Tasks.TASK_ESTIMATED));
            final String priority = data.getString(data.getColumnIndex(Tasks.TASK_PRIORITY));
            final String sprint = data.getString(data.getColumnIndex(Tasks.TASK_SPRINT_ID));
            final String status = data.getString(data.getColumnIndex(Tasks.TASK_STATUS));
            final String beginDate = data.getString(data.getColumnIndex(Tasks.TASK_BEGINDATE));
            final String user = data.getString(data.getColumnIndex(Tasks.TASK_USER_ID));
            final String endDate = data.getString(data.getColumnIndex(Tasks.TASK_ENDDATE));
            final String burned = data.getString(data.getColumnIndex(Tasks.TASK_BURNED));
            final String remaining = data.getString(data.getColumnIndex(Tasks.TASK_REMAINING));
            final String comments = data.getString(data.getColumnIndex(Tasks.TASK_COMMENTS));

            final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

            // Update UI
            mHandler.post(new Runnable() {
                public void run() {
                    ((TextView) getView().findViewById(R.id.txtSummary)).setText(summary);
                    ((TextView) getView().findViewById(R.id.txtDescription)).setText(description);
                    ((TextView) getView().findViewById(R.id.txtEstimated)).setText(estimated);
                    ((TextView) getView().findViewById(R.id.txtPriority)).setText(priority);
                    ((TextView) getView().findViewById(R.id.txtSprint)).setText(sprint);
                    ((TextView) getView().findViewById(R.id.txtStatus)).setText(status);
                    ((TextView) getView().findViewById(R.id.txtBeginDate)).setText(sdf
                            .format(new Date(Long.parseLong(beginDate))));
                    ((TextView) getView().findViewById(R.id.txtEndDate)).setText(sdf
                            .format(new Date(Long.parseLong(endDate))));
                    ((TextView) getView().findViewById(R.id.txtUser)).setText(user);
                    ((TextView) getView().findViewById(R.id.txtBurned)).setText(burned);
                    ((TextView) getView().findViewById(R.id.txtRemaining)).setText(remaining);
                    ((TextView) getView().findViewById(R.id.txtComments)).setText(comments);
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

    public void saveTask() throws ParseException {

        Log.d(getClass().getSimpleName(), "Save Task");

        // mTxtSummary = (EditText) getView().findViewById(R.id.txtSummary);
        // mTxtDescription = (EditText)
        // getView().findViewById(R.id.txtDescription);
        // mTxtEstimated = (EditText) getView().findViewById(R.id.txtEstimated);
        // mTxtPriority = (EditText) getView().findViewById(R.id.txtPriority);
        // mTxtSprint = (EditText) getView().findViewById(R.id.txtSprint);
        // mTxtStatus = (EditText) getView().findViewById(R.id.txtStatus);
        // mTxtBeginDate = (EditText) getView().findViewById(R.id.txtBeginDate);
        // mTxtEndDate = (EditText) getView().findViewById(R.id.txtEndDate);
        // mTxtBurned = (EditText) getView().findViewById(R.id.txtBurned);
        // mTxtRemaining = (EditText) getView().findViewById(R.id.txtRemaining);
        // mTxtComments = (EditText) getView().findViewById(R.id.txtComments);

        ContentResolver cr = mActivity.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(Tasks.TASK_SUMMARY, mTxtSummary.getText().toString());
        values.put(Tasks.TASK_DESCRIPTION, mTxtDescription.getText().toString());
        values.put(Tasks.TASK_ESTIMATED, mTxtEstimated.getText().toString());
        values.put(Tasks.TASK_PRIORITY, mTxtPriority.getText().toString());
        values.put(Tasks.TASK_SPRINT_ID, mTxtSprint.getText().toString());
        values.put(Tasks.TASK_USER_ID, mTxtUser.getText().toString());
        values.put(Tasks.TASK_STATUS, mTxtStatus.getText().toString());
        values.put(Tasks.TASK_BEGINDATE, secureEpochDate(mTxtBeginDate.getText().toString()));
        values.put(Tasks.TASK_ENDDATE, secureEpochDate(mTxtEndDate.getText().toString()));
        values.put(Tasks.TASK_BURNED, mTxtBurned.getText().toString());
        values.put(Tasks.TASK_REMAINING, mTxtRemaining.getText().toString());
        values.put(Tasks.TASK_COMMENTS, mTxtComments.getText().toString());

        try {
            if (mState == STATE_INSERT) {
                values.put(Tasks.TASK_CREATED, Long.toString(System.currentTimeMillis()));
                values.put(Projects.SYNC_STATUS, SsaContract.STATUS_CREATED);
                cr.insert(Tasks.CONTENT_URI, values);
            } else {
                values.put(Projects.SYNC_STATUS, SsaContract.STATUS_DIRTY);
                cr.update(mContentUri, values, null, null);
            }
        } catch (NullPointerException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

    private String secureEpochDate(String insecureDate) {
        try {
            return String.valueOf(sdf.parse(insecureDate).getTime());
        } catch (Exception ignored) {
            return null; // FIXME jeje esto es poco 'elegante'
        }
    }

    private String formatDate(final String started) {
        try {
            return sdf.format(new Date(Long.parseLong(started)));
        } catch (Exception ignored) {
            return "no date"; // FIXME jeje esto es poco 'elegante'
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                try {
                    saveTask();
                } catch (ParseException e) {

                    Log.w(getClass().getSimpleName(), "Formato no correcto de fecha");

                }
                // FIXME
                // getActivity().finish();
                if (mState == STATE_EDIT) {
                    startActivity(new Intent(Intent.ACTION_VIEW, mContentUri));
                } else {
                    startActivity(new Intent(getActivity(), TaskListActivity.class));
                }
                return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String[] projection = new String[] {
                Users._ID,
                Users.USER_FULLNAME,
                Users.USER_NICKNAME,
                Users.USER_EMAIL,
                Users.USER_NUMBER,
                Users.USER_COMPANY,
                Users.USER_ROLE
        };

        // If the request went well (OK) and the request was
        // PICK_CONTACT_REQUEST
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == PICK_USER_REQUEST) {
                mUserUri = data.getData();
                final String user = Users.getUserId(mUserUri);
                ((TextView) getView().findViewById(R.id.txtUser)).setText(user);
            }

        // Cursor cursor = getContentResolver().query(data.getData(),
        // projection, null, null, null);

        // if (cursor.moveToFirst()) { // True if the cursor is not empty
        // columnIndex = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
        // String name = cursor.getString(columnIndex);

        // }
        // }
    }

}

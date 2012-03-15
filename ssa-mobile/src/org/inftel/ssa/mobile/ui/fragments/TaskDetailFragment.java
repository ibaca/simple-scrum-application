
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.SprintTable;
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
        View view = inflater.inflate(R.layout.fragment_sprint_detail, container, false);

        Bundle arguments = getArguments();
        // TODO buscar donde esta la constante _uri!
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
            final String sumamry = data.getString(data.getColumnIndex(SprintTable.KEY_SUMMARY));
            // Update UI
            mHandler.post(new Runnable() {
                public void run() {
                    ((TextView) getView().findViewById(R.id.sprint_title)).setText(sumamry);
                    ((TextView) getView().findViewById(R.id.sprint_subtitle)).setText("subtitle "
                            + sumamry);
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
        inflater.inflate(R.menu.sprint_detail_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(Intent.ACTION_INSERT, TaskContentProvider.CONTENT_URI));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * private static final String[] PROJECTION = new String[] {
     * TaskTable.COLUMN_ID, // 0 TaskTable.COLUMN_SUMMARY, // 1
     * TaskTable.COLUMN_DESCRIPTION, // 2 TaskTable.COLUMN_ESTIMATED, // 3
     * TaskTable.COLUMN_PRIORITY, // 4 TaskTable.COLUMN_SPRINT, // 5
     * TaskTable.COLUMN_USER, // 6 TaskTable.COLUMN_STATUS, // 7
     * TaskTable.COLUMN_BEGINDATE, // 8 TaskTable.COLUMN_ENDDATE, // 9
     * TaskTable.COLUMN_BURNED, // 10 TaskTable.COLUMN_REMAINING, // 11
     * TaskTable.COLUMN_COMMENTS, // 12 }; private static final int
     * COLUMN_INDEX_ID = 0; private static final int COLUMN_INDEX_SUMMARY = 1;
     * private static final int COLUMN_INDEX_DESCRIPTION = 2; private static
     * final int COLUMN_INDEX_ESTIMATED = 3; private static final int
     * COLUMN_INDEX_PRIORITY = 4; private static final int COLUMN_INDEX_SPRINT =
     * 5; private static final int COLUMN_INDEX_USER = 6; private static final
     * int COLUMN_INDEX_STATUS = 7; private static final int
     * COLUMN_INDEX_BEGINDATE = 8; private static final int COLUMN_INDEX_ENDDATE
     * = 9; private static final int COLUMN_INDEX_BURNED = 10; private static
     * final int COLUMN_INDEX_REMAINING = 11; private static final int
     * COLUMN_INDEX_COMMENTS = 12; private Uri mUri; private Cursor mCursor;
     * private TextView mTxtSummary; private TextView mTxtDescription; private
     * TextView mTxtEstimated; private TextView mTxtPriority; private TextView
     * mTxtSprint; private TextView mTxtUser; private TextView mTxtStatus;
     * private TextView mTxtBeginDate; private TextView mTxtEndDate; private
     * TextView mTxtBurned; private TextView mTxtRemaining; private TextView
     * mTxtComments;
     * @Override public void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState); final Intent intent = getIntent();
     * mUri = intent.getData(); setContentView(R.layout.ssa_task_details);
     * mTxtSummary = (TextView) findViewById(R.id.lblSummary); mTxtDescription =
     * (TextView) findViewById(R.id.lblDescription); mTxtEstimated = (TextView)
     * findViewById(R.id.lblEstimated); mTxtPriority = (TextView)
     * findViewById(R.id.lblPriority); mTxtSprint = (TextView)
     * findViewById(R.id.lblSprint); mTxtUser = (TextView)
     * findViewById(R.id.lblUser); mTxtStatus = (TextView)
     * findViewById(R.id.lblStatus); mTxtBeginDate = (TextView)
     * findViewById(R.id.lblBeginDate); mTxtEndDate = (TextView)
     * findViewById(R.id.lblEndDate); mTxtBurned = (TextView)
     * findViewById(R.id.lblBurned); mTxtRemaining = (TextView)
     * findViewById(R.id.lblRemaining); mTxtComments = (TextView)
     * findViewById(R.id.lblComments); mCursor = managedQuery(mUri, PROJECTION,
     * null, null, null); }
     * @Override protected void onResume() { super.onResume(); if (mCursor !=
     * null) { mCursor.requery(); mCursor.moveToFirst(); String texto = "";
     * String title = mCursor.getString(COLUMN_INDEX_ID); Resources res =
     * getResources(); String text =
     * String.format(res.getString(R.string.title_edit), title); setTitle(text);
     * texto = mCursor.getString(COLUMN_INDEX_SUMMARY);
     * mTxtSummary.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_DESCRIPTION);
     * mTxtDescription.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_ESTIMATED);
     * mTxtEstimated.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_PRIORITY);
     * mTxtPriority.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_SPRINT);
     * mTxtSprint.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_USER); mTxtUser.setTextKeepState(texto);
     * texto = mCursor.getString(COLUMN_INDEX_STATUS);
     * mTxtStatus.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_BEGINDATE);
     * mTxtBeginDate.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_ENDDATE);
     * mTxtEndDate.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_BURNED);
     * mTxtBurned.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_REMAINING);
     * mTxtRemaining.setTextKeepState(texto); texto =
     * mCursor.getString(COLUMN_INDEX_COMMENTS);
     * mTxtComments.setTextKeepState(texto); } else {
     * setTitle(getText(R.string.error_title));
     * mTxtSummary.setText(getText(R.string.error_message)); } }
     * @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater
     * inflater = getMenuInflater();
     * inflater.inflate(R.menu.ssa_task_details_menu, menu); Intent intent = new
     * Intent(null, getIntent().getData());
     * intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
     * menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, new
     * ComponentName(this, TaskDetailFragment.class), null, intent, 0, null);
     * return super.onCreateOptionsMenu(menu); }
     * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
     * (item.getItemId()) { case R.id.menu_edit: startActivity(new
     * Intent(Intent.ACTION_EDIT, mUri, TaskDetailFragment.this,
     * TaskEditFragment.class)); return true; default: return
     * super.onOptionsItemSelected(item); } }
     */

}

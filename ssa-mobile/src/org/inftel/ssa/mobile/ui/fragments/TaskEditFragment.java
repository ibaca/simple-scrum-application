
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.TaskTable;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class TaskEditFragment extends Activity {
    private static final String TAG = "TaskEditActivity";

    private static final String[] PROJECTION = new String[] {
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

    /** The index of the id column */
    private static final int COLUMN_INDEX_ID = 0;
    /** The index of the summary column */
    private static final int COLUMN_INDEX_SUMMARY = 1;
    /** The index of the description column */
    private static final int COLUMN_INDEX_DESCRIPTION = 2;
    /** The index of the estimated column */
    private static final int COLUMN_INDEX_ESTIMATED = 3;
    /** The index of the priority column */
    private static final int COLUMN_INDEX_PRIORITY = 4;
    /** The index of the sprint column */
    private static final int COLUMN_INDEX_SPRINT = 5;
    /** The index of the user column */
    private static final int COLUMN_INDEX_USER = 6;
    /** The index of the status column */
    private static final int COLUMN_INDEX_STATUS = 7;
    /** The index of the beginDate column */
    private static final int COLUMN_INDEX_BEGINDATE = 8;
    /** The index of the endDate column */
    private static final int COLUMN_INDEX_ENDDATE = 9;
    /** The index of the burned column */
    private static final int COLUMN_INDEX_BURNED = 10;
    /** The index of the remaining column */
    private static final int COLUMN_INDEX_REMAINING = 11;
    /** The index of the comments column */
    private static final int COLUMN_INDEX_COMMENTS = 12;

    // The different distinct states the activity can be run in.
    private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;

    private int mState;
    private Uri mUri;
    private Cursor mCursor;
    private EditText mTxtSummary;
    private EditText mTxtDescription;
    private EditText mTxtEstimated;
    private EditText mTxtPriority;
    private EditText mTxtSprint;
    private EditText mTxtUser;
    private EditText mTxtStatus;
    private EditText mTxtBeginDate;
    private EditText mTxtEndDate;
    private EditText mTxtBurned;
    private EditText mTxtRemaining;
    private EditText mTxtComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();

        // Set the layout for this activity.
        setContentView(R.layout.ssa_task_edit);

        // The text view for task data, identified by its ID in the XML file.
        mTxtSummary = (EditText) findViewById(R.id.txtSummary);
        mTxtDescription = (EditText) findViewById(R.id.txtDescription);
        mTxtEstimated = (EditText) findViewById(R.id.txtEstimated);
        mTxtPriority = (EditText) findViewById(R.id.txtPriority);
        mTxtSprint = (EditText) findViewById(R.id.txtSprint);
        mTxtUser = (EditText) findViewById(R.id.txtUser);
        mTxtStatus = (EditText) findViewById(R.id.txtStatus);
        mTxtBeginDate = (EditText) findViewById(R.id.txtBeginDate);
        mTxtEndDate = (EditText) findViewById(R.id.txtEndDate);
        mTxtBurned = (EditText) findViewById(R.id.txtBurned);
        mTxtRemaining = (EditText) findViewById(R.id.txtRemaining);
        mTxtComments = (EditText) findViewById(R.id.txtComments);

        // Get the task!
        // uri.getLastPathSegment()

        // Do some setup based on the action being performed.
        final String action = intent.getAction();
        if (Intent.ACTION_EDIT.equals(action)) {
            mState = STATE_EDIT;
            mUri = intent.getData();
            mCursor = managedQuery(mUri, PROJECTION, null, null, null);
        } else if (Intent.ACTION_INSERT.equals(action)) {
            // Requested to insert: set that state, and create a new entry
            // in the container.
            mState = STATE_INSERT;
            mUri = getContentResolver().insert(intent.getData(), null);

            // If we were unable to create a new note, then just finish
            // this activity. A RESULT_CANCELED will be sent back to the
            // original activity if they requested a result.
            if (mUri == null) {
                Log.e(TAG, "Failed to insert new note into " + getIntent().getData());
                finish();
                return;
            }

            // The new entry was created, so assume all will end well and
            // set the result to be returned.
            // setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));

        } else {
            // Whoops, unknown action! Bail.
            Log.e(TAG, "Unknown action, exiting");
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If we didn't have any trouble retrieving the data, it is now
        // time to get at the stuff.
        if (mCursor != null) {
            // Requery in case something changed while paused (such as the
            // title)
            mCursor.requery();
            // Make sure we are at the one and only row in the cursor.
            mCursor.moveToFirst();

            String texto = "";

            // Set the title of the Activity to include the note title
            String title = mCursor.getString(COLUMN_INDEX_ID);
            Resources res = getResources();
            String text = String.format(res.getString(R.string.title_edit), title);
            setTitle(text);

            // Modify the task data
            // This is a little tricky: we may be resumed after previously being
            // paused/stopped. We want to put the new text in the text view,
            // but leave the user where they were (retain the cursor position
            // etc). This version of setText does that for us.
            texto = mCursor.getString(COLUMN_INDEX_SUMMARY);
            mTxtSummary.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_DESCRIPTION);
            mTxtDescription.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_ESTIMATED);
            mTxtEstimated.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_PRIORITY);
            mTxtPriority.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_SPRINT);
            mTxtSprint.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_USER);
            mTxtUser.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_STATUS);
            mTxtStatus.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_BEGINDATE);
            mTxtBeginDate.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_ENDDATE);
            mTxtEndDate.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_BURNED);
            mTxtBurned.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_REMAINING);
            mTxtRemaining.setTextKeepState(texto);
            texto = mCursor.getString(COLUMN_INDEX_COMMENTS);
            mTxtComments.setTextKeepState(texto);

        } else if (mState == STATE_EDIT) {
            setTitle(getText(R.string.error_title));
            mTxtSummary.setText(getText(R.string.error_message));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ssa_task_edit_menu, menu);

        // Generate any additional actions that can be performed on the
        // overall list.
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, TaskEditFragment.class), null, intent, 0, null);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                // Launch activity to insert a new item
                // startActivity(new Intent(Intent.ACTION_EDIT, mUri,
                // TaskDetailFragment.this,
                // TaskEditFragment.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

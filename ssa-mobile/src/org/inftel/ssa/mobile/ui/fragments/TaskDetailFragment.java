
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class TaskDetailFragment extends Activity {
    private static final String TAG = "TaskDetailFragmen";

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

    private Uri mUri;
    private Cursor mCursor;
    private TextView mTxtSummary;
    private TextView mTxtDescription;
    private TextView mTxtEstimated;
    private TextView mTxtPriority;
    private TextView mTxtSprint;
    private TextView mTxtUser;
    private TextView mTxtStatus;
    private TextView mTxtBeginDate;
    private TextView mTxtEndDate;
    private TextView mTxtBurned;
    private TextView mTxtRemaining;
    private TextView mTxtComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();

        mUri = intent.getData();

        // Set the layout for this activity. You can find it in
        // res/layout/note_editor.xml
        setContentView(R.layout.ssa_task_details);

        // The text view for task data, identified by its ID in the XML file.
        mTxtSummary = (TextView) findViewById(R.id.lblSummary);
        mTxtDescription = (TextView) findViewById(R.id.lblDescription);
        mTxtEstimated = (TextView) findViewById(R.id.lblEstimated);
        mTxtPriority = (TextView) findViewById(R.id.lblPriority);
        mTxtSprint = (TextView) findViewById(R.id.lblSprint);
        mTxtUser = (TextView) findViewById(R.id.lblUser);
        mTxtStatus = (TextView) findViewById(R.id.lblStatus);
        mTxtBeginDate = (TextView) findViewById(R.id.lblBeginDate);
        mTxtEndDate = (TextView) findViewById(R.id.lblEndDate);
        mTxtBurned = (TextView) findViewById(R.id.lblBurned);
        mTxtRemaining = (TextView) findViewById(R.id.lblRemaining);
        mTxtComments = (TextView) findViewById(R.id.lblComments);

        // Get the task
        mCursor = managedQuery(mUri, PROJECTION, null, null, null);
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

        } else {
            setTitle(getText(R.string.error_title));
            mTxtSummary.setText(getText(R.string.error_message));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ssa_task_details_menu, menu);

        // Generate any additional actions that can be performed on the
        // overall list.
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, TaskDetailFragment.class), null, intent, 0, null);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                // Launch activity to insert a new item
                startActivity(new Intent(Intent.ACTION_EDIT, mUri, TaskDetailFragment.this,
                        TaskEditFragment.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

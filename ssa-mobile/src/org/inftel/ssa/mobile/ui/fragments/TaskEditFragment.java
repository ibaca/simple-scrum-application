
package org.inftel.ssa.mobile.ui.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class TaskEditFragment extends Fragment implements LoaderCallbacks<Cursor> {
    private static final String TAG = "TaskEditActivity";

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
     * COLUMN_INDEX_COMMENTS = 12; private static final int STATE_EDIT = 0;
     * private static final int STATE_INSERT = 1; private int mState; private
     * Uri mUri; private Cursor mCursor; private EditText mTxtSummary; private
     * EditText mTxtDescription; private EditText mTxtEstimated; private
     * EditText mTxtPriority; private EditText mTxtSprint; private EditText
     * mTxtUser; private EditText mTxtStatus; private EditText mTxtBeginDate;
     * private EditText mTxtEndDate; private EditText mTxtBurned; private
     * EditText mTxtRemaining; private EditText mTxtComments;
     * @Override protected void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState); final Intent intent = getIntent();
     * setContentView(R.layout.ssa_task_edit); mTxtSummary = (EditText)
     * findViewById(R.id.txtSummary); mTxtDescription = (EditText)
     * findViewById(R.id.txtDescription); mTxtEstimated = (EditText)
     * findViewById(R.id.txtEstimated); mTxtPriority = (EditText)
     * findViewById(R.id.txtPriority); mTxtSprint = (EditText)
     * findViewById(R.id.txtSprint); mTxtUser = (EditText)
     * findViewById(R.id.txtUser); mTxtStatus = (EditText)
     * findViewById(R.id.txtStatus); mTxtBeginDate = (EditText)
     * findViewById(R.id.txtBeginDate); mTxtEndDate = (EditText)
     * findViewById(R.id.txtEndDate); mTxtBurned = (EditText)
     * findViewById(R.id.txtBurned); mTxtRemaining = (EditText)
     * findViewById(R.id.txtRemaining); mTxtComments = (EditText)
     * findViewById(R.id.txtComments); final String action = intent.getAction();
     * if (Intent.ACTION_EDIT.equals(action)) { mState = STATE_EDIT; mUri =
     * intent.getData(); mCursor = managedQuery(mUri, PROJECTION, null, null,
     * null); } else if (Intent.ACTION_INSERT.equals(action)) { mState =
     * STATE_INSERT; mUri = getContentResolver().insert(intent.getData(), null);
     * if (mUri == null) { Log.e(TAG, "Failed to insert new note into " +
     * getIntent().getData()); finish(); return; } } else { Log.e(TAG,
     * "Unknown action, exiting"); finish(); return; } }
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
     * mTxtComments.setTextKeepState(texto); } else if (mState == STATE_EDIT) {
     * setTitle(getText(R.string.error_title));
     * mTxtSummary.setText(getText(R.string.error_message)); } }
     * @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater
     * inflater = getMenuInflater(); inflater.inflate(R.menu.ssa_task_edit_menu,
     * menu); Intent intent = new Intent(null, getIntent().getData());
     * intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
     * menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, new
     * ComponentName(this, TaskEditFragment.class), null, intent, 0, null);
     * return super.onCreateOptionsMenu(menu); }
     * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
     * (item.getItemId()) { case R.id.menu_save: return true; default: return
     * super.onOptionsItemSelected(item); } }
     */
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }

}

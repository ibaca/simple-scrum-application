
package org.inftel.ssa.mobile.ui.fragments;

import static org.inftel.ssa.mobile.ui.BaseActivity.ARGS_URI;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    private static final String TAG = "TaskListFragment";

    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;

    protected Cursor mCursor = null;
    protected TaskListAdapter mAdapter;
    protected Uri mContentUri;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get(ARGS_URI) != null) {
            mContentUri = (Uri) arguments.get(ARGS_URI);
        }

        mAdapter = new TaskListAdapter(getActivity(), mCursor);

        // Allocate the adapter to the list displayed within this fragment.
        setListAdapter(mAdapter);

        // Enable context menu
        registerForContextMenu(getListView());

        // Populate the adapter / list using a Cursor Loader.
        getLoaderManager().initLoader(0, null, this);

        setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long theid) {
        super.onListItemClick(l, v, position, theid);

        Cursor c = mAdapter.getCursor();
        c.moveToPosition(position);
        String taskId = String.valueOf(c.getLong(c.getColumnIndex(Tasks._ID)));

        // Start view activity to show sprint details
        startActivity(new Intent(Intent.ACTION_VIEW, Tasks.buildTasktUri(taskId)));
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = new String[] {
                Tasks._ID, Tasks.TASK_SUMMARY, Tasks.TASK_DESCRIPTION,
                Tasks.TASK_ESTIMATED
        };

        return new CursorLoader(getActivity(), mContentUri,
                projection, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        Log.d("TaskList", "context menu created");
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, R.string.menu_edit);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("TaskList", "context menu selected");
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case EDIT_ID: {
                Cursor c = mAdapter.getCursor();
                c.moveToPosition(info.position);
                String taskId = String.valueOf(c.getLong(c.getColumnIndex(Tasks._ID)));

                // Start view activity to show task details
                startActivity(new Intent(Intent.ACTION_EDIT, Tasks.buildTasktUri(taskId)));
                return true;
            }
            case DELETE_ID: {
                // TODO Igual un popup que pregunte si se esta seguro
                String taskId = String.valueOf(info.id);
                Uri uri = Tasks.buildTasktUri(taskId);
                getActivity().getContentResolver().delete(uri, null, null);
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Log.d(getClass().getSimpleName(), "Creando nueva task");
                final Intent intent = new Intent(Intent.ACTION_INSERT, Tasks.CONTENT_URI);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TaskListAdapter extends CursorAdapter {
        private TextView subtitleView;
        private TextView titleView;
        private TextView taskEstimated;

        public TaskListAdapter(Context context, Cursor cursor) {
            super(context, cursor, false);
        }

        /** {@inheritDoc} */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return getActivity().getLayoutInflater().inflate(R.layout.ssa_task_list, parent,
                    false);
        }

        /** {@inheritDoc} */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            titleView = (TextView) view.findViewById(R.id.task_title);
            subtitleView = (TextView) view.findViewById(R.id.task_subtitle);
            taskEstimated = (TextView) view.findViewById(R.id.task_estimated);

            int colSummary = cursor.getColumnIndex(Tasks.TASK_SUMMARY);
            int colDescription = cursor.getColumnIndex(Tasks.TASK_DESCRIPTION);
            int colEstimated = cursor.getColumnIndex(Tasks.TASK_ESTIMATED);

            titleView.setText(cursor.getString(colSummary));
            subtitleView.setText(cursor.getString(colDescription));
            taskEstimated.setText(cursor.getString(colEstimated));
        }

    }

}

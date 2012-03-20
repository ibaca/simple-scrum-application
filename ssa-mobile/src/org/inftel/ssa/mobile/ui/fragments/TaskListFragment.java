
package org.inftel.ssa.mobile.ui.fragments;

import static org.inftel.ssa.mobile.ui.BaseActivity.ARGS_URI;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;
import org.inftel.ssa.mobile.contentproviders.TaskContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskTable;

import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.support.v4.widget.SimpleCursorAdapter;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        Uri taskUri = ContentUris.withAppendedId(TaskContentProvider.CONTENT_URI,
                c.getLong(c.getColumnIndex(TaskTable.COLUMN_ID)));

        // Start view activity to show sprint details
        startActivity(new Intent(Intent.ACTION_VIEW, taskUri));
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                TaskTable.COLUMN_ID, TaskTable.COLUMN_SUMMARY, TaskTable.COLUMN_DESCRIPTION, 
                TaskTable.COLUMN_ESTIMATED
        };

        return new CursorLoader(getActivity(), TaskContentProvider.CONTENT_URI,
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
            case EDIT_ID:
                Cursor c = mAdapter.getCursor();
                c.moveToPosition(info.position);

                Uri projectUri = ContentUris.withAppendedId(TaskContentProvider.CONTENT_URI,
                        c.getLong(c.getColumnIndex(TaskTable.COLUMN_ID)));

                // Start view activity to show sprint details
                startActivity(new Intent(Intent.ACTION_EDIT, projectUri));
                return true;
            case DELETE_ID:
                // TODO Igual un popup que pregunte si se esta seguro
                Uri uri = ContentUris.withAppendedId(TaskContentProvider.CONTENT_URI, info.id);
                getActivity().getContentResolver().delete(uri, null, null);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ssa_task_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Log.d(getClass().getSimpleName(), "Creando nueva task");
                final Intent intent = new Intent(Intent.ACTION_INSERT,
                        TaskContentProvider.CONTENT_URI);
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
            super(context, cursor, true);
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

            int colSummary = cursor.getColumnIndex(TaskTable.COLUMN_SUMMARY);
            int colDescription = cursor.getColumnIndex(TaskTable.COLUMN_DESCRIPTION);
            int colEstimated = cursor.getColumnIndex(TaskTable.COLUMN_ESTIMATED);

            Log.d(getClass().getSimpleName(), "" + colSummary);
            Log.d(getClass().getSimpleName(), "" + colDescription);

            titleView.setText(cursor.getString(colSummary));
            subtitleView.setText(cursor.getString(colDescription));
            taskEstimated.setText(cursor.getString(colEstimated));
            

        }

    }
    
}

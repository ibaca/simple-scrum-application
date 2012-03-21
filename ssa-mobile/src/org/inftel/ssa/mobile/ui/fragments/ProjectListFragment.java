
package org.inftel.ssa.mobile.ui.fragments;

import static android.content.Intent.ACTION_EDIT;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;

import android.content.ContentResolver;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;

/**
 * Fragmento UI para mostrar lista de projectos
 */

public class ProjectListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;

    protected Cursor mCursor = null;
    protected ProjectListAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ProjectListAdapter(getActivity(), mCursor);

        // Allocate the adapter to the list displayed within this fragment.
        setListAdapter(mAdapter);

        // Enable context menu
        registerForContextMenu(getListView());

        // Populate the adapter / list using a Cursor Loader.
        getLoaderManager().initLoader(0, null, this);

        // setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Cursor c = mAdapter.getCursor();
        c.moveToPosition(position);

        String projectId = String.valueOf(c.getLong(c.getColumnIndex(Projects._ID)));

        // Start view activity to show sprint details
        startActivity(new Intent(Intent.ACTION_VIEW, Projects.buildProjectUri(projectId)));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                Projects._ID,
                Projects.PROJECT_NAME,
                Projects.PROJECT_OPENED,
                Projects.PROJECT_CLOSE,
                Projects.PROJECT_SUMMARY
        };

        return new CursorLoader(getActivity(), Projects.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo
            menuInfo) {
        Log.d("ProjectList", "context menu created");
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, R.string.menu_edit);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("ProjectList", "context menu selected");
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)
                item.getMenuInfo();
        switch (item.getItemId()) {
            case EDIT_ID: {
                Cursor c = mAdapter.getCursor();
                c.moveToPosition(info.position);
                String projectId = String.valueOf(c.getLong(c.getColumnIndex(Projects._ID)));

                // Start view activity to show sprint details
                startActivity(new Intent(ACTION_EDIT, Projects.buildProjectUri(projectId)));
                return true;
            }
            case DELETE_ID: {
                // TODO Igual un popup que pregunte si se esta seguro
                String projectId = String.valueOf(info.id);
                Uri uri = Projects.buildProjectUri(projectId);
                getActivity().getContentResolver().delete(uri, null, null);
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    // @Override
    // public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // inflater.inflate(R.menu.new_project_menu, menu);
    // super.onCreateOptionsMenu(menu, inflater);
    // }
    //
    // @Override
    // public boolean onOptionsItemSelected(MenuItem item) {
    // switch (item.getItemId()) {
    // case R.id.menu_add:
    // Log.d(getClass().getSimpleName(), "Creando nuevo proyecto");
    // final Intent intent = new Intent(Intent.ACTION_INSERT,
    // ProjectContentProvider.CONTENT_URI);
    // startActivity(intent);
    // return true;
    // }
    // return super.onOptionsItemSelected(item);
    // }

    private class ProjectListAdapter extends CursorAdapter {
        private TextView subtitleView;
        private TextView titleView;
        private TextView countDoneTask;
        private TextView createdView;

        public ProjectListAdapter(Context context, Cursor cursor) {
            super(context, cursor, true);
        }

        /** {@inheritDoc} */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return getActivity().getLayoutInflater().inflate(R.layout.ssa_project_list, parent,
                    false);
        }

        /** {@inheritDoc} */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            titleView = (TextView) view.findViewById(R.id.project_title);
            subtitleView = (TextView) view.findViewById(R.id.project_subtitle);
            createdView = (TextView) view.findViewById(R.id.project_created);
            // countDoneTask = (TextView) view.findViewById(R.id.countDoneTask);

            int colName = cursor.getColumnIndex(Projects.PROJECT_NAME);
            int colSummary = cursor.getColumnIndex(Projects.PROJECT_SUMMARY);
            int colOpened = cursor.getColumnIndex(Projects.PROJECT_OPENED);
            // int colId = cursor.getColumnIndex(Projects._ID);

            // String doneTask = numberTaskComplete(cursor.getString(colId));
            String txtOpened = cursor.getString(colOpened);
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            PrettyTime p = new PrettyTime();
            String prettyTime = "no date";
            if (txtOpened != null) {
                try {
                    prettyTime = p.format(date.parse(txtOpened));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            Log.d(getClass().getSimpleName(), "" + txtOpened);

            titleView.setText(cursor.getString(colName));
            subtitleView.setText(cursor.getString(colSummary));
            createdView.setText("Created " + prettyTime + " ");
            // countDoneTask.setText(doneTask);
        }

    }

    public String numberTaskComplete(String projectId) {
        int doneTask = 0;

        ContentResolver cr = getActivity().getContentResolver();
        String[] projection = new String[] {
                Tasks._ID, Tasks.TASK_STATUS
        };
        String selection = "project = ?";
        String[] selectionArgs = new String[] {
                projectId
        };
        Cursor cursor = cr.query(Tasks.CONTENT_URI, projection, selection,
                selectionArgs, null);
        int taskCount = cursor.getCount();
        if (taskCount > 0) {
            while (cursor.moveToNext()) {
                String status = cursor.getString(cursor.getColumnIndex(Tasks.TASK_STATUS));
                if (status.equals("2")) {
                    doneTask++;
                }
            }
        }

        return "Tasks " + doneTask + "/" + taskCount + " ";
    }

}

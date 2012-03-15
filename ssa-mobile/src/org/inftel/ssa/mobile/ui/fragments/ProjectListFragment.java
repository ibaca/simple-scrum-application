
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectContentProvider;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Cursor c = mAdapter.getCursor();
        c.moveToPosition(position);

        Uri projectUri = ContentUris.withAppendedId(ProjectContentProvider.CONTENT_URI,
                c.getLong(c.getColumnIndex(ProjectTable.KEY_ID)));

        // Start view activity to show sprint details
        startActivity(new Intent(Intent.ACTION_VIEW, projectUri));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                ProjectTable.KEY_ID, ProjectTable.KEY_NAME,
                ProjectTable.KEY_OPENED, ProjectTable.KEY_CLOSE,
                ProjectTable.KEY_SUMMARY
        };

        return new CursorLoader(getActivity(), ProjectContentProvider.CONTENT_URI,
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

    // @Override
    // public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo
    // menuInfo) {
    // Log.d("FencesList", "context menu created");
    // super.onCreateContextMenu(menu, v, menuInfo);
    // menu.add(0, EDIT_ID, 0, R.string.menu_edit);
    // menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    // }
    //
    // @Override
    // public boolean onContextItemSelected(MenuItem item) {
    // Log.d("FencesList", "context menu selected");
    // AdapterContextMenuInfo info = (AdapterContextMenuInfo)
    // item.getMenuInfo();
    // switch (item.getItemId()) {
    // case EDIT_ID:
    // // Delega comportamiento al click listener
    // onListItemClick(getListView(), getView(), info.position, info.id);
    // return true;
    // case DELETE_ID:
    // // TODO Igual un popup que pregunte si se esta seguro
    // Uri uri = ContentUris.withAppendedId(SprintContentProvider.CONTENT_URI,
    // info.id);
    // getActivity().getContentResolver().delete(uri, null, null);
    // return true;
    // }
    // return super.onContextItemSelected(item);
    // }

    private class ProjectListAdapter extends CursorAdapter {
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

            final TextView titleView = (TextView) view.findViewById(R.id.project_title);
            final TextView subtitleView = (TextView) view.findViewById(R.id.project_subtitle);

            int colName = cursor.getColumnIndex(ProjectTable.KEY_NAME);
            int colSummary = cursor.getColumnIndex(ProjectTable.KEY_SUMMARY);

            Log.d(getClass().getSimpleName(), "" + colName);
            Log.d(getClass().getSimpleName(), "" + colSummary);

            titleView.setText(cursor.getString(colName));
            subtitleView.setText(cursor.getString(colSummary));

        }

    }

}

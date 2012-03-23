
package org.inftel.ssa.mobile.ui.fragments;

import static org.inftel.ssa.mobile.ui.BaseActivity.ARGS_URI;
import static org.inftel.ssa.mobile.util.Lists.ints;
import static org.inftel.ssa.mobile.util.Lists.strings;

import java.util.Date;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.SsaConstants;
import org.inftel.ssa.mobile.provider.SsaContract.Sprints;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

/**
 * Fragmento UI para mostra lista de fences del dispositivo.
 */
public class SprintListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;

    protected Cursor mCursor = null;
    protected SimpleCursorAdapter mAdapter;
    protected Uri mContentUri;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get(ARGS_URI) != null) {
            mContentUri = (Uri) arguments.get(ARGS_URI);
        }

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.ssa_sprint_list,
                mCursor, strings(Sprints.SPRINT_SUMMARY), ints(R.id.sprintname), 0);

        // Allocate the adapter to the List displayed within this fragment.
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

        // Find the ID and Reference of the selected fence.
        Cursor c = mAdapter.getCursor();
        c.moveToPosition(position);

        Uri sprintUri = Sprints.buildSprintUri(c.getString(c.getColumnIndex(Sprints._ID)));
        Intent intent = new Intent(Intent.ACTION_VIEW, sprintUri);
        // Se añade cursor y posicion para ViewPager
        intent.putExtra(SsaConstants.EXTRA_LIST_CURSOR_URI, mContentUri);
        intent.putExtra(SsaConstants.EXTRA_LIST_CURSOR_POSITION, position);

        // Start view activity to show sprint details
        startActivity(intent);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                Sprints._ID, Sprints.SPRINT_SUMMARY
        };
        return new CursorLoader(getActivity(), Sprints.CONTENT_URI,
                projection, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        Log.d("FencesList", "context menu created");
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, R.string.menu_edit);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("FencesList", "context menu selected");
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case EDIT_ID:
                // Delega comportamiento al click listener
                onListItemClick(getListView(), getView(), info.position, info.id);
                return true;
            case DELETE_ID:
                // TODO Igual un popup que pregunte si se esta seguro
                String sprintId = String.valueOf(info.id);
                Uri uri = Sprints.buildSprintUri(sprintId);
                getActivity().getContentResolver().delete(uri, null, null);
                return true;
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
                Log.d("SprintListFragment", "add clicked");

                ContentResolver cr = getActivity().getContentResolver();
                ContentValues values = new ContentValues();
                values.put(Sprints.SPRINT_SUMMARY, new Date().toString());
                cr.update(mContentUri.buildUpon().appendPath("1").build(), values, null, null);

                // startActivity(new Intent(Intent.ACTION_INSERT,
                // Sprints.CONTENT_URI));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

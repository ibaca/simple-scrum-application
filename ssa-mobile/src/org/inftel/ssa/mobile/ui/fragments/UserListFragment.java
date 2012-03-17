
package org.inftel.ssa.mobile.ui.fragments;

import static org.inftel.ssa.mobile.ui.BaseActivity.ARGS_URI;

import org.inftel.ssa.mobile.contentproviders.UserContentProvider;
import org.inftel.ssa.mobile.contentproviders.UserTable;

import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.view.View;
import android.widget.ListView;

public class UserListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

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

        mAdapter = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1,
                mCursor,
                new String[] {
                        UserTable.KEY_FULLNAME
                }, new int[] {
                        android.R.id.text1
                },
                0);
        // Allocate the adapter to the List displayed within this fragment.
        setListAdapter(mAdapter);

        // Enable context menu
        registerForContextMenu(getListView());

        // Populate the adapter / list using a Cursor Loader.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long theid) {
        super.onListItemClick(l, v, position, theid);

        // Find the ID and Reference of the selected fence.
        Cursor c = mAdapter.getCursor();
        c.moveToPosition(position);

        Uri userUri = ContentUris.withAppendedId(UserContentProvider.CONTENT_URI,
                c.getLong(c.getColumnIndex(UserTable.KEY_ID)));

        // Start view activity to show sprint details
        startActivity(new Intent(Intent.ACTION_VIEW, userUri));
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                UserTable.KEY_ID, UserTable.KEY_FULLNAME
        };
        String selection = null;
        String[] selectionArgs = null;
        if (mContentUri.getQueryParameter("project_id") != null) {
            selection = "project_id = ?";
            selectionArgs = new String[] {
                    mContentUri.getQueryParameter("project_id")
            };

        }

        return new CursorLoader(getActivity(), UserContentProvider.CONTENT_URI,
                projection, selection, selectionArgs, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    static void insertarEliminar(ContentResolver cr) {

        ContentValues values = new ContentValues();

        cr.delete(UserContentProvider.CONTENT_URI,
                UserTable.KEY_NICKNAME + " = 'ibaca'", null);
        cr.delete(UserContentProvider.CONTENT_URI,
                UserTable.KEY_NICKNAME + " = 'JuaNaN'", null);
        cr.delete(UserContentProvider.CONTENT_URI,
                UserTable.KEY_NICKNAME + " = '3'", null);
        cr.delete(UserContentProvider.CONTENT_URI,
                UserTable.KEY_NICKNAME + " = '4'", null);

        values.put(UserTable.KEY_FULLNAME, "Ignacio Baca");
        values.put(UserTable.KEY_NICKNAME, "ibaca");
        values.put(UserTable.KEY_EMAIL, "ignacio@gmail.com");
        values.put(UserTable.KEY_NUMBER, "957700652");
        values.put(UserTable.KEY_COMPANY, "Inftel");
        values.put(UserTable.KEY_PASS, "inftel");
        values.put(UserTable.KEY_ROLE, "SM");

        cr.insert(UserContentProvider.CONTENT_URI, values);

        values.put(UserTable.KEY_FULLNAME, "Juan Ant. Cobo");
        values.put(UserTable.KEY_NICKNAME, "JuaNaN");
        values.put(UserTable.KEY_EMAIL, "juanan20@gmail.com");
        values.put(UserTable.KEY_NUMBER, "957700652");
        values.put(UserTable.KEY_COMPANY, "Inftel");
        values.put(UserTable.KEY_PASS, "inftel");
        values.put(UserTable.KEY_ROLE, "SM");

        cr.insert(UserContentProvider.CONTENT_URI, values);

        values.put(UserTable.KEY_FULLNAME, "Jesus Ruiz");
        values.put(UserTable.KEY_NICKNAME, "3");
        values.put(UserTable.KEY_EMAIL, "jrovillano@gmail.com");
        values.put(UserTable.KEY_NUMBER, "957700652");
        values.put(UserTable.KEY_COMPANY, "Master Inftel");
        values.put(UserTable.KEY_PASS, "inftel");
        values.put(UserTable.KEY_ROLE, "SM");

        cr.insert(UserContentProvider.CONTENT_URI, values);

        values.put(UserTable.KEY_FULLNAME, "Jesus Barriga");
        values.put(UserTable.KEY_NICKNAME, "4");
        values.put(UserTable.KEY_EMAIL, "jesusbarriga@gmail.com");
        values.put(UserTable.KEY_NUMBER, "957700652");
        values.put(UserTable.KEY_COMPANY, "Master Inftel");
        values.put(UserTable.KEY_PASS, "inftel");
        values.put(UserTable.KEY_ROLE, "SM");

        cr.insert(UserContentProvider.CONTENT_URI, values);

    }

}

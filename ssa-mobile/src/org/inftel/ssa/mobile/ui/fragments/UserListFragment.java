
package org.inftel.ssa.mobile.ui.fragments;

import static org.inftel.ssa.mobile.ui.BaseActivity.ARGS_URI;

import org.inftel.ssa.mobile.provider.SsaContract.Users;

import android.content.ContentResolver;
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

        ContentResolver cr = getActivity().getContentResolver();

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get(ARGS_URI) != null) {
            mContentUri = (Uri) arguments.get(ARGS_URI);
        }

        mAdapter = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1,
                mCursor,
                new String[] {
                        Users.USER_FULLNAME
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

        Uri userUri = Users.buildUserUri(c.getString(c.getColumnIndex(Users._ID)));

        // ACTION PICK
        String action = getActivity().getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action) ||
                Intent.ACTION_GET_CONTENT.equals(action)) {
            System.out.println("entro en el ACTION_PICK");
            // The caller is waiting for us to return a note selected by
            // the user. The have clicked on one, so return it now.
            getActivity().setResult(-1, new
                    Intent().setData(userUri));
        } else {
            System.out.println("no entro en el ACTION_PICK");
            // Launch activity to view/edit the currently selected item
            startActivity(new Intent(Intent.ACTION_VIEW, userUri));
        }
        // FINALIZA ACTION PICK

        //
        // Start view activity to show sprint details
        // startActivity(new Intent(Intent.ACTION_VIEW, userUri));
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                Users._ID, Users.USER_FULLNAME
        };
        String selection = null;
        String[] selectionArgs = null;
        if (mContentUri.getQueryParameter("project_id") != null) {
            selection = Users.USER_PROJECT_ID + " = ?";
            selectionArgs = new String[] {
                    mContentUri.getQueryParameter("project_id")
            };

        }

        return new CursorLoader(getActivity(),
                Users.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}

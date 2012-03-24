
package org.inftel.ssa.mobile.ui.fragments;

import static org.inftel.ssa.mobile.ui.BaseActivity.ARGS_URI;
import static org.inftel.ssa.mobile.util.Lists.strings;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Users;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class UserListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    protected Cursor mCursor = null;
    protected UserListAdapter mAdapter;
    protected Uri mContentUri;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ContentResolver cr = getActivity().getContentResolver();

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get(ARGS_URI) != null) {
            mContentUri = (Uri) arguments.get(ARGS_URI);
        }

        mAdapter = new UserListAdapter(getActivity(), mCursor);

        // Allocate the adapter to the list displayed within this fragment.
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
            // The caller is waiting for us to return a user selected by
            // the user. The have clicked on one, so return it now.
            Intent intent = new Intent();
            intent.setData(userUri);
            getActivity().setResult(-1, intent);
            getActivity().finish();
        } else {
            // Launch activity to view/edit the currently selected item
            startActivity(new Intent(Intent.ACTION_VIEW, userUri));
        }
        // FINALIZA ACTION PICK

        //
        // Start view activity to show sprint details
        // startActivity(new Intent(Intent.ACTION_VIEW, userUri));
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), mContentUri,
                strings(Users._ID, Users.USER_FULLNAME, Users.USER_ROLE), null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private class UserListAdapter extends CursorAdapter {
        private TextView titleView;
        private TextView subtitleView;

        public UserListAdapter(Context context, Cursor cursor) {
            super(context, cursor, true);
        }

        /** {@inheritDoc} */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return getActivity().getLayoutInflater().inflate(R.layout.ssa_user_list, parent,
                    false);
        }

        /** {@inheritDoc} */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            titleView = (TextView) view.findViewById(R.id.user_fullname);
            subtitleView = (TextView) view.findViewById(R.id.user_role);

            int colName = cursor.getColumnIndex(Users.USER_FULLNAME);
            int colRole = cursor.getColumnIndex(Users.USER_ROLE);

            titleView.setText(cursor.getString(colName));
            subtitleView.setText(cursor.getString(colRole));
        }

    }

}

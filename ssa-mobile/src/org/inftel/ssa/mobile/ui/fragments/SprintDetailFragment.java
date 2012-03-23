
package org.inftel.ssa.mobile.ui.fragments;

import static org.inftel.ssa.mobile.ui.BaseActivity.ARGS_URI;
import static org.inftel.ssa.mobile.util.Lists.strings;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Sprints;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SprintDetailFragment extends Fragment implements LoaderCallbacks<Cursor> {

    protected final static String TAG = "SprintDetailFragment";
    protected Handler mHandler = new Handler();
    protected Activity mActivity;
    private Uri mContentUri;
    private Cursor mCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sprint_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get(ARGS_URI) != null) {
            mContentUri = (Uri) arguments.get(ARGS_URI);
        }

        setHasOptionsMenu(true);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        if (mContentUri != null) {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * {@inheritDoc} Query the {@link PlaceDetailsContentProvider} for the
     * Phone, Address, Rating, Reference, and Url of the selected venue. TODO
     * Expand the projection to include any other details you are recording in
     * the Place Detail Content Provider.
     */
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mActivity, mContentUri,
                strings(Sprints.SPRINT_SUMMARY), null, null, null);
    }

    /**
     * {@inheritDoc} When the Loader has completed, schedule an update of the
     * Fragment UI on the main application thread.
     */
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mCursor = data;
            final String sumamry = data.getString(data.getColumnIndex(Sprints.SPRINT_SUMMARY));
            // Update UI
            mHandler.post(new Runnable() {
                public void run() {
                    ((TextView) getView().findViewById(R.id.sprint_title)).setText(sumamry);
                    ((TextView) getView().findViewById(R.id.sprint_subtitle)).setText("subtitle "
                            + sumamry);
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onLoaderReset(Loader<Cursor> loader) {
        // mHandler.post(new Runnable() {
        // public void run() {
        // Clean UI data
        // }
        // });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                Log.d(TAG, "edit clicked");

                ContentResolver cr = mActivity.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(Sprints.SPRINT_SUMMARY,
                        mCursor.getString(mCursor.getColumnIndex(Sprints.SPRINT_SUMMARY)) + ">");
                cr.update(mContentUri, values, null, null);

                // startActivity(new Intent(Intent.ACTION_INSERT,
                // Sprints.CONTENT_URI));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

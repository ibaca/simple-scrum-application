
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.SprintTable;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SprintDetailFragment extends Fragment implements LoaderCallbacks<Cursor> {

    protected final static String TAG = "SprintDetailFragment";
    protected Handler mHandler = new Handler();
    protected Activity mActivity;
    private Uri mContentUri;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        if (mContentUri != null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            // New item (set default values)
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sprint_detail, container, false);

        Bundle arguments = getArguments();
        // TODO buscar donde esta la constante _uri!
        if (arguments != null && arguments.get("_uri") != null) {
            mContentUri = (Uri) arguments.get("_uri");
        }
        return view;
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
        String[] projection = new String[] {
                SprintTable.KEY_SUMMARY,
        };
        return new CursorLoader(mActivity, mContentUri, projection, null, null, null);
    }

    /**
     * {@inheritDoc} When the Loader has completed, schedule an update of the
     * Fragment UI on the main application thread.
     */
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            final String sumamry = data.getString(data.getColumnIndex(SprintTable.KEY_SUMMARY));
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
        mHandler.post(new Runnable() {
            public void run() {
                // ((TextView)
                // getView().findViewById(R.id.sprint_title)).setText("");
                // ((TextView)
                // getView().findViewById(R.id.sprint_subtitle)).setText("");
            }
        });
    }

}

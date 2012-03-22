
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ProjectEditFragment extends Fragment implements LoaderCallbacks<Cursor> {
    private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;

    private int mState;
    private Activity mActivity;
    private Uri mContentUri;
    private Intent mIntent;
    private String mAction;
    protected Handler mHandler = new Handler();
    private EditText title;
    private EditText description;
    private EditText summary;
    private EditText started;
    private EditText finished;
    private EditText company;
    private EditText license;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mIntent = mActivity.getIntent();
        mAction = mIntent.getAction();

        if (Intent.ACTION_EDIT.equals(mAction)) {
            mState = STATE_EDIT;
            getLoaderManager().initLoader(0, null, this);
        } else if (Intent.ACTION_INSERT.equals(mAction)) {
            mState = STATE_INSERT;
            // New item (set default values)
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ssa_project_edit, container, false);

        title = (EditText) view.findViewById(R.id.project_edit_title);
        summary = (EditText) view.findViewById(R.id.project_edit_summary);
        description = (EditText) view.findViewById(R.id.project_edit_description);
        started = (EditText) view.findViewById(R.id.project_edit_started);
        finished = (EditText) view.findViewById(R.id.project_edit_finished);
        company = (EditText) view.findViewById(R.id.project_edit_company);
        license = (EditText) view.findViewById(R.id.project_edit_license);

        started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment.newInstance(v.getContext(), started).show(
                        getActivity().getSupportFragmentManager(),
                        "Date Picker Dialog");
            }

        });

        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment.newInstance(v.getContext(), finished).show(
                        getActivity().getSupportFragmentManager(),
                        "Date Picker Dialog");
            }

        });

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get("_uri") != null) {
            mContentUri = (Uri) arguments.get("_uri");
        }

        // setHasOptionsMenu(true);
        return view;
    }

    // @Override
    // public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // inflater.inflate(R.menu.save_project_menu, menu);
    // super.onCreateOptionsMenu(menu, inflater);
    // }
    //
    // @Override
    // public boolean onOptionsItemSelected(MenuItem item) {
    // switch (item.getItemId()) {
    // case R.id.menu_save:
    // saveProject();
    // final Intent intent = new Intent(ACTION_VIEW,
    // Projects.CONTENT_URI);
    // startActivity(intent);
    // return true;
    // }
    // return onOptionsItemSelected(item);
    // }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                Projects.PROJECT_SUMMARY, Projects.PROJECT_NAME,
                Projects.PROJECT_DESCRIPTION, Projects.PROJECT_STARTED,
                Projects.PROJECT_CLOSE, Projects.PROJECT_COMPANY,
                Projects.PROJECT_LICENSE
        };
        return new CursorLoader(mActivity, mContentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            final String summary = data.getString(data.getColumnIndex(Projects.PROJECT_NAME));
            final String name = data.getString(data.getColumnIndex(Projects.PROJECT_SUMMARY));
            final String description = data.getString(data
                    .getColumnIndex(Projects.PROJECT_DESCRIPTION));
            final String started = data.getString(data.getColumnIndex(Projects.PROJECT_STARTED));
            final String close = data.getString(data.getColumnIndex(Projects.PROJECT_CLOSE));
            final String license = data.getString(data.getColumnIndex(Projects.PROJECT_LICENSE));
            final String company = data.getString(data.getColumnIndex(Projects.PROJECT_COMPANY));

            // Update UI
            mHandler.post(new Runnable() {
                public void run() {
                    ((TextView) getView().findViewById(R.id.project_edit_title)).setText(name);
                    ((TextView) getView().findViewById(R.id.project_edit_summary)).setText(summary);
                    ((TextView) getView().findViewById(R.id.project_edit_description))
                            .setText(description);
                    ((TextView) getView().findViewById(R.id.project_edit_started)).setText(started);
                    ((TextView) getView().findViewById(R.id.project_edit_finished)).setText(close);
                    ((TextView) getView().findViewById(R.id.project_edit_license)).setText(license);
                    ((TextView) getView().findViewById(R.id.project_edit_company)).setText(company);

                }
            });
        }

    }

    @Override
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

    public void saveProject() {

        Log.d(getClass().getSimpleName(), "Save Project");

        ContentResolver cr = mActivity.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(Projects.PROJECT_NAME, title.getText().toString());
        values.put(Projects.PROJECT_SUMMARY, summary.getText().toString());
        values.put(Projects.PROJECT_DESCRIPTION, description.getText().toString());
        values.put(Projects.PROJECT_STARTED, started.getText().toString());
        values.put(Projects.PROJECT_CLOSE, finished.getText().toString());
        values.put(Projects.PROJECT_COMPANY, company.getText().toString());
        values.put(Projects.PROJECT_LICENSE, license.getText().toString());

        try {
            if (mState == STATE_INSERT) {
                values.put(Projects.PROJECT_OPENED, Long.toString(System.currentTimeMillis()));
                cr.insert(Projects.CONTENT_URI, values);
            } else {
                cr.update(mContentUri, values, null, null);
            }
        } catch (NullPointerException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }

    }

}

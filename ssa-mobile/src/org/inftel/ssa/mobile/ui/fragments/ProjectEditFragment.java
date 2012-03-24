
package org.inftel.ssa.mobile.ui.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.ui.phone.ProjectListActivity;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        setHasOptionsMenu(true);
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

        return view;
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
                try {
                    saveProject();
                } catch (ParseException e) {
                    Log.w(getClass().getSimpleName(),
                            "Fallo inesperado guardando proyecto: " + e.getMessage(), e);
                }
                // getActivity().finish();
                if (mState == STATE_EDIT) {
                    startActivity(new Intent(Intent.ACTION_VIEW, mContentUri));
                } else {
                    startActivity(new Intent(getActivity(), ProjectListActivity.class));
                }

                return true;
        }
        return onOptionsItemSelected(item);
    }

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
            final String summary = data.getString(data.getColumnIndex(Projects.PROJECT_SUMMARY));
            final String name = data.getString(data.getColumnIndex(Projects.PROJECT_NAME));
            final String description = data.getString(data
                    .getColumnIndex(Projects.PROJECT_DESCRIPTION));
            final String started = data.getString(data.getColumnIndex(Projects.PROJECT_STARTED));
            final String close = data.getString(data.getColumnIndex(Projects.PROJECT_CLOSE));
            final String license = data.getString(data.getColumnIndex(Projects.PROJECT_LICENSE));
            final String company = data.getString(data.getColumnIndex(Projects.PROJECT_COMPANY));

            // Update UI
            mHandler.post(new Runnable() {
                public void run() {
                    populateView(summary, name, description, started, close, license, company);
                }

            });
        }

    }

    private void populateView(final String summary, final String name,
            final String description, final String started, final String close,
            final String license, final String company) {
        ((TextView) getView().findViewById(R.id.project_edit_title)).setText(name);
        ((TextView) getView().findViewById(R.id.project_edit_summary)).setText(summary);
        ((TextView) getView().findViewById(R.id.project_edit_description)).setText(description);
        ((TextView) getView().findViewById(R.id.project_edit_started)).setText(formatDate(started));
        ((TextView) getView().findViewById(R.id.project_edit_finished)).setText(formatDate(close));
        ((TextView) getView().findViewById(R.id.project_edit_license)).setText(license);
        ((TextView) getView().findViewById(R.id.project_edit_company)).setText(company);
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

    public void saveProject() throws ParseException {
        Log.d(getClass().getSimpleName(), "Save Project");

        ContentResolver cr = mActivity.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(Projects.PROJECT_NAME, title.getText().toString());
        values.put(Projects.PROJECT_SUMMARY, summary.getText().toString());
        values.put(Projects.PROJECT_DESCRIPTION, description.getText().toString());
        values.put(Projects.PROJECT_STARTED, secureEpochDate(started.getText().toString()));
        values.put(Projects.PROJECT_CLOSE, secureEpochDate(finished.getText().toString()));
        values.put(Projects.PROJECT_COMPANY, company.getText().toString());
        values.put(Projects.PROJECT_LICENSE, license.getText().toString());

        try {
            if (mState == STATE_INSERT) {
                values.put(Projects.SYNC_STATUS, SsaContract.STATUS_CREATED);
                values.put(Projects.PROJECT_OPENED, Long.toString(System.currentTimeMillis()));
                cr.insert(Projects.CONTENT_URI, values);
            } else {
                values.put(Projects.SYNC_STATUS, SsaContract.STATUS_DIRTY);
                cr.update(mContentUri, values, null, null);
            }
        } catch (NullPointerException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }

    }

    final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

    private String secureEpochDate(String insecureDate) {
        try {
            return String.valueOf(sdf.parse(insecureDate).getTime());
        } catch (Exception ignored) {
            return null; // FIXME jeje esto es poco 'elegante'
        }
    }

    private String formatDate(final String started) {
        try {
            return sdf.format(new Date(Long.parseLong(started)));
        } catch (Exception ignored) {
            return "no date"; // FIXME jeje esto es poco 'elegante'
        }
    }
}

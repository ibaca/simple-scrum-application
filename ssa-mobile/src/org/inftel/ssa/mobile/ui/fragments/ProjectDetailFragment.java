
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;
import org.inftel.ssa.mobile.contentproviders.SprintContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskContentProvider;
import org.inftel.ssa.mobile.contentproviders.UserContentProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class ProjectDetailFragment extends Fragment implements LoaderCallbacks<Cursor> {

    protected final static String TAG = "ProjectDetailFragment";

    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_INFORMATION = "information";
    private static final String TAG_LINKS = "links";

    protected Handler mHandler = new Handler();
    protected Activity mActivity;
    private Uri mContentUri;
    private String mProjectId;
    private String name;
    private String summary;
    private String description;
    private String labels;
    private String opened;
    private String closed;
    private String started;
    private String license;
    private String company;
    private String links;

    @Override
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

        View view = inflater.inflate(R.layout.ssa_project_details, container, false);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get("_uri") != null) {
            mContentUri = (Uri) arguments.get("_uri");
        }

        setHasOptionsMenu(true);

        // Handle sprints click
        view.findViewById(R.id.project_btn_sprints).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri sprintUri = SprintContentProvider.CONTENT_URI.buildUpon()
                        .appendQueryParameter("project_id", mProjectId).build();
                startActivity(new Intent(Intent.ACTION_VIEW, sprintUri));
            }
        });
        // Handle users click
        view.findViewById(R.id.project_btn_users).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri userUri = UserContentProvider.CONTENT_URI.buildUpon()
                        .appendQueryParameter("project_id", mProjectId).build();
                startActivity(new Intent(Intent.ACTION_VIEW, userUri));
            }
        });
        // Handle tasks click
        view.findViewById(R.id.project_btn_tasks).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri taskUri = TaskContentProvider.CONTENT_URI.buildUpon()
                        .appendQueryParameter("project_id", mProjectId).build();
                startActivity(new Intent(Intent.ACTION_VIEW, taskUri));
            }
        });

        TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup();
        setupDescriptionTab(view);
        setupLinksTab(view);
        setupInformationTab(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                ProjectTable.KEY_ID, ProjectTable.KEY_NAME,
                ProjectTable.KEY_SUMMARY, ProjectTable.KEY_DESCRIPTION,
                ProjectTable.KEY_OPENED, ProjectTable.KEY_STARTED,
                ProjectTable.KEY_CLOSE, ProjectTable.KEY_COMPANY,
                ProjectTable.KEY_LICENSE, ProjectTable.KEY_LABELS,
                ProjectTable.KEY_LINKS

        };
        return new CursorLoader(mActivity, mContentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {

            mProjectId = data.getString(data.getColumnIndex(ProjectTable.KEY_ID));
            name = data.getString(data.getColumnIndex(ProjectTable.KEY_NAME));
            summary = data.getString(data.getColumnIndex(ProjectTable.KEY_SUMMARY));
            description = data.getString(data.getColumnIndex(ProjectTable.KEY_DESCRIPTION));
            opened = data.getString(data.getColumnIndex(ProjectTable.KEY_OPENED));
            started = data.getString(data.getColumnIndex(ProjectTable.KEY_STARTED));
            closed = data.getString(data.getColumnIndex(ProjectTable.KEY_CLOSE));
            company = data.getString(data.getColumnIndex(ProjectTable.KEY_COMPANY));
            license = data.getString(data.getColumnIndex(ProjectTable.KEY_LICENSE));
            labels = data.getString(data.getColumnIndex(ProjectTable.KEY_LABELS));
            links = data.getString(data.getColumnIndex(ProjectTable.KEY_LINKS));

            // Update UI
            mHandler.post(new Runnable() {
                public void run() {

                    // Header
                    ((TextView) getView().findViewById(R.id.detail_title)).setText(name);
                    ((TextView) getView().findViewById(R.id.detail_subtitle)).setText(summary);

                    // Tab description
                    ((TextView) getView().findViewById(R.id.project_detail_description))
                            .setText(description);

                    // Tab links
                    // TODO Ver como llegan los links y aplicarle formato
                    String link = "<a href='http://www.masterinftel.uma.es/'>Master Inftel</a>";
                    TextView l = (TextView) getView().findViewById(R.id.empty_links);
                    l.setText(Html.fromHtml(link));
                    l.setMovementMethod(LinkMovementMethod.getInstance());
                    l.setLinksClickable(true);

                    // Tab Information
                    ((TextView) getView().findViewById(R.id.lblOpened)).setText(opened);
                    ((TextView) getView().findViewById(R.id.lblStarted)).setText(started);
                    ((TextView) getView().findViewById(R.id.lblClosed)).setText(closed);
                    ((TextView) getView().findViewById(R.id.lblCompany)).setText(company);
                    ((TextView) getView().findViewById(R.id.lblLicense)).setText(license);
                    ((TextView) getView().findViewById(R.id.lblLabels)).setText(labels);

                }
            });
        }

    }

    private void setupLinksTab(View view) {
        TabHost mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_LINKS)
                .setIndicator(buildIndicator(R.string.project_links, view))
                .setContent(R.id.tab_project_links));
    }

    private void setupInformationTab(View view) {
        TabHost mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_INFORMATION)
                .setIndicator(buildIndicator(R.string.project_information, view))
                .setContent(R.id.tab_project_information));
    }

    private void setupDescriptionTab(View view) {
        TabHost mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_DESCRIPTION)
                .setIndicator(buildIndicator(R.string.project_description, view))
                .setContent(R.id.tab_detail_project_description));
    }

    private View buildIndicator(int textRes, View view) {
        final TextView indicator = (TextView) getActivity().getLayoutInflater()
                .inflate(R.layout.tab_indicator,
                        (ViewGroup) view.findViewById(android.R.id.tabs), false);
        indicator.setText(textRes);
        return indicator;
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

    // public void fillProjectDetailsFragment(View view, Uri projectUri) {
    //
    // // The text view for task data, identified by its ID in the XML file.
    // mTxtName = (TextView) view.findViewById(R.id.lblProjectName);
    // mTxtSummary = (TextView) view.findViewById(R.id.lblProjectSummary);
    // mTxtDescription = (TextView)
    // view.findViewById(R.id.lblProjectDescription);
    //
    // // Get the note!
    // mCursor = getActivity().managedQuery(projectUri, PROJECTION, null, null,
    // null);
    // if (mCursor != null) {
    // // Requery in case something changed while paused (such as the
    // // title)
    //
    // mCursor.requery();
    //
    // int colName = mCursor.getColumnIndex(ProjectTable.KEY_NAME);
    // int colSummary = mCursor.getColumnIndex(ProjectTable.KEY_SUMMARY);
    // int colDescription =
    // mCursor.getColumnIndex(ProjectTable.KEY_DESCRIPTION);
    // String txt = "";
    // if (mCursor.moveToFirst()) {
    // txt = mCursor.getString(colName);
    // mTxtName.setText(txt);
    // txt = mCursor.getString(colSummary);
    // mTxtSummary.setText(txt);
    // txt = mCursor.getString(colDescription);
    // mTxtDescription.setText(txt);
    // }
    // mCursor.close();
    //
    // }
    // }
}


package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProjectDetailFragment extends Fragment {

    private static final String[] PROJECTION = new String[] {
            ProjectTable.KEY_ID, // 0
            ProjectTable.KEY_NAME, // 1
            ProjectTable.KEY_SUMMARY, // 2
            ProjectTable.KEY_DESCRIPTION, // 3
    };

    private TextView mTxtName;
    private TextView mTxtSummary;
    private TextView mTxtDescription;
    private Cursor mCursor;
    private View view;

    public static ProjectDetailFragment newInstance(Uri projectUri) {

        ProjectDetailFragment f = new ProjectDetailFragment();
        Bundle args = new Bundle();
        args.putString("projectUri", projectUri.toString());
        f.setArguments(args);
        return f;
    }

    public String getProjectUri() {
        return getArguments().getString("projectUri");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreateView");
        if (container == null) {
            return null;
        }

        view = inflater.inflate(R.layout.ssa_project_details, container, false);
        Log.d(getClass().getSimpleName(), "init: fillProjectDetailsFrament");
        fillProjectDetailsFragment(view, Uri.parse(getProjectUri()));
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void fillProjectDetailsFragment(View view, Uri projectUri) {

        // The text view for task data, identified by its ID in the XML file.
        mTxtName = (TextView) view.findViewById(R.id.lblProjectName);
        mTxtSummary = (TextView) view.findViewById(R.id.lblProjectSummary);
        mTxtDescription = (TextView) view.findViewById(R.id.lblProjectDescription);

        // Get the note!
        mCursor = getActivity().managedQuery(projectUri, PROJECTION, null, null, null);
        if (mCursor != null) {
            // Requery in case something changed while paused (such as the
            // title)

            mCursor.requery();

            int colName = mCursor.getColumnIndex(ProjectTable.KEY_NAME);
            int colSummary = mCursor.getColumnIndex(ProjectTable.KEY_SUMMARY);
            int colDescription = mCursor.getColumnIndex(ProjectTable.KEY_DESCRIPTION);
            String txt = "";
            if (mCursor.moveToFirst()) {
                txt = mCursor.getString(colName);
                mTxtName.setText(txt);
                txt = mCursor.getString(colSummary);
                mTxtSummary.setText(txt);
                txt = mCursor.getString(colDescription);
                mTxtDescription.setText(txt);
            }
            mCursor.close();

        }
    }
}

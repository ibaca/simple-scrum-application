
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.ProjectTable;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        if (container == null) {
            return null;
        }
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.ssa_project_details, container, false);
        // TextView text = (TextView) view.findViewById(R.id.TextView01);
        // text.setText(Information.TEXT[getShownIndex()]);
        fillProjectDetailsFragment(view, Uri.parse(getProjectUri()));
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
            // Make sure we are at the one and only row in the cursor.
            mCursor.moveToFirst();

            String txt = "";
            txt = mCursor.getString(mCursor.getColumnIndex(ProjectTable.KEY_NAME));
            mTxtName.setText(txt);
            txt = mCursor.getString(mCursor.getColumnIndex(ProjectTable.KEY_SUMMARY));
            mTxtSummary.setText(txt);
            txt = mCursor.getString(mCursor.getColumnIndex(ProjectTable.KEY_DESCRIPTION));
            mTxtDescription.setText(txt);

        }
    }
}

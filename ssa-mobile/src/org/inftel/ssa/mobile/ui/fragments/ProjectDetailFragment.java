
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.ui.ProjectActivity.Information;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProjectDetailFragment extends Fragment {

    public static ProjectDetailFragment newInstance(int index) {

        ProjectDetailFragment f = new ProjectDetailFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.ssa_project_details, container, false);
        TextView text = (TextView) view.findViewById(R.id.TextView01);
        text.setText(Information.TEXT[getShownIndex()]);
        return view;
    }
}

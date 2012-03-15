
package org.inftel.ssa.mobile.ui.fragments;

import static android.content.Intent.ACTION_VIEW;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.SprintContentProvider;
import org.inftel.ssa.mobile.util.AnalyticsUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DashboardFragment extends Fragment {

    public void fireTrackerEvent(String label) {
        AnalyticsUtils.getInstance(getActivity()).trackEvent(
                "Home Screen Dashboard", "Click", label, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container);

        // Attach event handlers
        root.findViewById(R.id.home_btn_sprints).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fireTrackerEvent("Sprints");
                // Launch sessions list
                final Intent intent = new Intent(ACTION_VIEW, SprintContentProvider.CONTENT_URI);
                intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.title_sprints));
                startActivity(intent);

            }
        });

        return root;
    }
}

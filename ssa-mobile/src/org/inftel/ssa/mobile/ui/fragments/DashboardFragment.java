
package org.inftel.ssa.mobile.ui.fragments;

import static android.content.Intent.ACTION_VIEW;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.provider.SsaContract.Sprints;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;
import org.inftel.ssa.mobile.provider.SsaContract.Users;
import org.inftel.ssa.mobile.ui.phone.AboutActivity;
import org.inftel.ssa.mobile.ui.phone.PreferencesActivity;
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
                final Intent intent = new Intent(ACTION_VIEW, Sprints.CONTENT_URI);
                intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.title_sprints));
                startActivity(intent);

            }
        });

        // Attach event handlers
        root.findViewById(R.id.home_btn_tasks).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fireTrackerEvent("Tasks");
                // Launch sessions list
                final Intent intent = new Intent(ACTION_VIEW, Tasks.CONTENT_URI);
                intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.title_task));
                startActivity(intent);

            }
        });

        // Attach event handlers
        root.findViewById(R.id.home_btn_users).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fireTrackerEvent("Users");
                // Launch sessions list
                final Intent intent = new Intent(ACTION_VIEW, Users.CONTENT_URI);
                intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.title_task));
                startActivity(intent);

            }
        });

        // Attach event handlers
        root.findViewById(R.id.home_btn_projects).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fireTrackerEvent("Projects");
                // Launch sessions list
                final Intent intent = new Intent(ACTION_VIEW, Projects.CONTENT_URI);
                intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.title_projects));
                startActivity(intent);

            }
        });

        // Attach event handlers
        root.findViewById(R.id.home_btn_preferences).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fireTrackerEvent("Preferences");

                startActivity(new Intent(getActivity(), PreferencesActivity.class));
            }
        });

        // Attach event handlers
        root.findViewById(R.id.home_btn_about).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fireTrackerEvent("About");

                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });
        return root;
    }
}

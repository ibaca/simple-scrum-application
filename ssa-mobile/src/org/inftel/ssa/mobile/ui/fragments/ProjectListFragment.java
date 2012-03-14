
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.ui.ProjectActivity;
import org.inftel.ssa.mobile.ui.ProjectActivity.Information;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProjectListFragment extends ListFragment {

    int textoActual = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.ssa_project_list, android.R.id.text1,
                Information.TITLES));

        if (savedInstanceState != null) {
            textoActual = savedInstanceState.getInt("currentChoice", 0);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentChoice", textoActual);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ((ProjectActivity) getActivity()).selectDetail(position);
    }

}

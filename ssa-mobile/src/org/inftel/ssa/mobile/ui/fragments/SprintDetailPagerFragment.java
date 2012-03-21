
package org.inftel.ssa.mobile.ui.fragments;

import static org.inftel.ssa.mobile.SsaConstants.EXTRA_LIST_CURSOR_POSITION;
import static org.inftel.ssa.mobile.SsaConstants.EXTRA_LIST_CURSOR_URI;
import static org.inftel.ssa.mobile.ui.BaseActivity.ARGS_URI;
import static org.inftel.ssa.mobile.util.Lists.strings;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Sprints;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SprintDetailPagerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);
        Bundle arguments = getArguments();

        // Optional page arguments
        Cursor cursor = null;
        Integer position = 0;

        // Si existen datos para generar cursor, se usa PageView
        if (hasCursorData(arguments)) {
            Uri listUri = (Uri) arguments.get(EXTRA_LIST_CURSOR_URI);
            position = (int) arguments.getInt(EXTRA_LIST_CURSOR_POSITION);
            ContentResolver cr = getActivity().getContentResolver();
            cursor = cr.query(listUri, strings(Sprints._ID), null, null, null);
        }

        // Populate ViewPager
        ViewPager pager = (ViewPager) view.findViewById(R.id.fragment_viewpager);
        pager.setAdapter(new SprintCursorPagerAdapter(getFragmentManager(), getArguments(), cursor));
        pager.setCurrentItem(position);

        setHasOptionsMenu(true);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private static class SprintCursorPagerAdapter extends FragmentStatePagerAdapter {

        private Bundle mArguments;
        private Cursor mCursor;

        public SprintCursorPagerAdapter(FragmentManager fm, Bundle arguments, Cursor cursor) {
            super(fm);
            mArguments = arguments;
            mCursor = cursor;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new SprintDetailFragment();
            if (mCursor != null) {
                Bundle arguments = (Bundle) mArguments.clone();
                mCursor.moveToPosition(position);
                String sprintId = mCursor.getString(mCursor.getColumnIndex(Sprints._ID));
                arguments.putParcelable(ARGS_URI, Sprints.buildSprintUri(sprintId));
                fragment.setArguments(arguments);
            } else {
                fragment.setArguments(mArguments);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return (mCursor != null) ? mCursor.getCount() : 1;
        }

    }

    private boolean hasCursorData(Bundle arguments) {
        return arguments != null
                && arguments.get(EXTRA_LIST_CURSOR_URI) != null
                && arguments.get(EXTRA_LIST_CURSOR_POSITION) != null;
    }

}

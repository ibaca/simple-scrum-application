
package org.inftel.ssa.mobile.ui.fragments;

import static org.inftel.ssa.mobile.SsaConstants.EXTRA_LIST_CURSOR_URI;
import static org.inftel.ssa.mobile.ui.BaseActivity.ARGS_URI;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.SsaConstants;
import org.inftel.ssa.mobile.provider.SsaContract.Sprints;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SprintDetailPagerFragment extends Fragment {

    protected final static String TAG = "SprintDetailFragment";
    protected Handler mHandler = new Handler();
    protected Activity mActivity;
    private Uri mContentUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_sprint_pager, container, false);
    	
        ViewPager pager = (ViewPager) view.findViewById(R.id.fragment_sprint_viewpager);
        pager.setAdapter(new CursorPagerAdapter(getFragmentManager(), getArguments()));
        pager.setCurrentItem(0);
        
        Bundle arguments = getArguments();
        // TODO buscar donde esta la constante _uri!
        if (arguments != null && arguments.get(EXTRA_LIST_CURSOR_URI) != null) {
            mContentUri = (Uri) arguments.get(EXTRA_LIST_CURSOR_URI);
        }

        setHasOptionsMenu(true);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    
    private static class CursorPagerAdapter extends FragmentPagerAdapter {
    	
    	private Bundle mArguments;
    	
    	public CursorPagerAdapter(FragmentManager fm, Bundle arguments) {
			super(fm);
			mArguments = arguments;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new SprintDetailFragment();
			fragment.setArguments(mArguments);
			return fragment;
		}

		@Override
		public int getCount() {
			return 5;
		}

    
    }

}

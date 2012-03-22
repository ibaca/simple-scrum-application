package org.inftel.ssa.mobile.ui.fragments;

import static android.content.Intent.ACTION_VIEW;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.provider.SsaContract.Projects;
import org.inftel.ssa.mobile.provider.SsaContract.Tasks;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class TaskDetailFragment extends Fragment implements
		LoaderCallbacks<Cursor> {

	protected final static String TAG = "TaskDetailFragment";

	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_INFORMATION = "information";

	protected Handler mHandler = new Handler();
	protected Activity mActivity;
	private Uri mContentUri;
	private String mTaskId;
	private String mSummary;
	private String mDescription;
	private String mEstimated;
	private String mPriority;
	private String mSprintId;
	private String mStatus;
	private String mBeginDate;
	private String mEndDate;
	private String mBurned;
	private String mRemaining;
	private String mComments;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.ssa_task_details, container,
				false);

		Bundle arguments = getArguments();
		if (arguments != null && arguments.get("_uri") != null) {
			mContentUri = (Uri) arguments.get("_uri");
		}

		setHasOptionsMenu(true);

		TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup();
		setupDescriptionTab(view);
		setupInformationTab(view);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * {@inheritDoc} Query the {@link PlaceDetailsContentProvider} for the
	 * Phone, Address, Rating, Reference, and Url of the selected venue. TODO
	 * Expand the projection to include any other details you are recording in
	 * the Place Detail Content Provider.
	 */
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		/*
		 * String[] projection = new String[] { SprintTable.KEY_SUMMARY, };
		 */
		String[] projection = new String[] { Tasks._ID, // 0
				Tasks.TASK_SUMMARY, // 1
				Tasks.TASK_DESCRIPTION, // 2
				Tasks.TASK_ESTIMATED, // 3
				Tasks.TASK_PRIORITY, // 4
				Tasks.TASK_STATUS, // 5
				Tasks.TASK_BEGINDATE, // 6
				Tasks.TASK_ENDDATE, // 7
				Tasks.TASK_BURNED, // 8
				Tasks.TASK_REMAINING, // 9
				Tasks.TASK_COMMENTS, // 10
		};

		return new CursorLoader(mActivity, mContentUri, projection, null, null,
				null);
	}

	/**
	 * {@inheritDoc} When the Loader has completed, schedule an update of the
	 * Fragment UI on the main application thread.
	 */
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.moveToFirst()) {

			mTaskId = data.getString(data.getColumnIndex(Tasks._ID));
			mSummary = data.getString(data.getColumnIndex(Tasks.TASK_SUMMARY));
			mDescription = data.getString(data
					.getColumnIndex(Tasks.TASK_DESCRIPTION));
			mEstimated = data.getString(data
					.getColumnIndex(Tasks.TASK_ESTIMATED));
			mPriority = data
					.getString(data.getColumnIndex(Tasks.TASK_PRIORITY));
			// mSprint =
			// data.getString(data.getColumnIndex(Tasks.TASK_SPRINT_ID));
			mStatus = data.getString(data.getColumnIndex(Tasks.TASK_STATUS));
			mBeginDate = data.getString(data
					.getColumnIndex(Tasks.TASK_BEGINDATE));
			mEndDate = data.getString(data.getColumnIndex(Tasks.TASK_ENDDATE));
			mBurned = data.getString(data.getColumnIndex(Tasks.TASK_BURNED));
			mRemaining = data.getString(data
					.getColumnIndex(Tasks.TASK_REMAINING));
			mComments = data
					.getString(data.getColumnIndex(Tasks.TASK_COMMENTS));

			// Update UI
			mHandler.post(new Runnable() {
				public void run() {

					// Header
					((TextView) getView().findViewById(R.id.detail_title))
							.setText(mSummary);

					// Tab description
					((TextView) getView().findViewById(
							R.id.task_detail_description))
							.setText(mDescription);

					// Tab Information
					((TextView) getView().findViewById(R.id.lblEstimated))
							.setText(mEstimated);
					((TextView) getView().findViewById(R.id.lblPriority))
							.setText(mPriority);
					((TextView) getView().findViewById(R.id.lblStatus))
							.setText(mStatus);
					((TextView) getView().findViewById(R.id.lblBeginDate))
							.setText(mBeginDate);
					((TextView) getView().findViewById(R.id.lblEndDate))
							.setText(mEndDate);
					((TextView) getView().findViewById(R.id.lblBurned))
							.setText(mBurned);
					((TextView) getView().findViewById(R.id.lblRemaining))
							.setText(mRemaining);
					((TextView) getView().findViewById(R.id.lblComments))
							.setText(mComments);
				}
			});
		}
	}

	private void setupInformationTab(View view) {
		TabHost mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		mTabHost.addTab(mTabHost.newTabSpec(TAG_INFORMATION)
				.setIndicator(buildIndicator(R.string.task_information, view))
				.setContent(R.id.tab_task_information));
	}

	private void setupDescriptionTab(View view) {
		TabHost mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		mTabHost.addTab(mTabHost.newTabSpec(TAG_DESCRIPTION)
				.setIndicator(buildIndicator(R.string.task_description, view))
				.setContent(R.id.tab_task_description));
	}

	private View buildIndicator(int textRes, View view) {
		final TextView indicator = (TextView) getActivity()
				.getLayoutInflater()
				.inflate(R.layout.tab_indicator,
						(ViewGroup) view.findViewById(android.R.id.tabs), false);
		indicator.setText(textRes);
		return indicator;
	}

	/**
	 * {@inheritDoc}
	 */
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

}

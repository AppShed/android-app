package com.appshed.appstore.activities;

import android.os.Bundle;

import com.appshed.appstore.R;
import com.appshed.appstore.fragments.AppsFragment;
import com.rightutils.activities.RightFragmentActivityNew;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class AppsActivity extends RightFragmentActivityNew {

	private static final String TAG = AppsActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initActivity(R.id.fragment_container, AppsFragment.newInstance());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apps);
	}
}

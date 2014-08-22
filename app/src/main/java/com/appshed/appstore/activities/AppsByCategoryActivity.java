package com.appshed.appstore.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.appshed.appstore.R;
import com.appshed.appstore.fragments.AppsByCategoryFragment;
import com.rightutils.rightutils.activities.RightFragmentActivityNew;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class AppsByCategoryActivity extends RightFragmentActivityNew {

	private static final String TAG = AppsByCategoryActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int categoryIcon = getIntent().getExtras().getInt(Integer.class.getSimpleName());
		String categoryName = getIntent().getExtras().getString(String.class.getSimpleName());
		initActivity(R.id.fragment_container, AppsByCategoryFragment.newInstance(categoryIcon, categoryName));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apps);
	}

	@Override
	public void onBackPressed() {
		Fragment lastFragment = getLastFragment();
		if (lastFragment instanceof AppsByCategoryFragment) {
			///TODO think about that
		} else {
			super.onBackPressed();
		}
	}
}

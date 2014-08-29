package com.appshed.appstore.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.appshed.appstore.R;
import com.appshed.appstore.fragments.AppsByCategoryFragment;
import com.appshed.appstore.fragments.OnBackPressed;
import com.rightutils.rightutils.activities.RightFragmentActivityNew;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class AppsByCategoryActivity extends RightFragmentActivityNew {

	private static final String TAG = AppsByCategoryActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int categoryIcon = getIntent().getIntExtra(Integer.class.getSimpleName(), 0);
		String categoryName = getIntent().getStringExtra(String.class.getSimpleName());
		int miniIcon = getIntent().getIntExtra(Long.class.getSimpleName(), 0);
		initActivity(R.id.fragment_container, AppsByCategoryFragment.newInstance(categoryIcon, categoryName, miniIcon));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apps);
	}

	@Override
	public void onBackPressed() {
		Fragment lastFragment = getLastFragment();
		if (lastFragment instanceof OnBackPressed) {
			boolean result = ((OnBackPressed) lastFragment).onBackPressed();
			if (result) {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}
}

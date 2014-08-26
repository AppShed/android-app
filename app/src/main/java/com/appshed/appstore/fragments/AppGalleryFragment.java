package com.appshed.appstore.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appshed.appstore.R;
import com.viewpagerindicator.UnderlinePageIndicator;

/**
 * Created by Anton Maniskevich on 8/20/14.
 */
public class AppGalleryFragment extends Fragment implements View.OnClickListener{

	private FragmentPagerAdapter adapter;
	private ViewPager pager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_app_gallery, null);
		adapter = new GoogleMusicAdapter(getFragmentManager());
		pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setAdapter(adapter);

		UnderlinePageIndicator indicator = (UnderlinePageIndicator)view.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setSelectedColor(0xFFFFFF00);
		indicator.setBackgroundColor(0xFF305EDA);
		indicator.setMinimumHeight(20);
		indicator.setFades(false);

		view.findViewById(R.id.rbtn_featured).setOnClickListener(this);
		view.findViewById(R.id.rbtn_categories).setOnClickListener(this);
		view.findViewById(R.id.rbtn_search).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rbtn_featured:
				pager.post(new Runnable() {
					@Override
					public void run() {
						pager.setCurrentItem(0);
					}
				});
				break;
			case R.id.rbtn_categories:
				pager.post(new Runnable() {
					@Override
					public void run() {
						pager.setCurrentItem(1);
					}
				});
				break;
			case R.id.rbtn_search:
				pager.post(new Runnable() {
					@Override
					public void run() {
						pager.setCurrentItem(2);
					}
				});
				break;
		}
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return FeaturedFragment.newInstance();
				case 1:
					return CategoriesFragment.newInstance();
				case 2:
					return SearchFragment.newInstance();
			}
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}
	}
}

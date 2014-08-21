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
public class AppGalleryFragment extends Fragment {

	private FragmentPagerAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_app_gallery, null);
		adapter = new GoogleMusicAdapter(getFragmentManager());
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setAdapter(adapter);

		UnderlinePageIndicator indicator = (UnderlinePageIndicator)view.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setSelectedColor(0xFFFFFF00);
		indicator.setBackgroundColor(0xFF305EDA);
		indicator.setMinimumHeight(20);
		indicator.setFades(false);
		return view;
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 1:
					return CategoriesFragment.newInstance();
			}
			return new PlaceholderFragment();
		}

		@Override
		public int getCount() {
			return 3;
		}
	}
}

package com.appshed.appstore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.appshed.appstore.R;
import com.appshed.appstore.activities.MainActivityNew;
import com.appshed.appstore.utils.SystemUtils;
import com.rightutils.rightutils.collections.RightList;
import com.viewpagerindicator.UnderlinePageIndicator;

/**
 * Created by Anton Maniskevich on 8/20/14.
 */
public class AppStoreFragment extends Fragment implements View.OnClickListener{

	private FragmentPagerAdapter adapter;
	private ViewPager pager;
	private View tileView;
	private RightList<Fragment> fragments;

	public static AppStoreFragment newInstance() {
		AppStoreFragment fragment = new AppStoreFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_app_store, null);
		fragments = RightList.asRightList(
				FeaturedFragment.newInstance(),
				CategoriesFragment.newInstance(),
				SearchFragment.newInstance()
		);
		adapter = new ViewPagerAdapter(getChildFragmentManager());
		pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(3);

		UnderlinePageIndicator indicator = (UnderlinePageIndicator)view.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {
			}

			@Override
			public void onPageSelected(int i) {
				if (i == 0 || i == 2) {
					tileView.setVisibility(View.VISIBLE);
				} else {
					tileView.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});
		indicator.setSelectedColor(0xFFFFFF00);
		indicator.setBackgroundColor(0xFF305EDA);
		indicator.setMinimumHeight(20);
		indicator.setFades(false);

		view.findViewById(R.id.rbtn_featured).setOnClickListener(this);
		view.findViewById(R.id.rbtn_categories).setOnClickListener(this);
		view.findViewById(R.id.rbtn_search).setOnClickListener(this);
		view.findViewById(R.id.img_menu).setOnClickListener(this);
		tileView = view.findViewById(R.id.img_tile);
		tileView.setOnClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
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
			case R.id.img_menu:
				((MainActivityNew) getActivity()).toggleMenu();
				break;
			case R.id.img_tile:
				if (SystemUtils.cache.getAppLayout() == R.layout.item_tile_app) {
					SystemUtils.cache.setAppLayout(R.layout.item_app);
				} else {
					SystemUtils.cache.setAppLayout(R.layout.item_tile_app);
				}
				SystemUtils.saveCache(getActivity());
				Fragment currentFragment = fragments.get(pager.getCurrentItem());
				if (currentFragment instanceof FeaturedFragment) {
					((FeaturedFragment) currentFragment).updateListView();
				}
				if (currentFragment instanceof SearchFragment) {
					((SearchFragment) currentFragment).updateListView();
				}
				break;
		}
	}

	class ViewPagerAdapter extends FragmentPagerAdapter {
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
//			switch (position) {
//				case 0:
//					return FeaturedFragment.newInstance();
//				case 1:
//					return CategoriesFragment.newInstance();
//				case 2:
//					return SearchFragment.newInstance();
//			}
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return 3;
		}

	}
}

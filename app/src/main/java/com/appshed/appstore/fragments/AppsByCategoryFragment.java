package com.appshed.appstore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.appshed.appstore.R;
import com.appshed.appstore.activities.MainActivityNew;
import com.appshed.appstore.dialogs.AppDetailDialog;
import com.appshed.appstore.adapters.AppAdapter;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.tasks.RetrieveCategoriesApps;
import com.appshed.appstore.utils.SystemUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rightutils.rightutils.collections.RightList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class AppsByCategoryFragment extends Fragment implements View.OnClickListener{

	private static final String TAG = AppsByCategoryFragment.class.getSimpleName();

	private RightList<App> apps = new RightList<App>();
	private PullToRefreshListView pullToRefreshListView;
	private ListView actualListView;
	private AppAdapter adapter;
	private View progressBar;
	private int bgDrawable;
	private int miniIcon;
	private String category;


	public static AppsByCategoryFragment newInstance(int bgDrawable, String category, int miniIcon) {
		AppsByCategoryFragment fragment = new AppsByCategoryFragment();
		fragment.setBgDrawable(bgDrawable);
		fragment.setCategory(category);
		fragment.setMiniIcon(miniIcon);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_apps_by_category, null);
		((ImageView) view.findViewById(R.id.img_category_icon)).setImageResource(bgDrawable);
		view.findViewById(R.id.img_menu).setOnClickListener(this);
		view.findViewById(R.id.img_tile).setOnClickListener(this);
		if (miniIcon != 0) {
			((ImageView) view.findViewById(R.id.img_mini_icon)).setImageResource(miniIcon);
		}
		((TextView) view.findViewById(R.id.txt_category)).setText(category);
		progressBar = view.findViewById(R.id.progress_bar);
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				RetrieveCategoriesApps retrieveCategoriesApps = new RetrieveCategoriesApps(getActivity(), null, AppsByCategoryFragment.this).setCategory(category);
				if (!apps.isEmpty()) {
					if (refreshView.getCurrentMode().showHeaderLoadingLayout()) {
						retrieveCategoriesApps.setSinceId(apps.get(0).getId());
					} else {
						retrieveCategoriesApps.setMaxId(apps.get(apps.size()-1).getId());
					}
				}
				retrieveCategoriesApps.execute();
			}
		});
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

		actualListView = pullToRefreshListView.getRefreshableView();
		registerForContextMenu(actualListView);

		adapter = new AppAdapter(getActivity(), apps, SystemUtils.cache.getAppLayout());
		actualListView.setAdapter(adapter);
		actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(getActivity(), AppDetailDialog.class).putExtra(App.class.getSimpleName(), apps.get(position-1)));
			}
		});
		if (apps.isEmpty()) {
			new RetrieveCategoriesApps(getActivity(), progressBar, AppsByCategoryFragment.this).setCategory(category).execute();
		}
		return view;
	}

	public void addApps(final RightList<App> newApps) {
		if (newApps.size() > 0) {
			apps.addAll(newApps);
			Collections.sort(this.apps, new Comparator<App>() {
				@Override
				public int compare(App lhs, App rhs) {
					return (int) (rhs.getId() - lhs.getId());
				}
			});
		}
		pullToRefreshListView.post(new Runnable() {
			@Override
			public void run() {
				if (!newApps.isEmpty()) {
					adapter.notifyDataSetChanged();
				}
				pullToRefreshListView.onRefreshComplete();
			}
		});
	}

	public void setBgDrawable(int bgDrawable) {
		this.bgDrawable = bgDrawable;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setMiniIcon(int miniIcon) {
		this.miniIcon = miniIcon;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
				adapter.changeLayout(SystemUtils.cache.getAppLayout());
				break;
		}
	}
}

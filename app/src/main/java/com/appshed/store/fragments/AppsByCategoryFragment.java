package com.appshed.store.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appshed.store.R;
import com.appshed.store.activities.MainActivityNew;
import com.appshed.store.adapters.AppAdapter;
import com.appshed.store.dialogs.AppDetailDialog;
import com.appshed.store.entities.App;
import com.appshed.store.tasks.RetrieveCategoriesApps;
import com.appshed.store.tasks.RetrieveCategorySearchApps;
import com.appshed.store.utils.SystemUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rightutils.rightutils.collections.RightList;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class AppsByCategoryFragment extends Fragment implements View.OnClickListener {

	private static final String TAG = AppsByCategoryFragment.class.getSimpleName();

	private RightList<App> apps = new RightList<App>();
	private PullToRefreshListView pullToRefreshListView;
	private ListView actualListView;
	private AppAdapter adapter;
	private View progressBar;
	private int bgDrawable;
	private int miniIcon;
	private String category;
	private View searchContainer;
	private View showSearchBtn;
	private View hideSearchBtn;
	private EditText search;
	private View emptyList;


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
		(showSearchBtn = view.findViewById(R.id.img_show_search)).setOnClickListener(this);
		(hideSearchBtn = view.findViewById(R.id.img_hide_search)).setOnClickListener(this);
		view.findViewById(R.id.img_search_btn).setOnClickListener(this);
		emptyList = view.findViewById(R.id.img_empty_list);
		search = (EditText) view.findViewById(R.id.f_search);
		search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					doSearch();
					return true;
				}
				return false;
			}
		});
		if (miniIcon != 0) {
			((ImageView) view.findViewById(R.id.img_mini_icon)).setImageResource(miniIcon);
		}
		searchContainer = view.findViewById(R.id.search_inner_container);
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
						retrieveCategoriesApps.setMaxId(apps.get(apps.size() - 1).getId());
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
				startActivity(new Intent(getActivity(), AppDetailDialog.class).putExtra(App.class.getSimpleName(), apps.get(position - 1)));
			}
		});
		if (apps.isEmpty()) {
			new RetrieveCategoriesApps(getActivity(), progressBar, AppsByCategoryFragment.this).setCategory(category).execute();
		}
		showOrHideEmptyList();
		return view;
	}

	public void addApps(final RightList<App> newApps) {
		if (newApps.size() > 0) {
			apps.addAll(newApps);
			showOrHideEmptyList();
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
			case R.id.img_show_search:
				showSearch();
				break;
			case R.id.img_hide_search:
				hideSearch();
				break;
			case R.id.img_search_btn:
				doSearch();
				break;
		}
	}

	public void showSearch() {
		showSearchBtn.setVisibility(View.INVISIBLE);
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
		searchContainer.setVisibility(View.VISIBLE);
		apps.clear();
		adapter.notifyDataSetChanged();
		showOrHideEmptyList();
	}

	public void hideSearch() {
		showSearchBtn.setVisibility(View.VISIBLE);
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		searchContainer.setVisibility(View.GONE);
		apps.clear();
		adapter.notifyDataSetChanged();
		showOrHideEmptyList();
		search.setText("");
		new RetrieveCategoriesApps(getActivity(), progressBar, AppsByCategoryFragment.this).setCategory(category).execute();
	}

	public void doSearch() {
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.search.getWindowToken(), 0);
		String query = search.getText().toString().trim();
		if (!query.isEmpty()) {
			new RetrieveCategorySearchApps(getActivity(), progressBar, AppsByCategoryFragment.this)
					.setCategory(category)
					.setQuery(query)
					.execute();
		}
	}

	private void showOrHideEmptyList() {
		if (apps.isEmpty()) {
			emptyList.setVisibility(View.VISIBLE);
		} else {
			emptyList.setVisibility(View.GONE);
		}
	}
}

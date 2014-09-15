package com.appshed.appstore.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appshed.appstore.R;
import com.appshed.appstore.activities.LaunchActivity;
import com.appshed.appstore.activities.MainActivityNew;
import com.appshed.appstore.dialogs.AppDetailDialog;
import com.appshed.appstore.adapters.AppAdapter;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.tasks.RetrieveSavedApps;
import com.rightutils.rightutils.collections.RightList;

/**
 * Created by Anton Maniskevich on 9/1/14.
 */
public class MySavedAppsFragment extends Fragment implements View.OnClickListener {

	private static final String TAG = MySavedAppsFragment.class.getSimpleName();
	private ListView listView;
	private View progressBar;
	private AppAdapter adapter;
	private View emptyList;

	public static MySavedAppsFragment newInstance() {
		MySavedAppsFragment fragment = new MySavedAppsFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_mysaved_apps, null);
		view.findViewById(R.id.img_menu).setOnClickListener(this);
		listView = (ListView) view.findViewById(R.id.list_view);
		emptyList = view.findViewById(R.id.img_empty_list);
		progressBar = view.findViewById(R.id.progress_bar);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_menu:
				((MainActivityNew) getActivity()).toggleMenu();
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		new RetrieveSavedApps(getActivity(), progressBar, MySavedAppsFragment.this).execute();
	}

	public void addApps(RightList<App> apps) {
		if (apps.isEmpty()) {
			emptyList.setVisibility(View.VISIBLE);
		} else {
			emptyList.setVisibility(View.GONE);
		}
		adapter = new AppAdapter(getActivity(), apps, R.layout.item_app);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent browserIntent = new Intent(getActivity(), LaunchActivity.class);
				browserIntent.setData(Uri.parse("/"+adapter.getItem(position).getId()));
				startActivity(browserIntent);
			}
		});
	}


}

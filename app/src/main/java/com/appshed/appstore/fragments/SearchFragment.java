package com.appshed.appstore.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.appshed.appstore.R;
import com.appshed.appstore.adapters.AppAdapter;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.tasks.SearchApps;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.utils.RightUtils;

/**
 * Created by Anton Maniskevich on 8/22/14.
 */
public class SearchFragment extends Fragment {

	private ListView listView;
	private EditText search;

	public static SearchFragment newInstance() {
		SearchFragment fragment = new SearchFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, null);
		listView = (ListView) view.findViewById(R.id.list_view);
		search = (EditText) view.findViewById(R.id.f_search);
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!s.toString().trim().isEmpty()) {
					new SearchApps(getActivity(), null, SearchFragment.this).setQuery(s.toString().trim()).execute();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		return view;
	}

	public void addApps(RightList<App> apps) {
		final AppAdapter adapter = new AppAdapter(getActivity(), apps);
		listView.setAdapter(adapter);
	}
}

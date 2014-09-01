package com.appshed.appstore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appshed.appstore.R;
import com.appshed.appstore.activities.MainActivity;

/**
 * Created by Anton Maniskevich on 9/1/14.
 */
public class MySavedAppsFragment extends Fragment implements View.OnClickListener {

	public static MySavedAppsFragment newInstance() {
		MySavedAppsFragment fragment = new MySavedAppsFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_mysaved_apps, null);
		view.findViewById(R.id.img_menu).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_menu:
				((MainActivity) getActivity()).toggleMenu();
				break;
		}
	}
}

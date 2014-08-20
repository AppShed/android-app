package com.appshed.appstore.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appshed.appstore.R;

/**
 * Created by Anton Maniskevich on 25.07.2014.
 */
@Deprecated
public class PlaceholderFragment extends Fragment {

	public static PlaceholderFragment newInstance() {
		PlaceholderFragment fragment = new PlaceholderFragment();
		return fragment;
	}

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(final View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
}
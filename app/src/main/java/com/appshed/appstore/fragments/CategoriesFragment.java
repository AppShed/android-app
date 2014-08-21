package com.appshed.appstore.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appshed.appstore.R;

/**
 * Created by Anton Maniskevich on 8/21/14.
 */
public class CategoriesFragment extends Fragment {

	public static CategoriesFragment newInstance() {
		CategoriesFragment fragment = new CategoriesFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_categories, null);

		return view;
	}
}

package com.appshed.appstore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appshed.appstore.R;
import com.appshed.appstore.activities.AppsByCategoryActivity;
import com.appshed.appstore.utils.SystemUtils;

/**
 * Created by Anton Maniskevich on 8/21/14.
 */
public class CategoriesFragment extends Fragment implements View.OnClickListener{

	public static CategoriesFragment newInstance() {
		CategoriesFragment fragment = new CategoriesFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_categories, null);
		view.findViewById(R.id.general_container).setOnClickListener(this);
		view.findViewById(R.id.education_container).setOnClickListener(this);
		view.findViewById(R.id.business_container).setOnClickListener(this);
		view.findViewById(R.id.fun_container).setOnClickListener(this);
		view.findViewById(R.id.events_container).setOnClickListener(this);
		view.findViewById(R.id.other_container).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), AppsByCategoryActivity.class);
		switch (v.getId()) {
			case R.id.general_container:
				intent.putExtra(Integer.class.getSimpleName(), R.drawable.general_icon);
				intent.putExtra(String.class.getSimpleName(), SystemUtils.GENERAL);
				intent.putExtra(Long.class.getSimpleName(), R.drawable.general_mini_icon);
				break;
			case R.id.education_container:
				intent.putExtra(Integer.class.getSimpleName(), R.drawable.education_icon);
				intent.putExtra(String.class.getSimpleName(), SystemUtils.EDUCATION);
				intent.putExtra(Long.class.getSimpleName(), R.drawable.education_mini_icon);
				break;
			case R.id.business_container:
				intent.putExtra(Integer.class.getSimpleName(), R.drawable.business_icon);
				intent.putExtra(String.class.getSimpleName(), SystemUtils.BUSINESS);
				intent.putExtra(Long.class.getSimpleName(), R.drawable.business_mini_icon);
				break;
			case R.id.fun_container:
				intent.putExtra(Integer.class.getSimpleName(), R.drawable.fun_icon);
				intent.putExtra(String.class.getSimpleName(), SystemUtils.FUN);
				intent.putExtra(Long.class.getSimpleName(), R.drawable.fun_mini_icon);
				break;
			case R.id.events_container:
				intent.putExtra(Integer.class.getSimpleName(), R.drawable.events_icon);
				intent.putExtra(String.class.getSimpleName(), SystemUtils.EVENTS);
				intent.putExtra(Long.class.getSimpleName(), R.drawable.events_mini_icon);
				break;
			case R.id.other_container:
				intent.putExtra(Integer.class.getSimpleName(), R.drawable.other_icon);
				intent.putExtra(String.class.getSimpleName(), SystemUtils.OTHER);
				intent.putExtra(Long.class.getSimpleName(), R.drawable.other_mini_icon);
				break;
		}
		startActivity(intent);
	}
}

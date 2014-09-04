package com.appshed.appstore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appshed.appstore.R;
import com.appshed.appstore.activities.LoginDialog;
import com.appshed.appstore.activities.MainActivity;

/**
 * Created by Anton Maniskevich on 9/1/14.
 */
public class AppsCreatedByMeFragment extends Fragment implements View.OnClickListener {

	public static AppsCreatedByMeFragment newInstance() {
		AppsCreatedByMeFragment fragment = new AppsCreatedByMeFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_apps_created_by_me, null);
		view.findViewById(R.id.img_menu).setOnClickListener(this);
		view.findViewById(R.id.img_login_logout).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_menu:
				((MainActivity) getActivity()).toggleMenu();
				break;
			case R.id.img_login_logout:
				startActivity(new Intent(getActivity(), LoginDialog.class));
				break;
		}
	}
}

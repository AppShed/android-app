package com.appshed.appstore.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.appshed.appstore.R;
import com.appshed.appstore.activities.PhonegapActivity;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.services.RetrieveAppService;
import com.appshed.appstore.utils.SystemUtils;

import java.io.File;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
@Deprecated
public class AppDetailFragment extends Fragment implements View.OnClickListener{

	private static final String TAG = AppDetailFragment.class.getSimpleName();
	private App app;
	private String appFolder;

	public static AppDetailFragment newInstance(App app) {
		AppDetailFragment fragment = new AppDetailFragment();
		fragment.setApp(app);
		return fragment;
	}

	public void setApp(App app) {
		this.app = app;
		appFolder = Environment.getExternalStorageDirectory() + "/download/appstore/"+app.getId();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_detail, null);
		TextView title = (TextView) view.findViewById(R.id.txt_title);
		title.setText(app.getName());
		TextView description = (TextView) view.findViewById(R.id.txt_description);
		description.setText(app.getDescription());
		SystemUtils.IMAGELOADER.displayImage(app.getIcon(), (ImageView) view.findViewById(R.id.img_icon));
		Button install = (Button) view.findViewById(R.id.btn_install);
		if (new File(appFolder).exists()) {
			install.setText("Launch app");
		}
		install.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_install:
				if (new File(appFolder).exists()) {
					Log.i(TAG, appFolder);
					startActivity(new Intent(getActivity(), PhonegapActivity.class).putExtra(App.class.getSimpleName(), app.getId()));
				} else {
					getActivity().startService(new Intent(getActivity(), RetrieveAppService.class)
							.putExtra(RetrieveAppService.RETRATIVE_TYPE, RetrieveAppService.LOAD_APP)
							.putExtra(App.class.getSimpleName(), app));
				}
				break;
		}
	}
}

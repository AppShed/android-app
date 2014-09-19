package com.appshed.appstore.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appshed.appstore.R;
import com.appshed.appstore.fragments.AppStoreFragment;
import com.appshed.appstore.fragments.AppsCreatedByMeFragment;
import com.appshed.appstore.fragments.MySavedAppsFragment;
import com.rightutils.rightutils.activities.SupportRightFragmentActivity;

/**
 * Created by Anton Maniskevich on 8/20/14.
 */
public class MainActivityNew extends SupportRightFragmentActivity implements View.OnClickListener {

	private static final String TAG = MainActivityNew.class.getSimpleName();
	private DrawerLayout drawerLayout;
	private RelativeLayout menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initActivity(R.id.fragment_container, AppStoreFragment.newInstance());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_new);
		setUpMenu();
	}

	public void setUpMenu() {
		menu = (RelativeLayout)findViewById(R.id.menu_container);
		findViewById(R.id.top_container).setOnClickListener(null);
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		menu.findViewById(R.id.txt_appshed_gallery).setOnClickListener(this);
		menu.findViewById(R.id.txt_my_saved_apps).setOnClickListener(this);
		menu.findViewById(R.id.txt_created_by_me).setOnClickListener(this);
		menu.findViewById(R.id.txt_app_creator).setOnClickListener(this);
		menu.findViewById(R.id.qr_scanner_container).setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(menu)) {
			drawerLayout.closeDrawer(menu);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.txt_appshed_gallery:
				if (!(getLastFragment() instanceof AppStoreFragment)) {
					pushFragment(AppStoreFragment.newInstance());
				}
				break;
			case R.id.txt_my_saved_apps:
				if (!(getLastFragment() instanceof MySavedAppsFragment)) {
					pushFragment(MySavedAppsFragment.newInstance());
				}
				break;
			case R.id.txt_created_by_me:
				if (!(getLastFragment() instanceof AppsCreatedByMeFragment)) {
					pushFragment(AppsCreatedByMeFragment.newInstance());
				}
				break;
			case R.id.txt_app_creator:
				PackageManager pm = context.getPackageManager();
				Intent appStartIntent = pm.getLaunchIntentForPackage("com.ekreative.appcreator");
				if (appStartIntent != null) {
					context.startActivity(appStartIntent);
				} else {
					Toast.makeText(MainActivityNew.this, "AppCreator not found", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.qr_scanner_container:
				startActivity(new Intent(MainActivityNew.this, QRScanerActivity.class));
				break;
		}
		drawerLayout.closeDrawer(menu);
	}

	public void toggleMenu() {
		drawerLayout.openDrawer(menu);
	}
}

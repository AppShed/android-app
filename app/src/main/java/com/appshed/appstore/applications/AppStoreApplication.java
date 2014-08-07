package com.appshed.appstore.applications;

import android.app.Application;

import com.appshed.appstore.utils.SystemUtils;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class AppStoreApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SystemUtils.getCache(getApplicationContext());
	}
}

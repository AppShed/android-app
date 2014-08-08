package com.appshed.appstore.activities;

import android.os.Bundle;

import com.appshed.appstore.entities.App;

import org.apache.cordova.Config;
import org.apache.cordova.DroidGap;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class PhonegapActivity extends DroidGap {

	private static final String TAG = PhonegapActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = "file:///sdcard/download/www/www/index.html";
		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(App.class.getSimpleName())) {
			url = "file:///sdcard/download/appstore/"+getIntent().getExtras().getLong(App.class.getSimpleName())+"/index.html";
		}
		super.loadUrl(Config.getStartUrl());
		super.loadUrl(url);
	}
}

package com.appshed.appstore;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.cordova.Config;
import org.apache.cordova.DroidGap;


public class MyActivity extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		super.loadUrl(Config.getStartUrl());
		super.loadUrl("file:///android_asset/www/index.html");
	}

}

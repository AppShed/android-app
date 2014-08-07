package com.appshed.appstore;

import android.os.Bundle;
import org.apache.cordova.Config;
import org.apache.cordova.DroidGap;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class PhonegapActivity extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.loadUrl(Config.getStartUrl());
		super.loadUrl("file:///sdcard/download/www/www/index.html");
	}
}

package com.appshed.appstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.appshed.appstore.R;
import com.appshed.appstore.applications.AppStoreApplication;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.utils.SystemUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class PhonegapActivity extends Activity implements CordovaInterface {

	private static final String TAG = PhonegapActivity.class.getSimpleName();
	private CordovaWebView cordovaWebView;
	protected CordovaPlugin activityResultCallback = null;
	protected boolean activityResultKeepRunning;
	protected boolean keepRunning = true;
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	private App selectedApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonegap);
		String url = null;
		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(App.class.getSimpleName())) {
			long appId = getIntent().getExtras().getLong(App.class.getSimpleName());
			Log.i(TAG, ""+appId);
			selectedApp = AppStoreApplication.dbUtils.getAllWhere(String.format("id = %d", appId), App.class).getFirst();
			Log.i(TAG, ""+selectedApp);
			url = "file:"+SystemUtils.getAppFolder(appId)+"/index.html";
		}
		cordovaWebView = (CordovaWebView) findViewById(R.id.tutorialView);
		Config.init(this);
		cordovaWebView.loadUrl(Config.getStartUrl());
		cordovaWebView.loadUrl(url);

		AdView adView = (AdView)this.findViewById(R.id.adView);
		if (selectedApp.isAds()) {
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
		} else {
			findViewById(R.id.bottom_bar).setVisibility(View.GONE);
		}
	}

	@Override
	public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
		this.activityResultCallback = command;
		this.activityResultKeepRunning = this.keepRunning;

		// If multitasking turned on, then disable it for activities that return results
		if (command != null) {
			this.keepRunning = false;
		}

		// Start activity
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin plugin) {
		this.activityResultCallback = plugin;
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Object onMessage(String s, Object o) {
		return null;
	}

	@Override
	public ExecutorService getThreadPool() {
		return threadPool;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		CordovaPlugin callback = this.activityResultCallback;
		if (callback != null) {
			callback.onActivityResult(requestCode, resultCode, intent);
		}
	}

	public void onDestroy() {
		super.onDestroy();

		if (this.cordovaWebView != null) {
			this.cordovaWebView.handleDestroy();
		}
	}
}

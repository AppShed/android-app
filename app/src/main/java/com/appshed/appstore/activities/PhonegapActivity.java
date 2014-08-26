package com.appshed.appstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.appshed.appstore.R;
import com.appshed.appstore.entities.App;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.DroidGap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class PhonegapActivity extends Activity implements CordovaInterface {

	private static final String TAG = PhonegapActivity.class.getSimpleName();
	private CordovaWebView cwv;
	protected CordovaPlugin activityResultCallback = null;
	protected boolean activityResultKeepRunning;
	protected boolean keepRunning = true;
	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonegap);
		String url = "file:///sdcard/download/www/www/index.html";
		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(App.class.getSimpleName())) {
			url = "file:///sdcard/download/appstore/"+getIntent().getExtras().getLong(App.class.getSimpleName())+"/index.html";
		}
		cwv = (CordovaWebView) findViewById(R.id.tutorialView);
		Config.init(this);
		cwv.loadUrl(Config.getStartUrl());
		cwv.loadUrl(url);

		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
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
}

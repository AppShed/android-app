package com.appshed.appstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appshed.appstore.R;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.tasks.RetrieveDetailApp;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class LaunchActivity extends Activity {

	private static final String TAG = LaunchActivity.class.getSimpleName();
	private boolean isFullScreen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		isFullScreen = getIntent().getBooleanExtra(Boolean.class.getSimpleName(), false);
		if (getIntent().getData() != null) {
			String text = getIntent().getData().toString();
			long appId = 0;
			try {
				appId = Long.valueOf(text.substring(text.lastIndexOf('/')+1));
			} catch (Exception e) {
				Toast.makeText(LaunchActivity.this, "Can't get application id", Toast.LENGTH_SHORT).show();
				finish();
			}
			new RetrieveDetailApp(LaunchActivity.this, findViewById(R.id.progress_bar), appId).execute();
		} else {
			Toast.makeText(LaunchActivity.this, "Can't get application id", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	public void setApp(App app) {
		if (app != null) {
			startActivity(new Intent(LaunchActivity.this, AppDetailDialog.class)
					.putExtra(App.class.getSimpleName(), app)
					.putExtra(Boolean.class.getSimpleName(), isFullScreen)
			);
		}
		finish();
	}
}

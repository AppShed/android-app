package com.appshed.appstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appshed.appstore.R;


public class MyActivity extends Activity implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		findViewById(R.id.btn_start).setOnClickListener(this);
		findViewById(R.id.btn_add_shortcut).setOnClickListener(this);
		findViewById(R.id.btn_delete_shortcut).setOnClickListener(this);
		findViewById(R.id.btn_scan).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_start:
				startActivity(new Intent(MyActivity.this, PhonegapActivity.class));
				break;
			case R.id.btn_add_shortcut:
				addShortcut();
				break;
			case R.id.btn_delete_shortcut:
				removeShortcut();
				break;
			case R.id.btn_scan:
				startActivity(new Intent(MyActivity.this, CameraTestActivity.class));
				break;
		}
	}

	private void addShortcut() {
		//Adding shortcut for MainActivity
		//on Home screen
		Intent shortcutIntent = new Intent(getApplicationContext(), LaunchActivity.class);
		shortcutIntent.putExtra(String.class.getSimpleName(), "launcher 1");

		shortcutIntent.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "launcher 1");
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(getApplicationContext(),
						R.drawable.ic_launcher));

		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(addIntent);
	}

	private void removeShortcut() {

		//Deleting shortcut for MainActivity
		//on Home screen
		Intent shortcutIntent = new Intent(getApplicationContext(),LaunchActivity.class);
		shortcutIntent.putExtra(String.class.getSimpleName(), "launcher 1");
		shortcutIntent.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "launcher 1");
		addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(addIntent);
	}
}

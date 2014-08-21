package com.appshed.appstore.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.appshed.appstore.R;


public class MyActivity extends RegIdActivity implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		findViewById(R.id.btn_start).setOnClickListener(this);
		findViewById(R.id.btn_scan).setOnClickListener(this);
		findViewById(R.id.btn_open).setOnClickListener(this);
		findViewById(R.id.btn_apps).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_start:
				startActivity(new Intent(MyActivity.this, PhonegapActivity.class));
				break;
			case R.id.btn_scan:
				startActivity(new Intent(MyActivity.this, CameraTestActivity.class));
				break;
			case R.id.btn_open:
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://appshed.com/appbuilder/"));
//				browserIntent.setComponent(ComponentName.unflattenFromString("com.appshed.appstore/com.appshed.appstore.activities.LaunchActivity"));
				startActivity(browserIntent);
				break;
			case R.id.btn_apps:
				startActivity(new Intent(MyActivity.this, AppsByCategoryActivity.class));
				break;
		}
	}

}

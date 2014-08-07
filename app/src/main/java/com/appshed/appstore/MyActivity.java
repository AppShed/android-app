package com.appshed.appstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.cordova.Config;
import org.apache.cordova.DroidGap;


public class MyActivity extends Activity implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		findViewById(R.id.btn_start).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_start:
				startActivity(new Intent(MyActivity.this, PhonegapActivity.class));
				break;
		}
	}
}

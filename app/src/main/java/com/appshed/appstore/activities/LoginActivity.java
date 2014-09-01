package com.appshed.appstore.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.appshed.appstore.R;

/**
 * Created by Anton Maniskevich on 9/1/14.
 */
public class LoginActivity extends Activity implements com.rightutils.rightutils.activities.LoginActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}

	@Override
	public void sendRequest() {

	}

	@Override
	public <T> void doStart(T t) {
	}
}

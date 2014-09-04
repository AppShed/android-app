package com.appshed.appstore.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.appshed.appstore.R;

/**
 * Created by Anton Maniskevich on 9/1/14.
 */
public class LoginDialog extends Activity implements com.rightutils.rightutils.activities.LoginActivity{

	private EditText login;
	private EditText password;
	private EditText schoolCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_login);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		login = (EditText) findViewById(R.id.f_login);
		password = (EditText) findViewById(R.id.f_password);
		schoolCode = (EditText) findViewById(R.id.f_school_code);
	}

	@Override
	public void sendRequest() {
	}

	@Override
	public <T> void doStart(T t) {
	}
}

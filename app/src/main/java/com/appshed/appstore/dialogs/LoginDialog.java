package com.appshed.appstore.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import com.appshed.appstore.R;
import com.appshed.appstore.entities.User;
import com.appshed.appstore.tasks.LoginTask;
import com.appshed.appstore.utils.SystemUtils;

/**
 * Created by Anton Maniskevich on 9/1/14.
 */
public class LoginDialog extends Activity implements View.OnClickListener{

	private EditText login;
	private EditText password;
	private EditText schoolCode;
	private View progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_login);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		login = (EditText) findViewById(R.id.f_login);
		password = (EditText) findViewById(R.id.f_password);
		schoolCode = (EditText) findViewById(R.id.f_school_code);
		progressBar = findViewById(R.id.progress_bar);
		findViewById(R.id.txt_login).setOnClickListener(this);
	}

	public void sendRequest() {
		String username = login.getText().toString();
		if (!schoolCode.getText().toString().trim().isEmpty()) {
			username = schoolCode.getText().toString().trim()+"_"+username;
		}
		String pass = password.getText().toString();
		new LoginTask(LoginDialog.this, progressBar, username, pass).execute();
	}

	public void doStart(User user) {
		SystemUtils.cache.setUser(user);
		SystemUtils.saveCache(LoginDialog.this);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.txt_login:
				sendRequest();
				break;
		}
	}
}

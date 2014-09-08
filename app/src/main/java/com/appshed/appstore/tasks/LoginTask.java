package com.appshed.appstore.tasks;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.appshed.appstore.dialogs.LoginDialog;
import com.appshed.appstore.entities.User;
import com.appshed.appstore.utils.SniRequestUtils;
import com.appshed.appstore.utils.SystemUtils;
import com.rightutils.rightutils.tasks.BaseTask;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;
import static com.appshed.appstore.utils.SystemUtils.MAPPER;

/**
 * Created by Anton Maniskevich on 9/8/14.
 */
public class LoginTask extends BaseTask {

	private static final String TAG = LoginTask.class.getSimpleName();
	private String basicAuth;
	private User user;


	public LoginTask(Context context, View progressBar, String username, String password) {
		super(context, progressBar);
		basicAuth = "Basic " + Base64.encodeToString((username+":"+password).getBytes(), Base64.NO_WRAP);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			HttpResponse response = SniRequestUtils.getHttpResponse(SystemUtils.LOGIN_URL, basicAuth);
			int status = response.getStatusLine().getStatusCode();
			Log.i(TAG, "status code: " + String.valueOf(status));
			if (status == HttpStatus.SC_OK) {
				user = MAPPER.readValue(response.getEntity().getContent(), UserData.class).user;
				return true;
			}
		} catch (Exception e) {
			Log.e(TAG, "run", e);
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			user.setBasicAuth(basicAuth);
			((LoginDialog) context).doStart(user);
		} else {
			Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}

	private static class UserData {
		public User user;
	}
}

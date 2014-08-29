package com.appshed.appstore.tasks;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.appstore.activities.LaunchActivity;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.utils.SniRequestUtils;
import com.appshed.appstore.utils.SystemUtils;
import com.rightutils.rightutils.tasks.BaseTask;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;
import ch.boye.httpclientandroidlib.util.EntityUtils;

import static com.appshed.appstore.utils.SystemUtils.MAPPER;

/**
 * Created by Anton Maniskevich on 8/29/14.
 */
public class RetrieveDetailApp extends BaseTask {

	private static final String TAG = RetrieveDetailApp.class.getSimpleName();
	private long appId;
	private String error;
	private AppData appData;

	public RetrieveDetailApp(Context context, View progressBar, long appId) {
		super(context, progressBar);
		this.appId = appId;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			HttpResponse response = SniRequestUtils.getHttpResponse(String.format(SystemUtils.APP_DETAIL_URL, appId));
			int status = response.getStatusLine().getStatusCode();
			Log.i(TAG, "status code: " + String.valueOf(status));
			if (status == HttpStatus.SC_OK) {
				appData = MAPPER.readValue(response.getEntity().getContent(), AppData.class);
				return true;
			} else {
				error = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e(TAG, "run", e);
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			((LaunchActivity) context).setApp(appData.app);
		} else {
			if (error != null) {
				Toast.makeText(context, error, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "Unknown error", Toast.LENGTH_LONG).show();
			}
			((LaunchActivity) context).setApp(null);
		}
		super.onPostExecute(result);
	}

	private static class AppData {
		public App app;
	}
}

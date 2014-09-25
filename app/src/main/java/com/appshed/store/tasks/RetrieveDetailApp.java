package com.appshed.store.tasks;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.store.R;
import com.appshed.store.activities.LaunchActivity;
import com.appshed.store.applications.AppStoreApplication;
import com.appshed.store.entities.App;
import com.appshed.store.utils.SniRequestUtils;
import com.appshed.store.utils.SystemUtils;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.tasks.BaseTask;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;

import static com.appshed.store.utils.SystemUtils.MAPPER;

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
			String resultUrl = String.format(SystemUtils.APP_DETAIL_URL, appId);
			Log.i(TAG, resultUrl);
			HttpResponse response = SniRequestUtils.getHttpResponse(resultUrl);
			int status = response.getStatusLine().getStatusCode();
			Log.i(TAG, "status code: " + String.valueOf(status));
			if (status == HttpStatus.SC_OK) {
				appData = MAPPER.readValue(response.getEntity().getContent(), AppData.class);
				return true;
			} else {
//				error = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e(TAG, "run", e);
		}
		RightList<App> apps = AppStoreApplication.dbUtils.getAllWhere(String.format("id = %d", appId), App.class);
		if (!apps.isEmpty()) {
			appData = new AppData();
			appData.app = apps.get(0);
			return true;
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
				Toast.makeText(context, context.getString(R.string.app_not_avaliable), Toast.LENGTH_LONG).show();
			}
			((LaunchActivity) context).setApp(null);
		}
		super.onPostExecute(result);
	}

	private static class AppData {
		public App app;
	}
}

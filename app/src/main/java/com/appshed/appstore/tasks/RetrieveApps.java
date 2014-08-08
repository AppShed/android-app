package com.appshed.appstore.tasks;

import static com.appshed.appstore.utils.SystemUtils.*;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.appstore.entities.App;
import com.appshed.appstore.fragments.AppsFragment;
import com.appshed.appstore.utils.RequestUtils;
import com.rightutils.collections.RightList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import java.net.URLDecoder;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class RetrieveApps extends BaseTask {

	private static final String TAG = RetrieveApps.class.getSimpleName();
	private AppsFragment fragment;
	private String error;
	private AppData appData;

	public RetrieveApps(Context context, View progressBar, AppsFragment fragment) {
		super(context, progressBar);
		this.fragment = fragment;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			Log.i(TAG, APPS_URL);
			HttpResponse response = RequestUtils.getHttpResponse(APPS_URL);
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

	private static class AppData {
		public RightList<App> apps;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			fragment.addApps(appData.apps);
		} else {
			fragment.addApps(new RightList<App>());
			if (error != null) {
				Toast.makeText(context, error, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "Unknown error", Toast.LENGTH_LONG).show();
			}
		}
		super.onPostExecute(result);
	}
}

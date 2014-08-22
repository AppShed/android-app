package com.appshed.appstore.tasks;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.appstore.entities.App;
import com.appshed.appstore.fragments.AppsByCategoryFragment;
import com.appshed.appstore.fragments.SearchFragment;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.tasks.BaseTask;
import com.rightutils.rightutils.utils.RequestUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import static com.appshed.appstore.utils.SystemUtils.APPS_URL;
import static com.appshed.appstore.utils.SystemUtils.MAPPER;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class SearchApps extends BaseTask {

	private static final String TAG = SearchApps.class.getSimpleName();
	private SearchFragment fragment;
	private String error;
	private AppData appData;
	private String query;

	public SearchApps(Context context, View progressBar, SearchFragment fragment) {
		super(context, progressBar);
		this.fragment = fragment;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			String resultUrl = APPS_URL;
			resultUrl += "?search="+query;
			Log.i(TAG, resultUrl);
			HttpResponse response = RequestUtils.getHttpResponse(resultUrl);
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

	public SearchApps setQuery(String query) {
		this.query = query;
		return this;
	}
}

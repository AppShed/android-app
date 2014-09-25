package com.appshed.store.tasks;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.store.R;
import com.appshed.store.entities.App;
import com.appshed.store.fragments.SearchFragment;
import com.appshed.store.utils.SniRequestUtils;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.tasks.BaseTask;

import java.net.URLEncoder;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;
import ch.boye.httpclientandroidlib.util.EntityUtils;
import static com.appshed.store.utils.SystemUtils.APPS_URL;
import static com.appshed.store.utils.SystemUtils.MAPPER;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class RetrieveSearchApps extends BaseTask {

	private static final String TAG = RetrieveSearchApps.class.getSimpleName();
	private SearchFragment fragment;
	private String error;
	private AppData appData;
	private String query;

	public RetrieveSearchApps(Context context, View progressBar, SearchFragment fragment) {
		super(context, progressBar);
		this.fragment = fragment;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			String resultUrl = APPS_URL;
			resultUrl += "?search="+ URLEncoder.encode(query, "UTF-8");
			Log.i(TAG, resultUrl);
			HttpResponse response = SniRequestUtils.getHttpResponse(resultUrl);
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
				Toast.makeText(context,  context.getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
			}
		}
		super.onPostExecute(result);
	}

	public RetrieveSearchApps setQuery(String query) {
		this.query = query;
		return this;
	}
}

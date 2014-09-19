package com.appshed.appstore.tasks;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.appstore.R;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.fragments.AppsByCategoryFragment;
import com.appshed.appstore.fragments.SearchFragment;
import com.appshed.appstore.utils.SniRequestUtils;
import com.appshed.appstore.utils.SystemUtils;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.tasks.BaseTask;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;
import ch.boye.httpclientandroidlib.util.EntityUtils;

import static com.appshed.appstore.utils.SystemUtils.APPS_URL;
import static com.appshed.appstore.utils.SystemUtils.MAPPER;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class RetrieveCategorySearchApps extends BaseTask {

	private static final String TAG = RetrieveCategorySearchApps.class.getSimpleName();
	private AppsByCategoryFragment fragment;
	private String error;
	private AppData appData;
	private String category;
	private String query;

	public RetrieveCategorySearchApps(Context context, View progressBar, AppsByCategoryFragment fragment) {
		super(context, progressBar);
		this.fragment = fragment;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			String resultUrl = APPS_URL;
			if (!category.equals(SystemUtils.GENERAL)) {
				resultUrl += "?category="+category;
				resultUrl += "&search=" + query;
			} else {
				resultUrl += "?search=" + query;
			}
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

	public RetrieveCategorySearchApps setQuery(String query) {
		this.query = query;
		return this;
	}

	public RetrieveCategorySearchApps setCategory(String category) {
		this.category = category;
		return this;
	}
}

package com.appshed.store.tasks;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.store.R;
import com.appshed.store.entities.App;
import com.appshed.store.fragments.AppsCreatedByMeFragment;
import com.appshed.store.utils.SniRequestUtils;
import com.appshed.store.utils.SystemUtils;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.tasks.BaseTask;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;

import static com.appshed.store.utils.SystemUtils.MY_APPS_URL;
import static com.appshed.store.utils.SystemUtils.MAPPER;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class RetrieveMyApps extends BaseTask {

	private static final String TAG = RetrieveMyApps.class.getSimpleName();
	private AppsCreatedByMeFragment fragment;
	private String error;
	private AppData appData;
	private Long sinceId;
	private Long maxId;

	public RetrieveMyApps(Context context, View progressBar, AppsCreatedByMeFragment fragment) {
		super(context, progressBar);
		this.fragment = fragment;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			//TODO
//			final String basicAuth = "Basic " + Base64.encodeToString("lera@ekreative.com:smile2me".getBytes(), Base64.NO_WRAP);
			String resultUrl = MY_APPS_URL;
			if (sinceId != null) {
				resultUrl += "&since_id=" + sinceId;
			}
			if (maxId != null) {
				resultUrl += "&max_id=" + maxId;
			}
			Log.i(TAG, resultUrl);
			HttpResponse response = SniRequestUtils.getHttpResponse(resultUrl, SystemUtils.cache.getUser().getBasicAuth());
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

	public RetrieveMyApps setSinceId(Long sinceId) {
		this.sinceId = sinceId;
		return this;
	}

	public RetrieveMyApps setMaxId(Long maxId) {
		this.maxId = maxId;
		return this;
	}
}

package com.appshed.appstore.tasks;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.appstore.R;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.fragments.FeaturedFragment;
import com.appshed.appstore.utils.SniRequestUtils;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.tasks.BaseTask;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;
import ch.boye.httpclientandroidlib.util.EntityUtils;
import static com.appshed.appstore.utils.SystemUtils.FEATURED_APPS_URL;
import static com.appshed.appstore.utils.SystemUtils.MAPPER;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class RetrieveFeaturedApps extends BaseTask {

	private static final String TAG = RetrieveFeaturedApps.class.getSimpleName();
	private FeaturedFragment fragment;
	private String error;
	private AppData appData;
	private Long sinceId;
	private Long maxId;

	public RetrieveFeaturedApps(Context context, View progressBar, FeaturedFragment fragment) {
		super(context, progressBar);
		this.fragment = fragment;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			String resultUrl = FEATURED_APPS_URL;
			if (sinceId != null) {
				resultUrl += "&since_id=" + sinceId;
			}
			if (maxId != null) {
				resultUrl += "&max_id=" + maxId;
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

	public RetrieveFeaturedApps setSinceId(Long sinceId) {
		this.sinceId = sinceId;
		return this;
	}

	public RetrieveFeaturedApps setMaxId(Long maxId) {
		this.maxId = maxId;
		return this;
	}
}

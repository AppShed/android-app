package com.appshed.appstore.tasks;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appshed.appstore.applications.AppStoreApplication;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.fragments.FeaturedFragment;
import com.appshed.appstore.fragments.MySavedAppsFragment;
import com.appshed.appstore.utils.SniRequestUtils;
import com.appshed.appstore.utils.SystemUtils;
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
public class RetrieveSavedApps extends BaseTask {

	private static final String TAG = RetrieveSavedApps.class.getSimpleName();
	private MySavedAppsFragment fragment;
	private RightList<App> apps;

	public RetrieveSavedApps(Context context, View progressBar, MySavedAppsFragment fragment) {
		super(context, progressBar);
		this.fragment = fragment;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		apps = AppStoreApplication.dbUtils.getAll(App.class);
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		fragment.addApps(apps);
		super.onPostExecute(result);
	}

}

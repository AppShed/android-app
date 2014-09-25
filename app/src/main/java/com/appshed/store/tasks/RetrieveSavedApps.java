package com.appshed.store.tasks;

import android.content.Context;
import android.view.View;

import com.appshed.store.applications.AppStoreApplication;
import com.appshed.store.entities.App;
import com.appshed.store.fragments.MySavedAppsFragment;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.tasks.BaseTask;

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

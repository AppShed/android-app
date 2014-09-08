package com.appshed.appstore.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.appshed.appstore.applications.AppStoreApplication;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.utils.SystemUtils;
import com.rightutils.rightutils.collections.RightList;
import java.io.File;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class DeleteAppService extends IntentService {

	private static final String TAG = DeleteAppService.class.getSimpleName();
	public static final String DELETE_TYPE = "delete_type";
	public static final int DELETE_APP = 200;
	private static RightList<App> appsPool = new RightList<App>();
	private static DeleteFile deleteFile;

	public DeleteAppService() {
		super(DeleteAppService.class.getName());
		deleteFile = new DeleteFile();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getExtras() != null) {
			int retrativeType = intent.getExtras().getInt(DELETE_TYPE);
			switch (retrativeType) {
				case DELETE_APP:
					appsPool.add((App) intent.getSerializableExtra(App.class.getSimpleName()));
					if (appsPool.size() == 1) {
						deleteFile.start();
					}
					break;
			}
		}
	}

	private class DeleteFile extends Thread {

		@Override
		public void run() {
			while (!appsPool.isEmpty()) {
				Log.i(TAG, "poolSize"+appsPool.size());
				try {
					App currentLoadingApp = appsPool.get(0);
					String PATH = SystemUtils.getSaveFolder();
					AppStoreApplication.dbUtils.deleteWhere(App.class, String.format("id = %d", currentLoadingApp.getId()));
					deleteRecursive(new File(PATH + currentLoadingApp.getId()));
					//remove icon for app
					SystemUtils.removeAppShortcut(getApplicationContext(), currentLoadingApp.getName(), currentLoadingApp.getId());
				} catch (Exception e) {
					Log.e(TAG, "DeleteFile", e);
				} finally {
					appsPool.remove(0);
				}
			}
		}
	}

	void deleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteRecursive(child);

		fileOrDirectory.delete();
	}

	public static long getProgress(long appId) {
		if (appsPool.contains(new App(appId))) {
			return 0;
		}
		return -1;
	}
}


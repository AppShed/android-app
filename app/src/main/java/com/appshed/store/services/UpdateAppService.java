package com.appshed.store.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.appshed.store.applications.AppStoreApplication;
import com.appshed.store.entities.App;
import com.appshed.store.utils.SystemUtils;
import com.appshed.store.utils.ZipUtils;
import com.rightutils.rightutils.collections.RightList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class UpdateAppService extends IntentService {

	private static final String TAG = UpdateAppService.class.getSimpleName();
	public static final String UPDATE_TYPE = "update_type";
	public static final int UPDATE_APP = 100;
	private static RightList<App> appsPool = new RightList<App>();
	private static UpdateAppFiles updateAppFiles;

	private static long progress = 0;
	private static long length = 0;
	private static boolean cancelCurrent = false;

	public UpdateAppService() {
		super(UpdateAppService.class.getName());
		updateAppFiles = new UpdateAppFiles();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getExtras() != null) {
			int updateType = intent.getExtras().getInt(UPDATE_TYPE);
			switch (updateType) {
				case UPDATE_APP:
					appsPool.add((App) intent.getSerializableExtra(App.class.getSimpleName()));
					if (appsPool.size() == 1) {
						updateAppFiles.start();
					}
					break;
			}
		}
	}

	private class UpdateAppFiles extends Thread {

		@Override
		public void run() {
			while (!appsPool.isEmpty()) {
				Log.i(TAG, "poolSize"+appsPool.size());
				try {
					progress = 0;
					App currentLoadingApp = appsPool.get(0);
					Log.i(TAG, currentLoadingApp.getZip());

					URL url = new URL(currentLoadingApp.getZip());
					URLConnection connection = url.openConnection();
					connection.connect();
					// this will be useful so that you can show a typical 0-100% progress bar
					length = connection.getContentLength();
					InputStream is = new BufferedInputStream(connection.getInputStream());

					String PATH = SystemUtils.getSaveFolder();
					File file = new File(PATH);
					file.mkdirs();
					File outputFile = new File(file,currentLoadingApp.getId() + ".zip");
					FileOutputStream fos = new FileOutputStream(outputFile);
					byte[] buffer = new byte[1024];
					int count = 0;
					while ((count = is.read(buffer)) != -1 && !cancelCurrent) {
						fos.write(buffer, 0, count);
						Log.i(TAG, "Loading..." + progress);
						progress += count;
					}
					Log.i(TAG, "1");
					fos.close();
					Log.i(TAG, "2");
					is.close();
					Log.i(TAG, "3");
					if (cancelCurrent) {
						new File(PATH+currentLoadingApp.getId()+".zip").delete();
						cancelCurrent = false;
						Log.i(TAG, "3.1");
					} else {
						//delete app
						AppStoreApplication.dbUtils.deleteWhere(App.class, String.format("id = %d", currentLoadingApp.getId()));
						deleteRecursive(new File(PATH + currentLoadingApp.getId()));
						//remove icon for app
						SystemUtils.removeAppShortcut(getApplicationContext(), currentLoadingApp.getName(), currentLoadingApp.getId());

						//unzip
						ZipUtils.unZipIt(PATH + currentLoadingApp.getId() + ".zip", PATH + currentLoadingApp.getId());
						AppStoreApplication.dbUtils.add(currentLoadingApp);
						//add icon for app
						SystemUtils.addAppShortcut(getApplicationContext(), currentLoadingApp.getName(), currentLoadingApp.getId());
					}
				} catch (IOException e) {
					Log.e(TAG, "UpdateAppFiles", e);
				} finally {
					Log.i(TAG, "4");
					appsPool.remove(0);
					Log.i(TAG, "5");
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
		if (cancelCurrent) {
			return -1;
		}
		if (appsPool.isEmpty()) {
			return -1;
		}
		if (appsPool.get(0).getId() == appId) {
			return progress;
		}
		if (appsPool.contains(new App(appId))) {
			return 0;
		}
		return -1;
	}

	public static long getLength(long appId) {
		if (appsPool.get(0).getId() == appId) {
			return length;
		}
		return -1;
	}

	public static void cancelLoading(long appId) {
		if (appsPool.get(0).getId() == appId) {
			cancelCurrent = true;
		} else {
			appsPool.remove(new App(appId));
		}
	}
}


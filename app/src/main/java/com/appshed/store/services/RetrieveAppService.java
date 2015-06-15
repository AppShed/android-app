package com.appshed.store.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.appshed.store.applications.AppStoreApplication;
import com.appshed.store.entities.App;
import com.appshed.store.utils.SystemUtils;
import com.appshed.store.utils.ZipUtils;
import com.rightutils.rightutils.collections.RightList;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class RetrieveAppService extends IntentService {

	private static final String TAG = RetrieveAppService.class.getSimpleName();
	public static final String RETRIEVE_TYPE = "retrieve_type";
	public static final int LOAD_APP = 100;
	private static RightList<App> appsPool = new RightList<App>();
	private static RetrieveFile retrieveFile;

	private static long progress = 0;
	private static long length = 0;
	private static boolean cancelCurrent = false;

	public RetrieveAppService() {
		super(RetrieveAppService.class.getName());
		retrieveFile = new RetrieveFile();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getExtras() != null) {
			int retrativeType = intent.getExtras().getInt(RETRIEVE_TYPE);
			switch (retrativeType) {
				case LOAD_APP:
					appsPool.add((App) intent.getSerializableExtra(App.class.getSimpleName()));
					if (appsPool.size() == 1) {
						retrieveFile.start();
					}
					break;
			}
		}
	}

	private class RetrieveFile extends Thread {

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
					Log.i(TAG, ""+connection.getHeaderField("Content-Disposition"));
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
//						Log.i(TAG, "Loading..." + progress);
						progress += count;
					}
					fos.close();
					is.close();
					if (cancelCurrent) {
						new File(PATH+currentLoadingApp.getId()+".zip").delete();
						cancelCurrent = false;
					} else {
						//unzip
						String outputFolder = PATH + currentLoadingApp.getId();
						ZipUtils.unZipIt(PATH + currentLoadingApp.getId() + ".zip", outputFolder);
						AppStoreApplication.dbUtils.add(currentLoadingApp);
						copyPlugins(outputFolder);
						//add icon for app
						SystemUtils.addAppShortcut(getApplicationContext(), currentLoadingApp.getName(), currentLoadingApp.getId());
					}
				} catch (IOException e) {
					Log.e(TAG, "RetrieveFile", e);
				} finally {
					appsPool.remove(0);
				}
			}
		}
	}

	private void copyPlugins(String outputFolder) throws IOException {
		copyFileFromAssets(outputFolder +"/plugins/com.appshed.ioioplugin/", "www/plugins/com.appshed.ioioplugin/ioio.js");
		copyFileFromAssets(outputFolder +"/plugins/org.apache.cordova.camera/www/", "www/plugins/org.apache.cordova.camera/www/Camera.js");
		copyFileFromAssets(outputFolder +"/plugins/org.apache.cordova.camera/www/", "www/plugins/org.apache.cordova.camera/www/CameraConstants.js");
		copyFileFromAssets(outputFolder +"/plugins/org.apache.cordova.camera/www/", "www/plugins/org.apache.cordova.camera/www/CameraPopoverHandle.js");
		copyFileFromAssets(outputFolder +"/plugins/org.apache.cordova.camera/www/", "www/plugins/org.apache.cordova.camera/www/CameraPopoverOptions.js");
		copyFileFromAssets(outputFolder, "www/cordova.js");
		copyFileFromAssets(outputFolder, "www/cordova_plugins.js");
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

	private void copyFileFromAssets(String path, String sourceFileName) throws IOException {
		String targetFile = sourceFileName.contains("/") ? sourceFileName.substring(sourceFileName.lastIndexOf("/")+1) : sourceFileName;
		InputStream myInput = getApplicationContext().getAssets().open(sourceFileName);
		File dir = new File(path);
		dir.mkdirs();
		File file = new File(dir, targetFile);
		FileOutputStream myOutput = new FileOutputStream(file);
		byte[] buffer = new byte[1024];

		int length;
		while((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
}


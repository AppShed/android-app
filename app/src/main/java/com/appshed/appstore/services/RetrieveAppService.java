package com.appshed.appstore.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.appshed.appstore.entities.App;
import com.appshed.appstore.utils.SystemUtils;
import com.appshed.appstore.utils.ZipUtils;
import com.rightutils.rightutils.collections.RightList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class RetrieveAppService extends IntentService {

	private static final String TAG = RetrieveAppService.class.getSimpleName();
	public static final String RETRATIVE_TYPE = "retrative_type";
	public static final int LOAD_APP = 100;
	private static RightList<App> appsPool = new RightList<App>();
	private static RetrativeFile retrativeFile;

	private static long progress = 0;

	public RetrieveAppService() {
		super(RetrieveAppService.class.getName());
		retrativeFile = new RetrativeFile();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getExtras() != null) {
			int retrativeType = intent.getExtras().getInt(RETRATIVE_TYPE);
			switch (retrativeType) {
				case LOAD_APP:
					appsPool.add((App) intent.getSerializableExtra(App.class.getSimpleName()));
					if (appsPool.size() == 1) {
						retrativeFile.start();
					}
					break;
			}
		}
	}

	private class RetrativeFile extends Thread {

		@Override
		public void run() {
			while (!appsPool.isEmpty()) {
				try {
					progress = 0;
					Log.i(TAG, appsPool.get(0).getZip());
					HttpGet get = new HttpGet(appsPool.get(0).getZip());
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpResponse response = httpClient.execute(get);
					int status = response.getStatusLine().getStatusCode();
					Log.i(TAG, "status " + status);
					Log.i(TAG, "length " + response.getEntity().getContentLength());
					InputStream is = response.getEntity().getContent();
					String PATH = Environment.getExternalStorageDirectory() + "/download/appstore/";
					File file = new File(PATH);
					file.mkdirs();
					File outputFile = new File(file, appsPool.get(0).getId() + ".zip");
					FileOutputStream fos = new FileOutputStream(outputFile);
					byte[] buffer = new byte[1024];
					int count = 0;
					while ((count = is.read(buffer)) != -1) {
						fos.write(buffer, 0, count);
						Log.i(TAG, "Loading..." + progress);
						progress += count;
					}
					fos.close();
					is.close();
					//unzip
					ZipUtils.unZipIt(PATH+appsPool.get(0).getId()+".zip",PATH+appsPool.get(0).getId());
					//add icon for app
					SystemUtils.addAppShortcut(getApplicationContext(), appsPool.get(0).getName(), appsPool.get(0).getId());

					//added info about file to base and static list
//					appsPool.get(0).setPhonePath(PATH + appsPool.get(0).getId()+".mp3");
//					dbUtils.checkAsDownloaded(appsPool.get(0));
				} catch (IOException e) {
					Log.e(TAG, "RetrativeFile", e);
				} finally {
					appsPool.remove(0);
				}
			}
		}
	}

	public static long getProgress(long appId) {
		Log.i(TAG, " " + appId);
//		if (appsPool.isEmpty()) {
//			return -1;
//		}
//		if (appsPool.get(0).getId() == appId) {
//			return progress;
//		}
		if (appsPool.contains(new App(appId))) {
			return 0;
		}
		return -1;
	}
}


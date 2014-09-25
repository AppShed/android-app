package com.appshed.store.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.appshed.store.activities.PhonegapActivity;
import com.appshed.store.entities.App;
import com.appshed.store.entities.Cache;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class SystemUtils {

	public static final String GENERAL = "general";
	public static final String EDUCATION = "education";
	public static final String BUSINESS = "business";
	public static final String FUN = "fun";
	public static final String EVENTS = "events";
	public static final String OTHER = "other";
	private static final String TAG = SystemUtils.class.getSimpleName();

	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String SHARE_URL = "http://apps.appshed.com/%d";

	public static Cache cache;
	public static String SENDER_ID = "800215983654";
//	private static final String BASE_URL = "http://appshed-api.fred.ekreative.com/";
	private static final String BASE_URL = "https://api.appshed.com/";

	public static final String APPS_URL = BASE_URL + "apps";
	public static final String FEATURED_APPS_URL = BASE_URL + "apps?featured=1";
	public static final String MY_APPS_URL = BASE_URL + "apps/me?1=1";
	public static final String APP_DETAIL_URL = BASE_URL + "apps/%d";
	public static final String LOGIN_URL =BASE_URL + "users/me";

	public static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	public static ImageLoader IMAGELOADER;

	private SystemUtils() {
	}

	public static void saveCache(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		try {
			sharedPreferences.edit().remove(Cache.class.getName()).commit();
			sharedPreferences.edit().putString(Cache.class.getName(), MAPPER.writeValueAsString(cache)).commit();
		} catch (Exception e) {
			Log.e(TAG, "save CACHE", e);
		}
	}

	public static void getCache(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		if (!sharedPreferences.getString(Cache.class.getName(), "").isEmpty()) {
			try {
				cache = MAPPER.readValue(sharedPreferences.getString(Cache.class.getName(), ""), Cache.class);
			} catch (Exception e) {
				Log.e(TAG, "load CACHE", e);
			}
		}
		if (cache == null) {
			cache = new Cache();
		}
	}

	public static void addAppShortcut(Context context, String name, long appId) {
		Intent shortcutIntent = new Intent(context, PhonegapActivity.class);
		shortcutIntent.putExtra(App.class.getSimpleName(), appId);
		shortcutIntent.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		Bitmap bitmap = BitmapFactory.decodeFile(getAppFolder(appId)+"/icon.png");
		if (bitmap != null) {
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, Bitmap.createScaledBitmap(bitmap, 128, 128, true));
		}

		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		context.sendBroadcast(addIntent);
	}

	public static void removeAppShortcut(Context context, String name, long appId) {

		Intent shortcutIntent = new Intent(context, PhonegapActivity.class);
		shortcutIntent.putExtra(App.class.getSimpleName(), appId);
		shortcutIntent.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
		context.sendBroadcast(addIntent);
	}

	public  static String getAppFolder(long appId) {
		return getSaveFolder()+appId;
	}

	public static String getSaveFolder() {
		return Environment.getExternalStorageDirectory() + "/Android/data/com.appshed.appstore/appstore/";
	}
}

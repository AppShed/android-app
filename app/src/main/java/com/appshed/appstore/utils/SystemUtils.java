package com.appshed.appstore.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.appshed.appstore.entities.Cache;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class SystemUtils {

	private static final String TAG = SystemUtils.class.getSimpleName();

	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	public static Cache cache;
	public static String SENDER_ID = "800215983654";

	public static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}

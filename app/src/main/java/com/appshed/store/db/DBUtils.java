package com.appshed.store.db;

import android.content.Context;

import com.appshed.store.entities.App;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.db.RightDBUtils;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class DBUtils extends RightDBUtils {

	public static DBUtils newInstance(Context context, String name, int version) {
		DBUtils dbUtils = new DBUtils();
		dbUtils.setDBContext(context, name, version);
		return dbUtils;
	}

	public int getAppVersion(long appId) {
		RightList<App> apps = getAllWhere(String.format("id = %d limit 1", appId), App.class);
		if (!apps.isEmpty()) {
			return apps.get(0).getVersion();
		}
		return 0;
	}


}

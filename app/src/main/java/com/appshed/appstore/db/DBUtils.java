package com.appshed.appstore.db;

import android.content.Context;

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


}

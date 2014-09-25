package com.appshed.store.applications;

import android.app.Application;
import android.graphics.Bitmap;

import com.appshed.store.db.DBUtils;
import com.appshed.store.utils.SystemUtils;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class AppStoreApplication extends Application {

	public static DBUtils dbUtils;

	@Override
	public void onCreate() {
		super.onCreate();
		SystemUtils.getCache(getApplicationContext());
		initImageLoader();
		dbUtils = DBUtils.newInstance(this, "appstore.sqlite", 1);
	}

	private void initImageLoader() {
		SystemUtils.IMAGELOADER = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.displayer(new FadeInBitmapDisplayer(500))
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.discCacheExtraOptions(480, 800, Bitmap.CompressFormat.PNG, 75, null).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.defaultDisplayImageOptions(options).build();
		SystemUtils.IMAGELOADER.init(config);
	}
}

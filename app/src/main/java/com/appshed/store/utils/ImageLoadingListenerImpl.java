package com.appshed.store.utils;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Created by Anton Maniskevich on 8/21/14.
 */
public class ImageLoadingListenerImpl implements ImageLoadingListener {
	@Override
	public void onLoadingStarted(String imageUri, View view) {

	}

	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {

	}
}

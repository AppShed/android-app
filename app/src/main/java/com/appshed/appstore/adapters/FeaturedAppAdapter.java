package com.appshed.appstore.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appshed.appstore.R;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.utils.BitmapUtils;
import com.appshed.appstore.utils.ImageLoadingListenerImpl;
import com.appshed.appstore.utils.SystemUtils;
import com.rightutils.rightutils.collections.RightList;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class FeaturedAppAdapter extends ArrayAdapter<App> {

	public FeaturedAppAdapter(Context context, RightList<App> apps) {
		super(context, R.layout.item_featured_app, apps);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(getContext(), R.layout.item_featured_app, null);
		App app = getItem(position);
		((TextView) view.findViewById(R.id.txt_title)).setText(app.getName());
		((TextView) view.findViewById(R.id.txt_description)).setText(app.getDescription());
		final ImageView appBg = (ImageView) view.findViewById(R.id.img_app_bg);
		SystemUtils.IMAGELOADER.displayImage(app.getFeaturedImage(), appBg);
		final ImageView icon = (ImageView) view.findViewById(R.id.img_icon);
		SystemUtils.IMAGELOADER.displayImage(app.getIcon(), icon, new ImageLoadingListenerImpl() {

			@Override
			public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
				if (loadedImage != null) {
					icon.setImageBitmap(BitmapUtils.getCroppedRoundBitmap(loadedImage));
				} else {
					icon.setImageResource(R.drawable.ic_launcher);
				}
			}
		});
		return view;
	}
}

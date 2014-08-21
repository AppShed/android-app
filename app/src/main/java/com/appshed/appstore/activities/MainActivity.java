package com.appshed.appstore.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import com.appshed.appstore.R;
import com.appshed.appstore.fragments.AppGalleryFragment;
import com.appshed.appstore.fragments.PlaceholderFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rightutils.rightutils.activities.RightFragmentActivityNew;

/**
 * Created by Anton Maniskevich on 8/20/14.
 */
public class MainActivity extends RightFragmentActivityNew implements View.OnClickListener {

	private SlidingMenu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initActivity(R.id.fragment_container, new AppGalleryFragment());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.fragment_container).setOnClickListener(this);
		setUpMenu();
	}

	public void setUpMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
//		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(android.R.color.transparent);
		menu.setBehindOffset(countWidth(context));
		menu.setFadeEnabled(false);
//		menu.setShadowDrawable(R.drawable.menu_shadow);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.sliding_menu);
		menu.setSlidingEnabled(true);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}

	private static int countWidth(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.x / 4;
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.toggle();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		menu.toggle();
	}
}

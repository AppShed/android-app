package com.appshed.store.activities;

import android.content.Intent;
import android.os.Bundle;

import com.appshed.store.R;
import com.rightutils.rightutils.activities.RightSplashActivity;

/**
 * Created by Anton Maniskevich on 9/1/14.
 */
public class SplashScreen extends RightSplashActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
//		final ImageView animImageView = (ImageView) findViewById(R.id.myImageView);
//		CyclicTransitionDrawable ctd = new CyclicTransitionDrawable(new Drawable[] {
//				getResources().getDrawable(R.drawable.anim_blue_1),
//				getResources().getDrawable(R.drawable.anim_blue_2),
//				getResources().getDrawable(R.drawable.anim_blue_3),
//				getResources().getDrawable(R.drawable.anim_blue_4),
//				getResources().getDrawable(R.drawable.anim_blue_5),
//				getResources().getDrawable(R.drawable.anim_blue_6),
//				getResources().getDrawable(R.drawable.anim_blue_7),
//				getResources().getDrawable(R.drawable.anim_blue_8)
//		});
//		animImageView.setImageDrawable(ctd);
//		ctd.startTransition(750, 0);
	}

	@Override
	public void doStart() {
		startActivity(new Intent(SplashScreen.this, MainActivityNew.class));
	}
}

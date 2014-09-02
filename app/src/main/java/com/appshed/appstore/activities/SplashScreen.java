package com.appshed.appstore.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import com.appshed.appstore.R;
import com.appshed.appstore.utils.CyclicTransitionDrawable;
import com.rightutils.rightutils.activities.RightSplashActivity;

/**
 * Created by Anton Maniskevich on 9/1/14.
 */
public class SplashScreen extends RightSplashActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setSplashDelay(15000);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		final ImageView animImageView = (ImageView) findViewById(R.id.myImageView);
		CyclicTransitionDrawable ctd = new CyclicTransitionDrawable(new Drawable[] {
				getResources().getDrawable(R.drawable.anim_1),
				getResources().getDrawable(R.drawable.anim_2),
				getResources().getDrawable(R.drawable.anim_3),
				getResources().getDrawable(R.drawable.anim_4),
				getResources().getDrawable(R.drawable.anim_5),
				getResources().getDrawable(R.drawable.anim_6),
				getResources().getDrawable(R.drawable.anim_7),
				getResources().getDrawable(R.drawable.anim_8)
		});
		animImageView.setImageDrawable(ctd);
		ctd.startTransition(150, 0);
//		animImageView.setBackgroundResource(R.drawable.progress_bar_list);
//		animImageView.post(new Runnable() {
//			@Override
//			public void run() {
//				AnimationDrawable frameAnimation = (AnimationDrawable) animImageView.getBackground();
//				frameAnimation.setEnterFadeDuration(100);
//				frameAnimation.setExitFadeDuration(100);
//				frameAnimation.start();
//			}
//		});
	}

	@Override
	public void doStart() {
		startActivity(new Intent(SplashScreen.this, MainActivity.class));
	}
}

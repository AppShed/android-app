package com.appshed.appstore.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import com.appshed.appstore.R;
import com.rightutils.rightutils.activities.RightSplashActivity;

/**
 * Created by Anton Maniskevich on 9/1/14.
 */
public class SplashScreen extends RightSplashActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		final ImageView animImageView = (ImageView) findViewById(R.id.myImageView);
		animImageView.setBackgroundResource(R.drawable.progress_bar_list);
		animImageView.post(new Runnable() {
			@Override
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) animImageView.getBackground();
				frameAnimation.start();
			}
		});
	}

	@Override
	public void doStart() {
		startActivity(new Intent(SplashScreen.this, MainActivity.class));
	}
}

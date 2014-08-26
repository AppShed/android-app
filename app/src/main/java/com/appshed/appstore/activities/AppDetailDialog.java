package com.appshed.appstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appshed.appstore.R;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.services.RetrieveAppService;
import com.appshed.appstore.utils.SystemUtils;

import java.io.File;

/**
 * Created by Anton Maniskevich on 8/26/14.
 */
public class AppDetailDialog extends Activity implements View.OnClickListener{

	private static final String TAG = AppDetailDialog.class.getSimpleName();

	private static final int UPDATE_DELAY = 200;
	public static final String LAUNCH_APP = "LAUNCH APP";
	public static final String GET_THIS_APP = "GET THIS APP";
	private App selectedApp;

	private Handler progressHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateAppLoading();
			sendEmptyMessageDelayed(0, UPDATE_DELAY);
		}
	};

	private void updateAppLoading() {
		if (selectedApp != null) {
			if (RetrieveAppService.getProgress(selectedApp.getId()) == 0) {
				appLoaderProgress.setVisibility(View.VISIBLE);
				install.setVisibility(View.INVISIBLE);
			} else {
				appLoaderProgress.setVisibility(View.INVISIBLE);
				final String appFolder = SystemUtils.getAppFolder(selectedApp);
				if (new File(appFolder).exists()) {
					install.setText(LAUNCH_APP);
				} else {
					install.setText(GET_THIS_APP);
				}
				install.setVisibility(View.VISIBLE);
			}
		}
	}

	private ProgressBar appLoaderProgress;
	private TextView install;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		selectedApp = (App) getIntent().getExtras().getSerializable(App.class.getSimpleName());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_app_detail);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		install = (TextView) findViewById(R.id.txt_get_this_app);
		appLoaderProgress = (ProgressBar) findViewById(R.id.app_loaging_progress_bar);
		SystemUtils.IMAGELOADER.displayImage(selectedApp.getIcon(), ((ImageView) findViewById(R.id.img_app_icon)));
		((TextView) findViewById(R.id.txt_title)).setText(selectedApp.getName());
		((TextView) findViewById(R.id.txt_description)).setText(selectedApp.getDescription());
		final String appFolder = SystemUtils.getAppFolder(selectedApp);
		install.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (new File(appFolder).exists()) {
					Log.i(TAG, appFolder);
					startActivity(new Intent(AppDetailDialog.this, PhonegapActivity.class).putExtra(App.class.getSimpleName(), selectedApp.getId()));
				} else {
					startService(new Intent(AppDetailDialog.this, RetrieveAppService.class)
							.putExtra(RetrieveAppService.RETRATIVE_TYPE, RetrieveAppService.LOAD_APP)
							.putExtra(App.class.getSimpleName(), selectedApp));
				}
			}
		});
		progressHandler.sendEmptyMessageDelayed(0, 0);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onDestroy() {
		progressHandler.removeMessages(0);
		super.onDestroy();
	}
}

package com.appshed.appstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.appshed.appstore.utils.BitmapUtils;
import com.appshed.appstore.utils.ImageLoadingListenerImpl;
import com.appshed.appstore.utils.SystemUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by Anton Maniskevich on 8/26/14.
 */
public class AppDetailDialog extends Activity implements View.OnClickListener {

	private static final String TAG = AppDetailDialog.class.getSimpleName();

	private static final int UPDATE_DELAY = 200;
	public static final String LAUNCH_APP = "LAUNCH APP";
	public static final String GET_THIS_APP = "GET THIS APP";
	private ProgressBar loadingProgressbar;
	private View downloadContainer;
	private View topButtonContainer;
	private App selectedApp;
	private TextView progressSize;
	private TextView progressPercent;

	private Handler progressHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateAppLoading();
			sendEmptyMessageDelayed(0, UPDATE_DELAY);
		}
	};

	private void updateAppLoading() {
		if (selectedApp != null) {
			long progress = RetrieveAppService.getProgress(selectedApp.getId());
			if (progress >= 0) {
				if (progress == 0) {
					loadingProgressbar.setIndeterminate(true);
					progressSize.setText("Loading...");
					progressPercent.setText("");
				} else {
					long length = RetrieveAppService.getLength(selectedApp.getId());
					if (length != progress) {
						double percentProgress = ((double) progress) / ((double) length);
						loadingProgressbar.setIndeterminate(false);
						loadingProgressbar.setProgress((int) Math.round(percentProgress * 100));
						progressPercent.setText(String.format("%.1f%%", percentProgress * 100));
						progressSize.setText(String.format("%s/%s", FileUtils.byteCountToDisplaySize(progress), FileUtils.byteCountToDisplaySize(length)));
					} else {
						loadingProgressbar.setIndeterminate(true);
						progressSize.setText("Installing...");
						progressPercent.setText("");
					}
				}
				downloadContainer.setVisibility(View.VISIBLE);

				install.setVisibility(View.INVISIBLE);
			} else {
				downloadContainer.setVisibility(View.GONE);
				final String appFolder = SystemUtils.getAppFolder(selectedApp.getId());
				if (new File(appFolder).exists()) {
					install.setText(LAUNCH_APP);
					topButtonContainer.setVisibility(View.VISIBLE);
				} else {
					install.setText(GET_THIS_APP);
					topButtonContainer.setVisibility(View.GONE);
				}
				install.setVisibility(View.VISIBLE);
			}
		}
	}

	private TextView install;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		selectedApp = (App) getIntent().getExtras().getSerializable(App.class.getSimpleName());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_app_detail);
		if (getIntent().getExtras().getBoolean(Boolean.class.getSimpleName(), true)) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		downloadContainer = findViewById(R.id.download_container);
		topButtonContainer = findViewById(R.id.top_button_container);
		loadingProgressbar = (ProgressBar) findViewById(R.id.loading_progress);
		progressSize = (TextView) findViewById(R.id.txt_progress_size);
		progressPercent = (TextView) findViewById(R.id.txt_progress_percent);
		findViewById(R.id.img_cancel_downloading).setOnClickListener(this);

		install = (TextView) findViewById(R.id.txt_get_this_app);

		findViewById(R.id.txt_close).setOnClickListener(this);
		findViewById(R.id.img_share).setOnClickListener(this);
		findViewById(R.id.txt_remove_app).setOnClickListener(this);
		findViewById(R.id.txt_update_app).setOnClickListener(this);

		SystemUtils.IMAGELOADER.displayImage(selectedApp.getFeaturedImage(), ((ImageView) findViewById(R.id.img_app_icon)));
		final ImageView icon = (ImageView) findViewById(R.id.img_icon);
		SystemUtils.IMAGELOADER.displayImage(selectedApp.getIcon(), icon, new ImageLoadingListenerImpl() {

			@Override
			public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
				if (loadedImage != null) {
					icon.setImageBitmap(BitmapUtils.getCroppedRoundBitmap(loadedImage));
				} else {
					icon.setImageResource(R.drawable.ic_launcher);
				}
			}
		});
		((TextView) findViewById(R.id.txt_title)).setText(selectedApp.getName());
		((TextView) findViewById(R.id.txt_description)).setText(selectedApp.getDescription());

		install.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		progressHandler.sendEmptyMessageDelayed(0, 0);
	}

	@Override
	protected void onPause() {
		progressHandler.removeMessages(0);
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.txt_get_this_app:
				final String appFolder = SystemUtils.getAppFolder(selectedApp.getId());
				if (new File(appFolder).exists()) {
					startActivity(new Intent(AppDetailDialog.this, PhonegapActivity.class).putExtra(App.class.getSimpleName(), selectedApp.getId()));
				} else {
					startService(new Intent(AppDetailDialog.this, RetrieveAppService.class)
							.putExtra(RetrieveAppService.RETRIEVE_TYPE, RetrieveAppService.LOAD_APP)
							.putExtra(App.class.getSimpleName(), selectedApp));
				}
				break;
			case R.id.txt_close:
				finish();
				break;
			case R.id.txt_remove_app:
				//TODO
				break;
			case R.id.txt_update_app:
				//TODO
				break;
			case R.id.img_cancel_downloading:

				break;
			case R.id.img_share:
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, String.format(SystemUtils.SHARE_URL,selectedApp.getId()));
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
				break;

		}
	}
}

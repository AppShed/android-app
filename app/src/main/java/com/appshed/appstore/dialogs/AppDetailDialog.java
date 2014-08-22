package com.appshed.appstore.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.appshed.appstore.R;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.fragments.AppDetailFragment;

/**
 * Created by Anton Maniskevich on 8/22/14.
 */
public class AppDetailDialog extends DialogFragment implements View.OnClickListener {

	private App app;

	public static AppDetailDialog newInstance(App app) {
		AppDetailDialog fragment = new AppDetailDialog();
		fragment.setApp(app);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.dialog_app_detail, null);
//		view.findViewById(R.id.txt_submit).setOnClickListener(this);
//		progressBar = view.findViewById(R.id.progress_bar);
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		return dialog;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onPause() {
		dismiss();
		super.onPause();
	}

	public void setApp(App app) {
		this.app = app;
	}
}

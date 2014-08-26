package com.appshed.appstore.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appshed.appstore.R;
import com.appshed.appstore.activities.PhonegapActivity;
import com.appshed.appstore.adapters.AppAdapter;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.services.RetrieveAppService;
import com.appshed.appstore.tasks.RetrieveApps;
import com.appshed.appstore.utils.BitmapUtils;
import com.appshed.appstore.utils.ImageLoadingListenerImpl;
import com.appshed.appstore.utils.SystemUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rightutils.rightutils.collections.RightList;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class AppsByCategoryFragment extends Fragment implements OnBackPressed {

	private static final String TAG = AppsByCategoryFragment.class.getSimpleName();

	private static final int UPDATE_DELAY = 200;
	private RightList<App> apps = new RightList<App>();
	private PullToRefreshListView pullToRefreshListView;
	private ListView actualListView;
	private AppAdapter adapter;
	private View progressBar;
	private int bgDrawable;
	private String category;
	private View appDetailView;
	private ProgressBar appLoaderProgress;
	private TextView install;
	private App selectedApp;

	private Handler progressHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateAppLoading();
			sendEmptyMessageDelayed(0, UPDATE_DELAY);
		}
	};

	private void updateAppLoading() {
		if (appDetailView.getVisibility() == View.VISIBLE) {
			if (selectedApp != null) {
				if (RetrieveAppService.getProgress(selectedApp.getId()) == 0) {
					appLoaderProgress.setVisibility(View.VISIBLE);
					install.setVisibility(View.INVISIBLE);
				} else {
					appLoaderProgress.setVisibility(View.INVISIBLE);
					final String appFolder = Environment.getExternalStorageDirectory() + "/download/appstore/"+selectedApp.getId();
					if (new File(appFolder).exists()) {
						install.setText("LAUNCH APP");
					} else {
						install.setText("GET THIS APP");
					}
					install.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	public static AppsByCategoryFragment newInstance(int bgDrawable, String category) {
		AppsByCategoryFragment fragment = new AppsByCategoryFragment();
		fragment.setBgDrawable(bgDrawable);
		fragment.setCategory(category);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_apps_by_category, null);
		((ImageView) view.findViewById(R.id.img_category_icon)).setImageResource(bgDrawable);
		((TextView) view.findViewById(R.id.txt_category)).setText(category);
		progressBar = view.findViewById(R.id.progress_bar);
		setUpDialog(view);
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new RetrieveApps(getActivity(), null, AppsByCategoryFragment.this).setCategory(category).execute();
			}
		});
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

		actualListView = pullToRefreshListView.getRefreshableView();
		registerForContextMenu(actualListView);

		adapter = new AppAdapter(getActivity(), apps);
		actualListView.setAdapter(adapter);
		actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				((RightFragmentActivityNew) getActivity()).pushFragment(AppDetailFragment.newInstance(apps.get(position-1)));
//				AppDetailDialog.newInstance(apps.get(position - 1)).show(getFragmentManager(), "showDialog");
				showDialog(apps.get(position - 1));
			}
		});
		if (apps.isEmpty()) {
			new RetrieveApps(getActivity(), progressBar, AppsByCategoryFragment.this).setCategory(category).execute();
		}
		return view;
	}

	public void addApps(RightList<App> newApps) {
		if (newApps.size() > 0) {
			apps.addAll(newApps);
			Collections.sort(this.apps, new Comparator<App>() {
				@Override
				public int compare(App lhs, App rhs) {
					return (int) (rhs.getId() - lhs.getId());
				}
			});
		}
		pullToRefreshListView.post(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
				pullToRefreshListView.onRefreshComplete();
			}
		});
	}

	public void setUpDialog(View view) {
		appDetailView = view.findViewById(R.id.dialog_app_detail);
		install = (TextView) appDetailView.findViewById(R.id.txt_get_this_app);
		appLoaderProgress = (ProgressBar) appDetailView.findViewById(R.id.app_loaging_progress_bar);
	}

	public void showDialog(final App app) {
		selectedApp = app;
		appDetailView.setOnClickListener(null);
		SystemUtils.IMAGELOADER.displayImage(app.getIcon(), ((ImageView) appDetailView.findViewById(R.id.img_app_icon)));
		((TextView) appDetailView.findViewById(R.id.txt_title)).setText(app.getName());
		((TextView) appDetailView.findViewById(R.id.txt_description)).setText(app.getDescription());
		final String appFolder = Environment.getExternalStorageDirectory() + "/download/appstore/"+app.getId();
		if (new File(appFolder).exists()) {
			install.setText("LAUNCH APP");
		} else {
			install.setText("GET THIS APP");
		}
		install.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (new File(appFolder).exists()) {
					Log.i(TAG, appFolder);
					startActivity(new Intent(getActivity(), PhonegapActivity.class).putExtra(App.class.getSimpleName(), app.getId()));
				} else {
					getActivity().startService(new Intent(getActivity(), RetrieveAppService.class)
							.putExtra(RetrieveAppService.RETRATIVE_TYPE, RetrieveAppService.LOAD_APP)
							.putExtra(App.class.getSimpleName(), app));
				}
			}
		});
		appDetailView.setVisibility(View.VISIBLE);
		progressHandler.sendEmptyMessageDelayed(0, 0);
	};

	public void hideDialog() {
		appDetailView.setVisibility(View.GONE);
		progressHandler.removeMessages(0);
		selectedApp = null;
	}

	public boolean isDialogShowing() {
		return appDetailView.getVisibility() == View.VISIBLE;
	}

	public void setBgDrawable(int bgDrawable) {
		this.bgDrawable = bgDrawable;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public boolean onBackPressed() {
		if (isDialogShowing()) {
			hideDialog();
			return false;
		}
		return true;
	}
}
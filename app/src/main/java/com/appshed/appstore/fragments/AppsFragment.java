package com.appshed.appstore.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.appshed.appstore.R;
import com.appshed.appstore.adapters.AppAdapter;
import com.appshed.appstore.entities.App;
import com.appshed.appstore.tasks.RetrieveApps;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rightutils.collections.RightList;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class AppsFragment extends Fragment {

	private static final String TAG = AppsFragment.class.getSimpleName();
	private RightList<App> apps = new RightList<App>();
	private PullToRefreshListView pullToRefreshListView;
	private ListView actualListView;
	private AppAdapter adapter;
	private View progressBar;

	public static AppsFragment newInstance() {
		AppsFragment fragment = new AppsFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_apps, null);
		progressBar = view.findViewById(R.id.progress_bar);
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.getCurrentMode().showHeaderLoadingLayout()) {
//					new RetrieveTopics(getActivity(), TopicsFragment.this, company).setTimestampTop(timestampTop).execute();
				} else {
//					new RetrieveTopics(getActivity(), TopicsFragment.this, company).setTimestampBottom(timestampBottom).execute();
					new RetrieveApps(getActivity(), null, AppsFragment.this).execute();
				}
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
//				((RightFragmentActivityNew) getActivity()).pushFragment(PostsFragment.newInstance(company, topics.get(position - 1)));
			}
		});
		if (apps.isEmpty()) {
			new RetrieveApps(getActivity(), progressBar, AppsFragment.this).execute();
		}
		return view;
	}

	public void addApps(RightList<App> newApps) {
		if (newApps.size() > 0) {
			apps.addAll(newApps);
//			Collections.sort(this.topics, new Comparator<Topic>() {
//				@Override
//				public int compare(Topic lhs, Topic rhs) {
//					return (int) (rhs.getCreated() - lhs.getCreated());
//				}
//			});
		}
		pullToRefreshListView.post(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
				pullToRefreshListView.onRefreshComplete();
			}
		});
	}
}

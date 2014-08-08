package com.appshed.appstore.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appshed.appstore.R;
import com.appshed.appstore.entities.App;
import com.rightutils.collections.RightList;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class AppAdapter extends ArrayAdapter<App> {

	public AppAdapter(Context context, RightList<App> apps) {
		super(context, R.layout.item_app, apps);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(getContext(), R.layout.item_app, null);
		App app = getItem(position);
		((TextView) view.findViewById(R.id.txt_title)).setText(app.getName());
		return view;
	}
}

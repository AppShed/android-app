package com.appshed.appstore.entities;

import com.appshed.appstore.R;

import java.io.Serializable;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class Cache implements Serializable {

	private String regId;
	private int appLayout;

	public Cache() {
		this.appLayout = R.layout.item_tile_app;
	}

	@Override
	public String toString() {
		return "Cache{" +
				"regId='" + regId + '\'' +
				", appLayout=" + appLayout +
				'}';
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public int getAppLayout() {
		return appLayout;
	}

	public void setAppLayout(int appLayout) {
		this.appLayout = appLayout;
	}
}

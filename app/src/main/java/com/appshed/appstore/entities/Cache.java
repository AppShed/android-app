package com.appshed.appstore.entities;

import java.io.Serializable;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class Cache implements Serializable {

	private String regId;

	public Cache() {
	}

	@Override
	public String toString() {
		return "Cache{" +
				"regId='" + regId + '\'' +
				'}';
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}
}

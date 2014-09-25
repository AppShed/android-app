package com.appshed.store.entities;

import java.io.Serializable;

/**
 * Created by Anton Maniskevich on 9/8/14.
 */
public class User implements Serializable {

	private String name;
	private String basicAuth;

	public User() {
	}

	public User(String name, String basicAuth) {
		this.name = name;
		this.basicAuth = basicAuth;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", basicAuth='" + basicAuth + '\'' +
				'}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBasicAuth() {
		return basicAuth;
	}

	public void setBasicAuth(String basicAuth) {
		this.basicAuth = basicAuth;
	}
}

package com.appshed.appstore.entities;

import java.io.Serializable;

/**
 * Created by Anton Maniskevich on 8/8/14.
 */
public class App implements Serializable {

	private long id;
	private String name;
	private String description;
	private String zip;
	private String icon;

	public App() {
	}

	public App(long id, String name, String description, String zip, String icon) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.zip = zip;
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "App{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", zip='" + zip + '\'' +
				", icon='" + icon + '\'' +
				'}';
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}

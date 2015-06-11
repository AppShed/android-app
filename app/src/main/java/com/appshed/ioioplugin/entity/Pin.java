package com.appshed.ioioplugin.entity;

import org.json.JSONObject;

abstract public class Pin{
	public static final String PIN_INPUT_ANALOG = "analogInput";
	public static final String PIN_OUTPUT_PWM = "pwmOutput";
	public static final String PIN_OUTPUT_DIGITAL = "digitalOutput";
	public static final String PIN_INPUT_DIGITAL = "digitalInput";
	public int pin;
	
	public abstract JSONObject getJson();
}
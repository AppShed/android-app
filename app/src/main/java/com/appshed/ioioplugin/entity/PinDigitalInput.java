package com.appshed.ioioplugin.entity;

import ioio.lib.api.DigitalInput;

import org.json.JSONException;
import org.json.JSONObject;

public class PinDigitalInput extends Pin{
	public boolean input = false;
	public DigitalInput digitalInput;
	
	public PinDigitalInput(int pin){
		this.pin = pin;
	}

	@Override
	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("pin",pin);
			json.put("value",input);
			json.put("class",PIN_INPUT_DIGITAL);
			json.put("info",toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public String toString() {
		return "PinDigitalInput [input=" + input + ", digitalInput=" + digitalInput + "]";
	}
	
	

}
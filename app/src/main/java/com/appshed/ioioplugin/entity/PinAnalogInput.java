package com.appshed.ioioplugin.entity;

import org.json.JSONException;
import org.json.JSONObject;

import ioio.lib.api.AnalogInput;

public class PinAnalogInput extends Pin{
	public float output;
	public AnalogInput analogInput;

	public PinAnalogInput(int pin){
		this.pin = pin;
	}

	@Override
	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("pin",pin);
			json.put("value",output);
			json.put("class",PIN_INPUT_ANALOG);
			json.put("info",toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public String toString() {
		return "PinAnalogInput [output=" + output + ", analogInput=" + analogInput + "]";
	}

}
package com.appshed.ioioplugin.entity;

import ioio.lib.api.DigitalOutput;

import org.json.JSONException;
import org.json.JSONObject;

public class PinDigitalOutput extends Pin {
	public boolean output = false;
	public DigitalOutput digitalOutput;
	
	public PinDigitalOutput(int pin){
		this.pin = pin;
	}

	@Override
	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("pin",pin);
			json.put("value",output);
			json.put("class",PIN_OUTPUT_DIGITAL);
			json.put("info",toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public String toString() {
		return "PinDigitalOutput [output=" + output + ", digitalOutput=" + digitalOutput + "]";
	}
	
	

}

package com.appshed.ioioplugin.entity;

import org.json.JSONException;
import org.json.JSONObject;

import ioio.lib.api.PwmOutput;

public class PinPwmOutput extends Pin{
	

	public int freq;
	public PwmOutput pwmOutput;

	public PinPwmOutput(int pin,int freq){
		this.pin = pin;
		this.freq = freq;
	}

	@Override
	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("pin",pin);
			json.put("freq",freq);
			json.put("class",PIN_OUTPUT_PWM);
			json.put("info",toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public String toString() {
		return "PinPwmOutput [freq=" + freq + ", pwmOutput=" + pwmOutput + "]";
	}
	
	

}
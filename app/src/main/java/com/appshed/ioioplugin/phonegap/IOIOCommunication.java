package com.appshed.ioioplugin.phonegap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appshed.ioioplugin.service.IOIOCOmmunicationService;

import android.content.Context;
import android.content.Intent;

public class IOIOCommunication extends CordovaPlugin{
	public static final String TAG = IOIOCommunication.class.getSimpleName();
	public static final String ACTION_START_SERVICE = "openIOIO";
	public static final String ACTION_STOP_SERVICE = "stopIOIO";
	public static final String ACTION_REPEAT_MAIN_LISTENER = "repeatMainListener";

	public static final String ACTION_SET_PWMOUTPUT = "setPwmOutput";
	public static final String ACTION_SET_DIGITALOUTPUT = "setDigitalOutput";
	public static final String ACTION_TOGGLE_DIGITALOUTPUT = "toggleDigitalOutput";

	@Override
	public boolean execute(String action, JSONArray args,CallbackContext callbackContext) throws JSONException {
		// TODO Auto-generated method stub

		IOIOCOmmunicationService.lastCallbackFromJS = System.currentTimeMillis();
		
		Context context = this.cordova.getActivity();
		System.out.println(TAG + " action " + action);
		//System.out.println(TAG + " args " + args.toString());

		if(ACTION_START_SERVICE.equals(action)){
			IOIOCOmmunicationService.eventListener = callbackContext;
		    IOIOCOmmunicationService.initPins();

		    JSONObject options =  args.getJSONObject(0);
		    if(options == null){
		    	return false;
		    }

		    if(options.has("inputs")){ // CHECK INPUTS
		    	JSONObject inputs = options.getJSONObject("inputs");
		    	
		    	if(inputs.has("digital")){ // SET DIGITAL
		    		JSONArray digital = inputs.getJSONArray("digital");
		    		for(int i=0;i<digital.length();i++){
				    	IOIOCOmmunicationService.addDigitalInput(digital.getInt(i));	
		    		}
		    	}

		    	if(inputs.has("analogue")){ // SET ANALOGUE
		    		JSONArray analogue = inputs.getJSONArray("analogue");
		    		for(int i=0;i<analogue.length();i++){
				    	IOIOCOmmunicationService.addAnalogInput(analogue.getInt(i));	
		    		}
		    	}
		    }

		    if(options.has("outputs")){ // CHECK OUTPUT
	    		JSONObject output = options.getJSONObject("outputs");
	    		
	    		if(output.has("digital")){
	    			JSONArray digitals = output.getJSONArray("digital");
	    			for(int i=0;i<digitals.length();i++){
	    				IOIOCOmmunicationService.addDigitalOutput(digitals.getInt(i));
	    			}
	    		}

		    	if(output.has("pwm")){ // SET PWM
				    JSONArray pwmArray = output.getJSONArray("pwm");
				    for(int i=0;i<pwmArray.length();i++){
				    	JSONObject pwm = pwmArray.getJSONObject(i);
				    	IOIOCOmmunicationService.addPwnOutput(pwm.getInt("pin"),pwm.getInt("freq"));
				    }
		    	}
		    }
			context.stopService(new Intent(context, IOIOCOmmunicationService.class));
			context.startService(new Intent(context, IOIOCOmmunicationService.class));
			return true;
		}

		if(ACTION_STOP_SERVICE.equals(action)){
			IOIOCOmmunicationService.eventListener = null;
			context.stopService(new Intent(context, IOIOCOmmunicationService.class));
		}

		if(ACTION_SET_PWMOUTPUT.equals(action)){
			IOIOCOmmunicationService.setPwmOutput(args.getInt(0),args.getInt(1));			
		}
		
		if(ACTION_SET_DIGITALOUTPUT.equals(action)){
			IOIOCOmmunicationService.setDigitalOutput(args.getInt(0),args.getBoolean(1));			
		}		

		if(ACTION_REPEAT_MAIN_LISTENER.equals(action)){			
			 // do nothing
		}
		
		if(ACTION_TOGGLE_DIGITALOUTPUT.equals(action)){
			IOIOCOmmunicationService.toggleDigitalOutput(args.getInt(0));
		}
		
		callbackContext.success();
		return true;
	}
}
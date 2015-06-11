package com.appshed.ioioplugin.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;

import com.appshed.ioioplugin.entity.Pin;
import com.appshed.ioioplugin.entity.PinAnalogInput;
import com.appshed.ioioplugin.entity.PinDigitalInput;
import com.appshed.ioioplugin.entity.PinDigitalOutput;
import com.appshed.ioioplugin.entity.PinPwmOutput;

import android.content.Intent;
import android.os.IBinder;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.IOIO.VersionType;
import ioio.lib.api.Uart.StopBits;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

/**
 * An example IOIO service. While this service is alive, it will attempt to
 * connect to a IOIO and blink the LED. A notification will appear on the
 * notification bar, enabling the user to stop the service.
 */
public class IOIOCOmmunicationService extends IOIOService {
	public static final int PWN_MAX_FREQ = 10000;
	public static Map<Integer,Pin> pins;
	public static CallbackContext eventListener;

	public static final long delay = 100;
	public static boolean activeLed = true;
	public static long lastCallbackFromJS = 0;
	public static long lastCallbackToJS = 0;
	
	public static void initPins(){
		System.out.print("initPins()");
		pins = new HashMap<Integer,Pin>();
	}

	public static void addAnalogInput(int pinPort){
		pins.put(pinPort,new PinAnalogInput(pinPort));
	}

	public static void addPwnOutput(int pinPort,int freq){
		pins.put(pinPort,new PinPwmOutput(pinPort,freq));
	}
	
	public static void addDigitalOutput(int pinPort){
		pins.put(pinPort,new PinDigitalOutput(pinPort));
	}
	
	public static void addDigitalInput(int pinPort){
		pins.put(pinPort,new PinDigitalInput(pinPort));
	}

	public static void setPwmOutput(int pinPort,int freq){
		if(pins.containsKey(pinPort) && pins.get(pinPort)instanceof PinPwmOutput){
			((PinPwmOutput)pins.get(pinPort)).freq = freq; 
		}
	}
	
	public static void setDigitalOutput(int pinPort,boolean output){
		if(pins.containsKey(pinPort) && pins.get(pinPort)instanceof PinDigitalOutput){
			((PinDigitalOutput)pins.get(pinPort)).output = output;
		}
	}
	
	public static void toggleDigitalOutput(int pinPort){
		if(pins.containsKey(pinPort) && pins.get(pinPort)instanceof PinDigitalOutput){
			PinDigitalOutput pinDigitalOutput = (PinDigitalOutput)pins.get(pinPort);
			pinDigitalOutput.output = !pinDigitalOutput.output;
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new BaseIOIOLooper() {
			private DigitalOutput led_;

			/**
			 * here we need to open all ports before we would use them
			 */

			@Override
			public void incompatible() {
				JSONObject r = new JSONObject();
				try{
					r.put("IOIOLIB_VER", ioio_.getImplVersion(VersionType.IOIOLIB_VER));
					r.put("APP_FIRMWARE_VER", ioio_.getImplVersion(VersionType.APP_FIRMWARE_VER));
					r.put("BOOTLOADER_VER", ioio_.getImplVersion(VersionType.BOOTLOADER_VER));
					r.put("HARDWARE_VER", ioio_.getImplVersion(VersionType.HARDWARE_VER));
				}catch(Exception e){}
				
				PluginResult result = new PluginResult(PluginResult.Status.ERROR,r);
                result.setKeepCallback(true);
				eventListener.sendPluginResult(result);
			}
			
			@Override
			protected void setup() throws ConnectionLostException,InterruptedException {
				
				
				led_ = ioio_.openDigitalOutput(IOIO.LED_PIN);
				for(Integer pinPort:pins.keySet()){
					Pin pin = pins.get(pinPort);

					if(pin instanceof PinPwmOutput){ // PinPwmOutput
						PinPwmOutput pinPwmOutput = (PinPwmOutput)pin;
						pinPwmOutput.pwmOutput = ioio_.openPwmOutput(pinPort, PWN_MAX_FREQ); 
					}else if(pin instanceof PinAnalogInput){ // PinAnalogInput
						PinAnalogInput pinAnalogInput = (PinAnalogInput)pin;
						pinAnalogInput.analogInput = ioio_.openAnalogInput(pinPort);
					}else if(pin instanceof PinDigitalOutput){ // PinDigitalOutput
						PinDigitalOutput pinDigitalOutput = (PinDigitalOutput)pin;
						pinDigitalOutput.digitalOutput = ioio_.openDigitalOutput(pinPort);						
					}else if(pin instanceof PinDigitalInput){ // PinDigitalInput
						PinDigitalInput pinDigitalInput = (PinDigitalInput)pin;
						pinDigitalInput.digitalInput = ioio_.openDigitalInput(pinPort);
					}
				}
			}

			/**
			 * here we read/write from/to board with minimum delay of 100 milliseconds, on JS side we would increase this value 
			 */
			@Override
			public void loop() throws ConnectionLostException,InterruptedException {
								
				if(lastCallbackFromJS == 0 || (IOIOCOmmunicationService.lastCallbackToJS+delay) > System.currentTimeMillis()){
					return;
				}

				led_.write(activeLed);
				activeLed = !activeLed;
				IOIOCOmmunicationService.lastCallbackToJS = System.currentTimeMillis();
				lastCallbackFromJS = 0;

				JSONArray parameters = new JSONArray();
				for(Integer pinPort:pins.keySet()){
					Pin pin = pins.get(pinPort);
					if(pins.get(pinPort) instanceof PinPwmOutput){ // PinPwmOutput
						PinPwmOutput pinPwmOutput = (PinPwmOutput)pins.get(pinPort); 
						pinPwmOutput.pwmOutput.setPulseWidth(pinPwmOutput.freq);
						try {
							parameters.put(pinPwmOutput.getJson());
			            } catch (Exception e) {}

					}else if(pins.get(pinPort) instanceof PinAnalogInput){ // PinAnalogInput
						PinAnalogInput pinAnalogInput = (PinAnalogInput)pins.get(pinPort);
						pinAnalogInput.output = pinAnalogInput.analogInput.read();
						try {
							parameters.put(pinAnalogInput.getJson());
			            } catch (Exception e) {}
					}else if(pin instanceof PinDigitalOutput){
						PinDigitalOutput pinDigitalOutput = (PinDigitalOutput)pin;
						pinDigitalOutput.digitalOutput.write(!pinDigitalOutput.output); // TODO for some reasons, true is false........
						try {
							parameters.put(pinDigitalOutput.getJson());
			            } catch (Exception e) {}
					}else if(pin instanceof PinDigitalInput){ // PinDigitalInput
						PinDigitalInput pinDigitalInput = (PinDigitalInput)pin;
						pinDigitalInput.input = pinDigitalInput.digitalInput.read();
						try {
							parameters.put(pinDigitalInput.getJson());
			            } catch (Exception e) {}
					}
				}

				PluginResult result = new PluginResult(PluginResult.Status.OK, parameters);
                result.setKeepCallback(true);
				eventListener.sendPluginResult(result);
			}
		};
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		initPins();		
		
		/*
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		if (intent != null && intent.getAction() != null
				&& intent.getAction().equals("stop")) {
			// User clicked the notification. Need to stop the service.
			nm.cancel(0);
			stopSelf();
		} else {
 
			Notification notification = new Notification(
					R.drawable.ic_launcher, "IOIO service running",
					System.currentTimeMillis());
			notification
					.setLatestEventInfo(this, "IOIO Service", "Click to stop",
							PendingIntent.getService(this, 0, new Intent(
									"stop", null, this, this.getClass()), 0));
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			nm.notify(0, notification);
		}
		//*/
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}

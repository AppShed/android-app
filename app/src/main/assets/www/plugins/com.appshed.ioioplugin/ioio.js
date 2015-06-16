cordova.define("com.appshed.ioioplugin.IOIO", function(require, exports, module) { var ioio = {
	PIN_OUTPUT_PWM : "pwmOutput",
	PIN_OUTPUT_DIGITAL : "digitalOutput",
	PIN_INPUT_DIGITAL : "digitalInput",
	PIN_INPUT_ANALOG : "analogInput",
	pinListeners:[],
	addValuePinListener:function(pin,value,callback){
		if(this.pinListeners[pin]){
			this.pinListeners[pin].push({'prevValue':value,'value':value,'callback':callback});
		}else{
			this.pinListeners[pin] = [{'prevValue':value,'value':value,'callback':callback}];
		}
	},
	addPinListener:function(pin,callback){
		this.addValuePinListener(pin,0,callback);
	},
	open: function(options,succ,fail,allListener) {
		var that = this;
        cordova.exec(
			function(vals){ // DIRECT IOIO LISTENER
				if (succ) {
					succ();
					succ = null;
				}

				if (allListener) {
					try{
						allListener(vals);
					}catch(e){
						console.log('IOIO Callback function error' ,e);
			                }
				}

				for(var i=0;i<vals.length;i++){
					var pin = vals[i];
					switch(pin.class){
						case that.PIN_OUTPUT_DIGITAL:
							break;
						case that.PIN_OUTPUT_PWM:
							break;
						case that.PIN_INPUT_DIGITAL:
							if(that.pinListeners[pin.pin]){
								for(var j=0;j<that.pinListeners[pin.pin].length;j++){								
									that.pinListeners[pin.pin][j].callback(pin.value);
								}
							}
							break;
						case that.PIN_INPUT_ANALOG:
							if(that.pinListeners[pin.pin]){
								for(var j=0;j<that.pinListeners[pin.pin].length;j++){

									if(that.pinListeners[pin.pin][j].value >0){
										if(that.pinListeners[pin.pin][j].value == pin.value){
											continue;
										}
										if(that.pinListeners[pin.pin][j].value > that.pinListeners[pin.pin][j].prevValue
											&&
											that.pinListeners[pin.pin][j].value > pin.value
										){continue;}
									
										if(that.pinListeners[pin.pin][j].value < that.pinListeners[pin.pin][j].prevValue
											&&
											that.pinListeners[pin.pin][j].value < pin.value
										){continue;}
									}

									that.pinListeners[pin.pin][j].prevValue = pin.value;
									that.pinListeners[pin.pin][j].callback(pin.value);
								}
							}
							break;
					}
				}

				setTimeout(function(){
					cordova.exec(function(){},function(){},'IOIOCommunication','repeatMainListener',[]);
				}, options.delay);
			},
			function(params){
				succ = null;
				allListener = null;
				if(fail){
					fail(params);
				}

			},
            'IOIOCommunication',
            'openIOIO',
            [options]
        );
     },
 	close: function(succ,fail) {
    	cordova.exec(
            succ || function(){},
            fail || function(){},
            'IOIOCommunication',
            'stopIOIO',
			[]
        );
	},
 	setPwnOutput: function(pin,freq, succ, fail) {
    	cordova.exec(
            succ || function(){},
            fail || function(){},
            'IOIOCommunication',
            'setPwmOutput',
        	[pin,freq]
        ); 
	},
 	setDigitalOutput: function(pin,output, succ, fail) {
    	cordova.exec(
            succ || function(){},
            fail || function(){},
            'IOIOCommunication',
            'setDigitalOutput',
        	[pin,output]
        );
	},
 	toggleDigitalOutput: function(pin,succ, fail) {
    	cordova.exec(
            succ || function(){},
            fail || function(){},
            'IOIOCommunication',
            'toggleDigitalOutput',
        	[pin]
        ); 
	}
 	
};

window.ioio = ioio;

});

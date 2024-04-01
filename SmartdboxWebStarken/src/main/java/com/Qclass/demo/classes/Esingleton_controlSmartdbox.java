package com.Qclass.demo.classes;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Esingleton_controlSmartdbox {
	
	private static Esingleton_controlSmartdbox instance = new Esingleton_controlSmartdbox();
	
	private Esingleton_controlSmartdbox() {}
	
	public static Esingleton_controlSmartdbox getInstance() {
		
		return instance;
	}
	
	
	
	
	
	
}

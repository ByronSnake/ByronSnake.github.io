package com.Qclass.demo.classes;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



//@Configuration
//@ConfigurationProperties(prefix = "LOGA")//la config que esta en el application.properties
public class LogStarken {

	final static Logger logger =  LoggerFactory.getLogger(LogStarken.class);

	String IP;
	String sucursal;
	String stringValue;
	
	String NroModulo;
	String DeviceName;
	String datetime;
	String tipo;
	String barra;
	Object data;
	
	public LogStarken(String iP, String sucursal, String nroModulo, String deviceName, String tipo, String barra, Object data) {
		super();
		IP = iP;
		this.sucursal = sucursal;
	
		this.NroModulo = nroModulo;
		this.DeviceName = deviceName;
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		Date date = new Date(); 
		
		this.datetime = formatter.format(date);
		this.tipo = tipo;
		this.barra = barra;
		this.data = data;
	}


	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public String getNroModulo() {
		return NroModulo;
	}
	public void setNroModulo(String nroModulo) {
		NroModulo = nroModulo;
	}
	public String getDeviceName() {
		return DeviceName;
	}
	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}
	public String getDatetime() {
		return datetime;
	}
	public String getTipo() {
		return tipo;
	}
	
	public String getBarra() {
		return barra;
	}
	
	public Object getData() {
		return data;
	}


	public String SerializarJson() {
		String json = "";
		     try {
		     GsonBuilder builder = new GsonBuilder();
		     Gson gson = builder.create();
		     json = gson.toJson(this);
		    // System.out.println(json);
		     }
		     catch (Exception e){
		    	 System.out.println("ERROR, No fue posible serializar el JSON !");
		     }
		     
		     return json;

		}
	
	
	
	
}

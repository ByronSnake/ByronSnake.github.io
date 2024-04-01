package com.Qclass.demo.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class respuesta {

	private String id;
	int StatusCode;
	String Info;

	
	public respuesta(String id, int statusCode, String info) {
		super();
		this.id = id;
		StatusCode = statusCode;
		Info = info;
	}
	
	public int getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}
	public String getInfo() {
		return Info;
	}
	public void setInfo(String info) {
		Info = info;
	}
	
	
	public String SerializaJzon() {
		
		String json = "";
		
	    try {
		    GsonBuilder builder = new GsonBuilder();
		    Gson gson2 = builder.create();
		    json = gson2.toJson(this);
	    }catch (Exception e){
		    //logger.error("ERROR, No fue posible serializar el JSON !");
	    }
	     
	    return json;
	}
	
	
	
}
